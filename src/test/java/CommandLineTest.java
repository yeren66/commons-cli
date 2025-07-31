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
    public void testAddOptionWithDuplicateOption() {
        // Arrange
        Option option1 = new Option("a", "optionA", false, "Test option A");
        Option option2 = new Option("a", "optionA", false, "Test option A Duplicate");
        builder.addOption(option1);

        // Act
        CommandLine.Builder result = builder.addOption(option2);

        // Assert
        assertNotNull("The returned Builder instance should not be null", result);
        assertEquals("The options list should only contain one instance of the option", 1, result.build().getOptions().stream().filter(opt -> opt.getOpt().equals("a")).count());
    }
}
}