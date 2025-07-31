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
    void testAddOptionWithValidOption() {
        // Arrange
        Option option = Option.builder("a")
            .longOpt("alpha")
            .desc("Option alpha")
            .hasArg()
            .build();

        // Act
        CommandLine.Builder result = builder.addOption(option);

        // Assert
        assertNotNull(result, "Builder instance should not be null");
        assertTrue(builder.build().getOptions().contains(option), "The option should be added to the CommandLine options");
    }
}
}