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

class CommandLineBuilderTest {

    @Test
    void test_builder_creates_empty_commandline_object() {
        // Arrange
        CommandLine.Builder builder = new CommandLine.Builder();

        // Act
        CommandLine commandLine = builder.build();

        // Assert
        assertNotNull(commandLine, "The CommandLine object should not be null.");
        assertTrue(commandLine.getOptions().length == 0, "The CommandLine object should have no options by default.");
    }
}

}