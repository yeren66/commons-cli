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
    public void testAddOption_withDuplicateOptions() {
        // Arrange
        Option option = new Option("a", "alpha", false, "Alpha option");

        // Act
        builder.addOption(option);
        builder.addOption(option);
        CommandLine commandLine = builder.build();

        // Assert
        assertEquals(1, commandLine.getOptions().stream().filter(o -> o.equals(option)).count());
    }
}
}