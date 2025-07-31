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
    void test_builder_functionality_with_default_options() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption("a", "alpha", false, "Alpha option");
        CommandLine.Builder builder = new CommandLine.Builder();

        // Act
        CommandLine cmd = builder.build();

        // Assert
        assertNotNull(cmd, "CommandLine object should not be null");
        assertFalse(cmd.hasOption("a"), "CommandLine should not have 'alpha' option by default");
    }
}
}