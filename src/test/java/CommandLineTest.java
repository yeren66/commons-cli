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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandLineTest {

    private CommandLine.Builder builder;

    @BeforeEach
    void setUp() {
        builder = new CommandLine.Builder();
    }

    @Test
    void testAddOptionWithNullOption() {
        // Arrange
        int initialOptionCount = builder.build().getOptions().size();

        // Act
        CommandLine.Builder result = builder.addOption(null);

        // Assert
        assertNotNull(result, "Builder instance should not be null");
        int finalOptionCount = builder.build().getOptions().size();
        assertEquals(initialOptionCount, finalOptionCount, "Adding a null option should not change the options list");
    }
}
}