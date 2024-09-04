package edu.escuelaing.arem.ASE.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimpleHttpServerTest {

    @Mock
    private Socket mockSocket;

    @Mock
    private InputStream mockInputStream;

    @Mock
    private OutputStream mockOutputStream;

    private SimpleHttpServer server;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
        server = new SimpleHttpServer();
    }

    @Test
    void testHandleRequest_StaticFile() throws IOException {
        String request = "GET /index.html HTTP/1.1\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        when(mockSocket.getInputStream()).thenReturn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(mockSocket.getOutputStream()).thenReturn(outputStream);

        server.handleRequest(mockSocket);

        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Type: text/html"));
    }

    @Test
    void testHandleRequest_AppEndpoint() throws IOException {
        String request = "GET /App/hello HTTP/1.1\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        when(mockSocket.getInputStream()).thenReturn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(mockSocket.getOutputStream()).thenReturn(outputStream);

        server.handleRequest(mockSocket);

        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Type: text/plain"));
        assertTrue(response.contains("Hello World!"));
    }

    @Test
    void testHandleRequest_NotFound() throws IOException {
        String request = "GET /nonexistent HTTP/1.1\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        when(mockSocket.getInputStream()).thenReturn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(mockSocket.getOutputStream()).thenReturn(outputStream);

        server.handleRequest(mockSocket);

        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 404 Not Found"));
    }

    @Test
    void testParseRequest() {
        SimpleHttpServer.RequestDetails details = server.parseRequest("GET /index.html?param1=value1 HTTP/1.1");
        assertEquals("index.html", details.path);
        assertEquals("value1", details.queryParams.get("param1"));
    }

    @Test
    void testParseQueryString() {
        Map<String, String> params = server.parseQueryString("param1=value1&param2=value2");
        assertEquals("value1", params.get("param1"));
        assertEquals("value2", params.get("param2"));
    }

    @Test
    void testExtractArguments() throws NoSuchMethodException {
        Method method = HelloService.class.getMethod("greeting", String.class);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "John");

        Object[] args = server.extractArguments(method, queryParams);
        assertEquals(1, args.length);
        assertEquals("John", args[0]);
    }
}