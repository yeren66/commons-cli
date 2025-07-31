package org.apache.commons.cli;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOption_Normal() {
        // Arrange
        Option option = Option.builder("h")
            .longOpt("help")
            .desc("Show help message")
            .build();

        // Act
        Options result = options.addOption(option);

        // Assert
        assertNotNull(result);
        assertTrue(options.hasOption("h"));
        assertEquals("Show help message", options.getOption("h").getDescription());
    }
}