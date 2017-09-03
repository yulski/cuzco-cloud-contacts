package com.yulski.cuzco.util;

import org.junit.Test;
import spark.Request;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestUtilTest {

    @Test
    public void acceptsJsonReturnsFalseForNullAcceptHeaderTest() {
        Request mockRequest = mock(Request.class);

        when(mockRequest.headers("Accept")).thenReturn(null);

        boolean result = RequestUtil.acceptsJson(mockRequest);
        assertFalse(result);
    }

    @Test
    public void acceptsJsonReturnsFalseForDifferentAcceptHeaderTest() {
        Request mockRequest = mock(Request.class);

        when(mockRequest.headers("Accept")).thenReturn("text/html");

        boolean result = RequestUtil.acceptsJson(mockRequest);
        assertFalse(result);
    }

    @Test
    public void acceptsJsonReturnsTrueForCorrectAcceptJsonHeaderTest() {
        Request mockRequest = mock(Request.class);

        when(mockRequest.headers("Accept")).thenReturn("application/json");

        boolean result = RequestUtil.acceptsJson(mockRequest);
        assertTrue(result);
    }

    @Test
    public void isJsonReturnsFalseForNullContentTypeHeaderTest() {
        Request mockRequest = mock(Request.class);

        when(mockRequest.headers("Content-Type")).thenReturn(null);

        boolean result = RequestUtil.isJson(mockRequest);
        assertFalse(result);
    }

    @Test
    public void isJsonReturnsFalseForDifferentContentTypeHeaderTest() {
        Request mockRequest = mock(Request.class);

        when(mockRequest.headers("Content-Type")).thenReturn("application/x-www-form-urlencoded");

        boolean result = RequestUtil.isJson(mockRequest);
        assertFalse(result);
    }

    @Test
    public void isJsonReturnsTrueForCorrectContentTypeJsonHeaderTest() {
        Request mockRequest = mock(Request.class);

        when(mockRequest.headers("Content-Type")).thenReturn("application/json");

        boolean result = RequestUtil.isJson(mockRequest);
        assertTrue(result);
    }

}
