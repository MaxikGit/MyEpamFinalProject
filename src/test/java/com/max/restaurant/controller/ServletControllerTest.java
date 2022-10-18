package com.max.restaurant.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.max.restaurant.utils.UtilsCommandNames.ACTION;
import static com.max.restaurant.utils.UtilsCommandNames.LOGIN;
import static org.junit.jupiter.api.Assertions.*;

class ServletControllerTest {

    @BeforeEach
    void setUp() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(request.getParameter(ACTION)).thenReturn(LOGIN);
    }

    @Test
    void doGet() {
    }

    @Test
    void doPost() {
    }
}