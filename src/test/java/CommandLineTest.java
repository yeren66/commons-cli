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

class CommandLineBuilderTest {

    @Test
    void testAddOption_withDuplicateOption() {
        // Arrange
        CommandLine.Builder builder = new CommandLine.Builder();
        Option option = new Option("a", "alpha", false, "Description for alpha");
        builder.addOption(option);

        // Act
        CommandLine.Builder result = builder.addOption(option);

        // Assert
        assertNotNull(result, "Builder instance should not be null after adding a duplicate option");
        assertEquals(1, builder.build().getOptions().size(), "Options list should not contain duplicate entries");
    }

}
}