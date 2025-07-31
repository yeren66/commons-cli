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

import org.apache.commons.cli.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineTest {

    @Test
    void test_builder_with_null_options() {
        // Arrange
        CommandLine.Builder builder = new CommandLine.Builder();

        // Act & Assert
        assertDoesNotThrow(() -> builder.build(), "Builder should handle null options without throwing an exception");
    }
}
}