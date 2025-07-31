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
    public void testAddOptionWithValidOption() {
        // Arrange
        Option option = new Option("a", "optionA", false, "Test option A");

        // Act
        CommandLine.Builder result = builder.addOption(option);

        // Assert
        assertNotNull("The returned Builder instance should not be null", result);
        assertTrue("The options list should contain the added option", result.build().getOptions().contains(option));
    }
}
}