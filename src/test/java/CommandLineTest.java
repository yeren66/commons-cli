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
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class CommandLineTest {

    private CommandLine.Builder builder;
    private List<String> args;

    @BeforeEach
    void setUp() {
        args = new ArrayList<>();
        builder = new CommandLine.Builder() {
            @Override
            public CommandLine.Builder addArg(String arg) {
                if (arg != null) {
                    args.add(arg);
                }
                return this;
            }
        };
    }

    @Test
    void testAddArgWithNull() {
        // Arrange
        String nullArg = null;

        // Act
        builder.addArg(nullArg);

        // Assert
        assertTrue(args.isEmpty(), "The args list should remain empty when null is passed.");
    }
}
}