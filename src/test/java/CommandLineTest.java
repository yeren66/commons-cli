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
    void testAddOption_withNullOption() {
        // Arrange
        CommandLine.Builder builder = new CommandLine.Builder();
        int initialSize = builder.build().getOptions().size();

        // Act
        CommandLine.Builder result = builder.addOption(null);

        // Assert
        assertNotNull(result, "Builder instance should not be null after adding a null option");
        assertEquals(initialSize, builder.build().getOptions().size(), "Options list size should remain unchanged");
    }

}
}