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
    public void setup() {
        builder = new CommandLine.Builder();
    }

    @Test
    public void addArg_addEmptyStringArgument_successfullyAddsToArgsList() {
        // Arrange
        String emptyArgument = "";

        // Act
        builder.addArg(emptyArgument);

        // Assert
        assertTrue("The empty string argument should be added to the args list.", builder.args.contains(emptyArgument));
    }
}
}