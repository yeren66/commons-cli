package org.apache.commons.cli;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class DefaultParserTest {

    private DefaultParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
        options.addOption("h", "help", false, "Show help");
        options.addOption("f", "file", true, "Input file");
    }

    @Test
    public void testParse_Normal() throws ParseException {
        // Arrange
        String[] args = {"-h", "-f", "input.txt"};

        // Act
        CommandLine cmd = parser.parse(options, args);

        // Assert
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("h"));
        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }
}