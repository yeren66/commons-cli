package org.apache.commons.cli;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommandLineTest {

    private CommandLine commandline;

    @Before
    public void setUp() {
        commandline = new CommandLine();
    }

import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommandLineTest {

    private CommandLine.Builder builder;

    @BeforeEach
    void setUp() {
        builder = new CommandLine.Builder();
    }

    @Test
    void testAddArgWithMultipleCalls() {
        // Arrange
        String arg1 = "arg1";
        String arg2 = "arg2";
        String arg3 = "arg3";

        // Act
        builder.addArg(arg1).addArg(arg2).addArg(arg3);

        // Assert
        List<String> args = builder.getArgs();
        assertNotNull(args);
        assertEquals(3, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
        assertEquals("arg3", args.get(2));
    }
}
}