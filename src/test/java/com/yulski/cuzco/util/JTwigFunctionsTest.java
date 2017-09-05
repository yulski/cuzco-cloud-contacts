package com.yulski.cuzco.util;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class JTwigFunctionsTest {

    @Test
    public void pathFunctionHasExpectedNameAndAliasesTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);

        JtwigFunction function = jTwigFunctions.getPath();
        String name = function.name();
        Collection<String> aliases = function.aliases();

        assertEquals("path", name);
        assertEquals(1, aliases.size());
        assertTrue(aliases.contains("path"));
    }

    @Test
    public void pathFunctionNullArgsShouldReturnEmptyStringTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);
        FunctionRequest mockFunctionRequest = mock(FunctionRequest.class);

        when(mockFunctionRequest.getArguments()).thenReturn(null);

        Object result = jTwigFunctions.getPath().execute(mockFunctionRequest);
        assertEquals("", result);
    }

    @Test
    public void pathFunctionEmptyArgsReturnEmptyStringTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);
        FunctionRequest mockFunctionRequest = mock(FunctionRequest.class);

        when(mockFunctionRequest.getArguments()).thenReturn(new ArrayList<>());

        Object result = jTwigFunctions.getPath().execute(mockFunctionRequest);
        assertEquals("", result);
    }

    @Test
    public void pathFunctionOnlyPathArgumentReturnsOriginalPathTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);
        FunctionRequest mockFunctionRequest = mock(FunctionRequest.class);

        final String path = "/test/path";
        final List<Object> args = new ArrayList<>();
        args.add(path);

        when(mockFunctionRequest.getArguments()).thenReturn(args);

        Object result = jTwigFunctions.getPath().execute(mockFunctionRequest);
        assertEquals(path, result);
    }

    @Test
    public void pathFunctionSecondArgumentNullReturnsOriginalPathTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);
        FunctionRequest mockFunctionRequest = mock(FunctionRequest.class);

        final String path = "/test/path";
        final List<Object> args = new ArrayList<>();
        args.add(path);
        args.add(null);

        when(mockFunctionRequest.getArguments()).thenReturn(args);

        Object result = jTwigFunctions.getPath().execute(mockFunctionRequest);
        assertEquals(path, result);
    }

    @Test
    public void pathFunctionSecondArgumentIsEmptyMapReturnsOriginalPathTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);
        FunctionRequest mockFunctionRequest = mock(FunctionRequest.class);

        final String path = "/test/path";
        final List<Object> args = new ArrayList<>();
        args.add(path);
        args.add(new HashMap<String, String>());

        when(mockFunctionRequest.getArguments()).thenReturn(args);

        Object result = jTwigFunctions.getPath().execute(mockFunctionRequest);
        assertEquals(path, result);
    }

    @Test
    public void pathFunctionSecondArgumentIsNotMapReturnsOriginalPathTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);
        FunctionRequest mockFunctionRequest = mock(FunctionRequest.class);

        final String path = "/test/path";
        final List<Object> args = new ArrayList<>();
        args.add(path);
        args.add("");

        when(mockFunctionRequest.getArguments()).thenReturn(args);

        Object result = jTwigFunctions.getPath().execute(mockFunctionRequest);
        assertEquals(path, result);
    }

    @Test
    public void pathFunctionAllArgumentsPresentCallsPathGeneratorGeneratePathTest() {
        PathGenerator mockPathGenerator = spy(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);
        FunctionRequest mockFunctionRequest = mock(FunctionRequest.class);

        final String path = "/test/path/:id";
        final Map<String, String> params = new HashMap<>();
        params.put("id", "100");
        final List<Object> args = new ArrayList<>();
        args.add(path);
        args.add(params);

        when(mockFunctionRequest.getArguments()).thenReturn(args);

        jTwigFunctions.getPath().execute(mockFunctionRequest);

        verify(mockPathGenerator, times(1)).generatePath(path, params);
    }

    @Test
    public void toStringFunctionHasExpectedNameAndAliasesTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);

        JtwigFunction function = jTwigFunctions.getToString();
        String name = function.name();
        Collection<String> aliases = function.aliases();

        assertEquals("toString", name);
        assertEquals(1, aliases.size());
        assertTrue(aliases.contains("toString"));
    }

    @Test
    public void toStringFunctionNullArgumentsReturnsEmptyStringTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);
        FunctionRequest mockFunctionRequest = mock(FunctionRequest.class);

        when(mockFunctionRequest.getArguments()).thenReturn(null);

        Object result = jTwigFunctions.getToString().execute(mockFunctionRequest);

        assertEquals("", result);
    }

    @Test
    public void toStringFunctionEmptyArgumentsReturnsEmptyStringTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);
        FunctionRequest mockFunctionRequest = mock(FunctionRequest.class);

        when(mockFunctionRequest.getArguments()).thenReturn(new ArrayList<>());

        Object result = jTwigFunctions.getToString().execute(mockFunctionRequest);

        assertEquals("", result);
    }

    @Test
    public void toStringFunctionNullFirstArgumentReturnsEmptyStringTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);
        FunctionRequest mockFunctionRequest = mock(FunctionRequest.class);

        final List<Object> args = new ArrayList<>();
        args.add(null);

        when(mockFunctionRequest.getArguments()).thenReturn(args);

        Object result = jTwigFunctions.getToString().execute(mockFunctionRequest);

        assertEquals("", result);
    }

    @Test
    public void toStringFunctionWithArgumentReturnsItsToStringTest() {
        PathGenerator mockPathGenerator = mock(PathGenerator.class);
        JTwigFunctions jTwigFunctions = new JTwigFunctions(mockPathGenerator);
        FunctionRequest mockFunctionRequest = mock(FunctionRequest.class);

        final Integer i = new Integer(400);
        final List<Object> args = new ArrayList<>();
        args.add(i);

        when(mockFunctionRequest.getArguments()).thenReturn(args);

        Object result = jTwigFunctions.getToString().execute(mockFunctionRequest);

        assertEquals(i.toString(), result);
    }

}
