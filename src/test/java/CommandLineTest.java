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

import org.apache.commons.cli.CommandLine.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandLineBuilderTest {

    private Builder builder;

    @BeforeEach
    public void setUp() {
        builder = new Builder();
    }

    @Test
    public void testAddArgWithValidArgument() {
        // Arrange
        String validArg = "--file=example.txt";

        // Act
        Builder result = builder.addArg(validArg);

        // Assert
        assertNotNull(result, "The returned Builder instance should not be null.");
        assertTrue(builder.args.contains(validArg), "The argument should be added to the args list.");
    }
}
}