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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommandLineTest {

    @Test
    public void test_addOption_with_empty_option() {
        // Arrange
        CommandLine.Builder builder = new CommandLine.Builder();
        Option option = new Option("", "");

        // Act
        builder.addOption(option);

        // Assert
        assertTrue(builder.getOptions().contains(option), "Empty option should still be added to the builder");
    }
}
}