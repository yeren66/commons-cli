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
    public void testAddArgWithSpecialCharacters() {
        // Arrange
        String specialArg = "--key=value&param=123";

        // Act
        Builder result = builder.addArg(specialArg);

        // Assert
        assertNotNull(result, "The returned Builder instance should not be null.");
        assertTrue(builder.args.contains(specialArg), "The argument with special characters should be added to the args list.");
    }
}
}