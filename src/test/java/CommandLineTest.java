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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine.Builder builder;

    @Before
    public void setUp() {
        builder = new CommandLine.Builder();
    }

    @Test
    public void testAddOption_withMultipleOptions() {
        // Arrange
        Option option1 = new Option("a", "alpha", false, "Alpha option");
        Option option2 = new Option("b", "beta", true, "Beta option");

        // Act
        builder.addOption(option1);
        builder.addOption(option2);
        CommandLine commandLine = builder.build();

        // Assert
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }
}
}