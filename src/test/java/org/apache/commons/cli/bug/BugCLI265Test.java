/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.cli.bug;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for CLI-265.
 * <p>
 * The issue is that a short option with an optional value will use whatever comes next as value.
 */
class BugCLI265Test {

    private DefaultParser parser;
    private Options options;

    @BeforeEach
    public void setUp() {
        parser = new DefaultParser();

        final Option optionT1 = Option.builder("t1").hasArg().numberOfArgs(1).optionalArg(true).argName("t1_path").get();
        final Option optionA = Option.builder("a").hasArg(false).get();
        final Option optionB = Option.builder("b").hasArg(false).get();
        final Option optionLast = Option.builder("last").hasArg(false).get();

        options = new Options().addOption(optionT1).addOption(optionA).addOption(optionB).addOption(optionLast);
    }

    @Test
    void testShouldParseConcatenatedShortOptions() throws Exception {
        final String[] concatenatedShortOptions = {"-t1", "-ab"};

        final CommandLine commandLine = parser.parse(options, concatenatedShortOptions);

        assertTrue(commandLine.hasOption("t1"));
        assertNull(commandLine.getOptionValue("t1"));
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption("last"));
    }

    @Test
    void testShouldParseShortOptionWithoutValue() throws Exception {
        final String[] twoShortOptions = {"-t1", "-last"};

        final CommandLine commandLine = parser.parse(options, twoShortOptions);

        assertTrue(commandLine.hasOption("t1"));
        assertNotEquals(commandLine.getOptionValue("t1"), "Second option has been used as value for first option", "-last");
        assertTrue(commandLine.hasOption("last"), "Second option has not been detected");
    }

    @Test
    void testShouldParseShortOptionWithValue() throws Exception {
        final String[] shortOptionWithValue = {"-t1", "path/to/my/db"};

        final CommandLine commandLine = parser.parse(options, shortOptionWithValue);

        assertEquals("path/to/my/db", commandLine.getOptionValue("t1"));
        assertFalse(commandLine.hasOption("last"));
    }
}
