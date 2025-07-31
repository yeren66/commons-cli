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
    public void test_addOption_with_duplicate_option() {
        // Arrange
        CommandLine.Builder builder = new CommandLine.Builder();
        Option option = Option.builder("a")
                              .longOpt("alpha")
                              .desc("Test option")
                              .required(false)
                              .build();
        builder.addOption(option);

        // Act
        builder.addOption(option);

        // Assert
        assertEquals(1, builder.getOptions().size(), "Duplicate options should not be added");
    }
}
}