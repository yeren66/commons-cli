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
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineTest {

    private CommandLine.Builder builder;

    @BeforeEach
    void setUp() {
        builder = new CommandLine.Builder();
    }

    @Test
    void addOption_validOption_addsOptionSuccessfully() {
        // Arrange
        Option option = Option.builder("o")
                              .longOpt("option")
                              .desc("Test option")
                              .hasArg()
                              .build();

        // Act
        builder.addOption(option);

        // Assert
        assertTrue(builder.getOptions().contains(option), "The option should be added to the options list.");
    }
}
}