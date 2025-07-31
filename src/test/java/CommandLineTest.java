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
    public void testAddOption_withNullOption() {
        // Arrange
        Option option = null;

        // Act
        CommandLine.Builder result = builder.addOption(option);

        // Assert
        assertNotNull(result);
        assertTrue(result.build().getOptions().isEmpty());
    }
}
}