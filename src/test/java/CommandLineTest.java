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
    void testAddArg_withMultipleCalls() {
        // Arrange
        String firstArg = "arg1";
        String secondArg = "arg2";

        // Act
        builder.addArg(firstArg);
        builder.addArg(secondArg);

        // Assert
        List<String> args = builder.args;
        assertNotNull(args, "Args list should not be null");
        assertEquals(2, args.size(), "Args list should contain two elements");
        assertEquals(firstArg, args.get(0), "The first argument should match the first input");
        assertEquals(secondArg, args.get(1), "The second argument should match the second input");
    }
}
}