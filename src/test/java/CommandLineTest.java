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

public class CommandLineTest {

    private CommandLine.Builder commandLineBuilder;

    @BeforeEach
    public void setUp() {
        commandLineBuilder = new CommandLine.Builder();
    }

    @Test
    public void testAddOptionWithValidOption() {
        // Arrange
        Option option = Option.builder("a")
                .longOpt("alpha")
                .hasArg()
                .desc("Test option")
                .build();

        // Act
        CommandLine.Builder result = commandLineBuilder.addOption(option);

        // Assert
        assertNotNull(result, "Builder instance should not be null.");
        assertTrue(commandLineBuilder.build().getOptions().contains(option), "Option should be added to the Builder.");
    }
}
}