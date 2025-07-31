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

import static org.junit.jupiter.api.Assertions.*;

public class CommandLineTest {

    private CommandLine.Builder commandLineBuilder;

    @BeforeEach
    public void setUp() {
        commandLineBuilder = new CommandLine.Builder();
    }

    @Test
    public void testAddOptionWithNullOption() {
        // Arrange
        int initialSize = commandLineBuilder.build().getOptions().size();

        // Act
        CommandLine.Builder result = commandLineBuilder.addOption(null);

        // Assert
        assertNotNull(result, "Builder instance should not be null.");
        assertEquals(initialSize, commandLineBuilder.build().getOptions().size(), "Options size should remain unchanged.");
    }
}
}