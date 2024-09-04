package edu.escuelaing.arem.ASE.app;

import edu.escuelaing.arem.ASE.app.MyOwnSpringboot.GetMapping;
import edu.escuelaing.arem.ASE.app.MyOwnSpringboot.RequestParam;
import edu.escuelaing.arem.ASE.app.MyOwnSpringboot.RestController;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleHttpServer {
    private static final Logger LOGGER = Logger.getLogger(SimpleHttpServer.class.getName());
    private static final int PORT = 8080;
    private static final String DEFAULT_PATH = "src/main/resources";
    private static final String INDEX_FILE = "index.html";
    private static final String SERVICE_CLASS_NAME = "edu.escuelaing.arem.ASE.app.HelloService";

    private static final Map<String, Method> endpointMap = new HashMap<>();
    private static Object serviceInstance;

    public static void main(String[] args) {
        initializeService();
        startServer();
    }

    private static void initializeService() {
        try {
            Class<?> serviceClass = Class.forName(SERVICE_CLASS_NAME);
            if (serviceClass.isAnnotationPresent(RestController.class)) {
                serviceInstance = serviceClass.getDeclaredConstructor().newInstance();
                for (Method method : serviceClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        String path = method.getAnnotation(GetMapping.class).value();
                        endpointMap.put(path, method);
                    }
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize service", e);
            System.exit(1);
        }
    }

    private static void startServer() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOGGER.info("Server is running on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(() -> handleRequest(clientSocket));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server error", e);
        } finally {
            executor.shutdown();
        }
    }

    public static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null) {
                return;
            }

            LOGGER.info("Request: " + requestLine);
            RequestDetails requestDetails = parseRequest(requestLine);

            if (requestDetails.path.startsWith("/App/")) {
                handleAppRequest(requestDetails, out);
            } else {
                Method serviceMethod = endpointMap.get(requestDetails.path);
                if (serviceMethod != null) {
                    handleServiceMethod(serviceMethod, requestDetails, out);
                } else {
                    handleStaticFile(requestDetails.path, out);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error handling request", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error closing client socket", e);
            }
        }
    }

    private static String extractServiceName(String path) {
        String[] segments = path.split("/");
        return segments.length > 2 ? "/" + segments[2] : "";
    }

    private static void handleAppRequest(RequestDetails requestDetails, OutputStream out) throws IOException {
        try {
            URL serviceUrl = new URL("http://localhost:8080" + requestDetails.path + "?" + requestDetails.queryParams);
            String path = serviceUrl.getPath();
            String query = serviceUrl.getQuery();

            LOGGER.info("Path: " + path);
            LOGGER.info("Query: " + query);

            String serviceName = extractServiceName(path);
            LOGGER.info("Service Name: " + serviceName);

            Method targetMethod = endpointMap.get(serviceName);
            if (targetMethod != null) {
                Object[] methodArguments = extractArguments(targetMethod, parseQueryString(query));
                String response = (String) targetMethod.invoke(serviceInstance, methodArguments);
                sendResponse(out, "200 OK", "text/plain", response);
            } else {
                sendResponse(out, "404 Not Found", "text/plain", "Service not found: " + serviceName);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling App request", e);
            sendResponse(out, "500 Internal Server Error", "text/plain", "Internal Server Error");
        }
    }

    private static void handleServiceMethod(Method serviceMethod, RequestDetails requestDetails, OutputStream out) throws IOException {
        try {
            Object[] methodParams = extractArguments(serviceMethod, requestDetails.queryParams);
            String response = (String) serviceMethod.invoke(serviceInstance, methodParams);
            sendResponse(out, "200 OK", "text/plain", response);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error invoking service method", e);
            sendResponse(out, "500 Internal Server Error", "text/plain", "Internal Server Error");
        }
    }

    private static void handleStaticFile(String path, OutputStream out) throws IOException {
        String response = HelloService.serveStaticFile(path);
        out.write(response.getBytes());
    }

    private static void sendResponse(OutputStream out, String status, String contentType, String content) throws IOException {
        String response = String.format("HTTP/1.1 %s\r\nContent-Type: %s\r\nContent-Length: %d\r\n\r\n%s",
                status, contentType, content.length(), content);
        out.write(response.getBytes());
    }

    public static RequestDetails parseRequest(String requestLine) {
        String[] parts = requestLine.split("\\s+");
        if (parts.length < 2) {
            return new RequestDetails(DEFAULT_PATH, new HashMap<>());
        }

        String fullPath = parts[1];
        String path = fullPath;
        Map<String, String> queryParams = new HashMap<>();

        int queryStart = fullPath.indexOf('?');
        if (queryStart != -1) {
            path = fullPath.substring(0, queryStart);
            String queryString = fullPath.substring(queryStart + 1);
            queryParams = parseQueryString(queryString);
        }

        return new RequestDetails(path.equals(DEFAULT_PATH) ? INDEX_FILE : path, queryParams);
    }

    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryParams = new HashMap<>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
        return queryParams;
    }

    public static Object[] extractArguments(Method method, Map<String, String> queryParams) {
        return java.util.Arrays.stream(method.getParameters())
                .map(param -> extractParameterValue(param, queryParams))
                .toArray();
    }

    private static Object extractParameterValue(Parameter param, Map<String, String> queryParams) {
        if (param.isAnnotationPresent(RequestParam.class)) {
            RequestParam annotation = param.getAnnotation(RequestParam.class);
            String paramName = annotation.value();
            return queryParams.getOrDefault(paramName, annotation.defaultValue());
        }
        return null;
    }

    public static class RequestDetails {
        final String path;
        final Map<String, String> queryParams;

        RequestDetails(String path, Map<String, String> queryParams) {
            this.path = path;
            this.queryParams = queryParams;
        }
    }
}
