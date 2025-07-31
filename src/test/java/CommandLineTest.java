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
    void testAddArg_withValidString() {
        // Arrange
        String validArg = "test-argument";

        // Act
        builder.addArg(validArg);

        // Assert
        List<String> args = builder.args;
        assertNotNull(args, "Args list should not be null");
        assertEquals(1, args.size(), "Args list should have one element");
        assertEquals(validArg, args.get(0), "The added argument should match the input");
    }
}
}