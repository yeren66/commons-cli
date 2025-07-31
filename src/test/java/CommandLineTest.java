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

public class CommandLineBuilderTest {

    private CommandLine.Builder builder;

    @BeforeEach
    public void setUp() {
        builder = new CommandLine.Builder();
    }

    @Test
    public void testAddArgWithMultipleInvocations() {
        // Arrange
        String arg1 = "arg1";
        String arg2 = "arg2";
        String arg3 = "arg3";

        // Act
        builder.addArg(arg1).addArg(arg2).addArg(arg3);

        // Assert
        List<String> args = builder.getArgs();
        assertEquals(3, args.size(), "The args list should contain all added arguments.");
        assertEquals(arg1, args.get(0), "The first argument should match.");
        assertEquals(arg2, args.get(1), "The second argument should match.");
        assertEquals(arg3, args.get(2), "The third argument should match.");
    }
}
}