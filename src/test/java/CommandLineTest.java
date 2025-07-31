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
    public void testAddOptionWithDuplicateOptions() {
        // Arrange
        Option option = new Option("a", "alpha", false, "Alpha option");

        // Act
        builder.addOption(option);
        builder.addOption(option);

        // Assert
        assertEquals(1, builder.getOptions().size()); // Assuming getOptions() returns a collection.
        assertTrue(builder.getOptions().contains(option));
    }
}
}