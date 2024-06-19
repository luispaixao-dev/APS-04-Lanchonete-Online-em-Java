package org.example.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.Cookie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class validarTokenTest {
    private  validarToken vt;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setup() {
        vt = new validarToken();
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    void processRequest() {

    }
    @Test
    void testProcessRequestWithValidCookie() throws Exception {
        // Arrange
        Cookie[] cookies = {new Cookie()};

        // Act
        vt.processRequest(request, response);

        // Assert
        Assertions.assertEquals("valido", outputStream.toString().trim());
    }

    @Test
    void doGet() {
    }
}