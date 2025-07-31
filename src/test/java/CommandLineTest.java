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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandLineTest {

    @Test
    void testBuilderCreation_MultipleInstances() {
        // Arrange & Act
        CommandLine.Builder builder1 = new CommandLine.Builder();
        CommandLine.Builder builder2 = new CommandLine.Builder();

        // Assert
        assertNotNull(builder1, "First Builder instance should not be null");
        assertNotNull(builder2, "Second Builder instance should not be null");
        assertNotSame(builder1, builder2, "Builder instances should be unique");
    }
}
}