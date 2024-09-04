package edu.escuelaing.arem.ASE.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HelloServiceTest {

    private HelloService helloService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        helloService = new HelloService();
    }

    @Test
    void testHello() {
        assertEquals("Hello World!", HelloService.hello());
    }

    @Test
    void testPi() {
        assertEquals("PI: " + Math.PI, HelloService.pi());
    }

    @Test
    void testCapitalized() {
        assertEquals("Capitalized: HELLO WORLD!", HelloService.capitalized());
    }

    @Test
    void testHoraydia() {
        String result = HelloService.horaydia();
        assertTrue(result.startsWith("Hora y dia: "));
        LocalDateTime.parse(result.substring(12), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }


    @Test
    void testGreeting() {
        assertEquals("Hello, World!", HelloService.greeting("World"));
        assertEquals("Hello, Alice!", HelloService.greeting("Alice"));
    }

    @Test
    void testUuid() {
        String result = HelloService.uuid();
        assertTrue(result.startsWith("Your unique identifier is: "));
        UUID.fromString(result.substring(29));
    }

    @Test
    void testDayOfWeek() {
        String result = HelloService.dayOfWeek();
        assertEquals("Today is: " + LocalDate.now().getDayOfWeek(), result);
    }

    @Test
    void testServeStaticFile() throws IOException {
        // Create a test file
        Path testFile = tempDir.resolve("test.html");
        Files.write(testFile, "<html><body>Test Content</body></html>".getBytes());

        // Set the static files directory to our temp directory
        HelloService.STATIC_FILES_DIR = tempDir.toString();

        String result = HelloService.serveStaticFile("test.html");
        assertTrue(result.contains("HTTP/1.1 200 OK"));
        assertTrue(result.contains("Content-Type: text/html"));
        assertTrue(result.contains("<html><body>Test Content</body></html>"));
    }

    @Test
    void testServeStaticFileNotFound() {
        HelloService.STATIC_FILES_DIR = tempDir.toString();
        String result = HelloService.serveStaticFile("nonexistent.html");
        assertEquals("HTTP/1.1 404 Not Found\r\n\r\nFile not found", result);
    }
}