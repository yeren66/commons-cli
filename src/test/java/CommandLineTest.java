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
    public void addArg_chainedAdditionOfArguments_allArgumentsAreAdded() {
        // Arrange
        String arg1 = "--help";
        String arg2 = "--version";

        // Act
        builder.addArg(arg1).addArg(arg2);

        // Assert
        assertTrue("The first argument should be present in the args list.", builder.args.contains(arg1));
        assertTrue("The second argument should be present in the args list.", builder.args.contains(arg2));
    }
}
}