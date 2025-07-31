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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommandLineBuilderTest {

    @Test
    public void testBuilderInstanceMethodsWorkCorrectly() {
        // Arrange
        CommandLine.Builder builder = new CommandLine.Builder();

        // Act
        builder.addOption("option1", "value1");
        CommandLine commandLine = builder.build();

        // Assert
        assertNotNull(commandLine, "The builder should produce a valid CommandLine instance.");
        assertTrue(commandLine.hasOption("option1"), "The CommandLine instance should contain the added option.");
        assertEquals("value1", commandLine.getOptionValue("option1"), "The option value should match the provided value.");
    }
}
}