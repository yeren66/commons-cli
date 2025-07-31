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
    void testAddDuplicateOption() {
        // Arrange
        Option option = Option.builder("b")
            .longOpt("beta")
            .desc("Option beta")
            .hasArg()
            .build();

        // Act
        builder.addOption(option);
        builder.addOption(option);

        // Assert
        CommandLine commandLine = builder.build();
        assertEquals(2, commandLine.getOptions().stream().filter(opt -> opt.equals(option)).count(), "The duplicate option should be added to the options list");
    }
}
}