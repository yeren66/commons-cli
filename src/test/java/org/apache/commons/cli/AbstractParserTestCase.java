/*
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Abstract test case testing common parser features.
 *
 * TODO Needs a rework using JUnit parameterized tests.
 */
public abstract class AbstractParserTestCase {

    protected CommandLineParser parser;

    protected Options options;

    @SuppressWarnings("deprecation")
    private CommandLine parse(final CommandLineParser parser, final Options options, final String[] args, final Properties properties) throws ParseException {
        if (parser instanceof Parser) {
            return ((Parser) parser).parse(options, args, properties);
        }
        if (parser instanceof DefaultParser) {
            return ((DefaultParser) parser).parse(options, args, properties);
        }
        throw new UnsupportedOperationException("Default options not supported by this parser");
    }

    @BeforeEach
    public void setUp() {
        //@formatter:off
        options = new Options()
            .addOption("a", "enable-a", false, "turn [a] on or off")
            .addOption("b", "bfile", true, "set the value of [b]")
            .addOption("c", "copt", false, "turn [c] on or off");
        //@formatter:on
    }

    @Test
    void testAmbiguousArgParsing() throws Exception {
        final String[] args = { "-=-" };
        final Options options = new Options();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    void testAmbiguousLongWithoutEqualSingleDash() throws Exception {
        final String[] args = { "-b", "-foobar" };
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").hasOptionalArg().create('f'));
        options.addOption(OptionBuilder.withLongOpt("bar").hasOptionalArg().create('b'));
        final CommandLine cl = parser.parse(options, args);
        assertTrue(cl.hasOption("b"));
        assertTrue(cl.hasOption("f"));
        assertEquals("bar", cl.getOptionValue("foo"));
    }

    @Test
    void testAmbiguousLongWithoutEqualSingleDash2() throws Exception {
        final String[] args = { "-b", "-foobar" };
        final Options options = new Options();
        options.addOption(Option.builder().longOpt("foo").option("f").optionalArg(true).get());
        options.addOption(Option.builder().longOpt("bar").option("b").optionalArg(false).get());
        final CommandLine cl = parser.parse(options, args);
        assertTrue(cl.hasOption("b"));
        assertTrue(cl.hasOption("f"));
        assertEquals("bar", cl.getOptionValue("foo"));
    }

    @Test
    void testAmbiguousPartialLongOption1() throws Exception {
        final String[] args = { "--ver" };
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("version").create());
        options.addOption(OptionBuilder.withLongOpt("verbose").create());
        final AmbiguousOptionException e = assertThrows(AmbiguousOptionException.class, () -> parser.parse(options, args));
        assertEquals("--ver", e.getOption(), "Partial option");
        assertNotNull(e.getMatchingOptions(), "Matching options null");
        assertEquals(2, e.getMatchingOptions().size(), "Matching options size");
    }

    @Test
    void testAmbiguousPartialLongOption2() throws Exception {
        final String[] args = { "-ver" };
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("version").create());
        options.addOption(OptionBuilder.withLongOpt("verbose").create());
        final AmbiguousOptionException e = assertThrows(AmbiguousOptionException.class, () -> parser.parse(options, args));
        assertEquals("-ver", e.getOption(), "Partial option");
        assertNotNull(e.getMatchingOptions(), "Matching options null");
        assertEquals(2, e.getMatchingOptions().size(), "Matching options size");
    }

    @Test
    void testAmbiguousPartialLongOption3() throws Exception {
        final String[] args = { "--ver=1" };
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("version").create());
        options.addOption(OptionBuilder.withLongOpt("verbose").hasOptionalArg().create());
        final AmbiguousOptionException e = assertThrows(AmbiguousOptionException.class, () -> parser.parse(options, args));
        assertEquals("--ver", e.getOption(), "Partial option");
        assertNotNull(e.getMatchingOptions(), "Matching options null");
        assertEquals(2, e.getMatchingOptions().size(), "Matching options size");
    }

    @Test
    void testAmbiguousPartialLongOption4() throws Exception {
        final String[] args = { "-ver=1" };
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("version").create());
        options.addOption(OptionBuilder.withLongOpt("verbose").hasOptionalArg().create());
        final AmbiguousOptionException e = assertThrows(AmbiguousOptionException.class, () -> parser.parse(options, args));
        assertEquals("-ver", e.getOption(), "Partial option");
        assertNotNull(e.getMatchingOptions(), "Matching options null");
        assertEquals(2, e.getMatchingOptions().size(), "Matching options size");
    }

    @Test
    void testArgumentStartingWithHyphen() throws Exception {
        final String[] args = { "-b", "-foo" };
        final CommandLine cl = parser.parse(options, args);
        assertEquals("-foo", cl.getOptionValue("b"));
    }

    @Test
    void testBursting() throws Exception {
        final String[] args = { "-acbtoast", "foo", "bar" };
        final CommandLine cl = parser.parse(options, args);
        assertTrue(cl.hasOption("a"), "Confirm -a is set");
        assertTrue(cl.hasOption("b"), "Confirm -b is set");
        assertTrue(cl.hasOption("c"), "Confirm -c is set");
        assertEquals("toast", cl.getOptionValue("b"), "Confirm arg of -b");
        assertEquals(2, cl.getArgList().size(), "Confirm size of extra args");
    }

    @Test
    void testDoubleDash1() throws Exception {
        final String[] args = { "--copt", "--", "-b", "toast" };
        final CommandLine cl = parser.parse(options, args);
        assertTrue(cl.hasOption("c"), "Confirm -c is set");
        assertFalse(cl.hasOption("b"), "Confirm -b is not set");
        assertEquals(2, cl.getArgList().size(), "Confirm 2 extra args: " + cl.getArgList().size());
    }

    @Test
    void testDoubleDash2() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.hasArg().create('n'));
        options.addOption(OptionBuilder.create('m'));
        final MissingArgumentException e = assertThrows(MissingArgumentException.class, () -> parser.parse(options, new String[] { "-n", "--", "-m" }));
        assertNotNull(e.getOption(), "option null");
        assertEquals("n", e.getOption().getOpt());
    }

    @Test
    void testLongOptionQuoteHandling() throws Exception {
        final String[] args = { "--bfile", "\"quoted string\"" };
        final CommandLine cl = parser.parse(options, args);
        assertEquals("quoted string", cl.getOptionValue("b"), "Confirm --bfile \"arg\" strips quotes");
    }

    @Test
    void testLongOptionWithEqualsQuoteHandling() throws Exception {
        final String[] args = { "--bfile=\"quoted string\"" };
        final CommandLine cl = parser.parse(options, args);
        assertEquals("quoted string", cl.getOptionValue("b"), "Confirm --bfile=\"arg\" strips quotes");
    }

    @Test
    void testLongWithEqualDoubleDash() throws Exception {
        final String[] args = { "--foo=bar" };
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").hasArg().create('f'));
        final CommandLine cl = parser.parse(options, args);
        assertEquals("bar", cl.getOptionValue("foo"));
    }

    @Test
    void testLongWithEqualSingleDash() throws Exception {
        final String[] args = { "-foo=bar" };
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").hasArg().create('f'));
        final CommandLine cl = parser.parse(options, args);
        assertEquals("bar", cl.getOptionValue("foo"));
    }

    @Test
    void testLongWithoutEqualDoubleDash() throws Exception {
        final String[] args = { "--foobar" };
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").hasArg().create('f'));
        final CommandLine cl = parser.parse(options, args, true);
        assertFalse(cl.hasOption("foo")); // foo isn't expected to be recognized with a double dash
    }

    @Test
    void testLongWithoutEqualSingleDash() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").hasArg().create('f'));
        final CommandLine cl = parser.parse(options, new String[] { "-foobar" });
        assertEquals("bar", cl.getOptionValue("foo"));
    }

    @Test
    void testLongWithUnexpectedArgument1() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create('f'));
        final UnrecognizedOptionException e = assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[] { "--foo=bar" }));
        assertEquals("--foo=bar", e.getOption());
    }

    @Test
    void testLongWithUnexpectedArgument2() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create('f'));
        final UnrecognizedOptionException e = assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[] { "-foobar" }));
        assertEquals("-foobar", e.getOption());
    }

    @Test
    void testMissingArg() throws Exception {
        final MissingArgumentException e = assertThrows(MissingArgumentException.class, () -> parser.parse(options, new String[] { "-b" }));
        assertEquals("b", e.getOption().getOpt(), "option missing an argument");
    }

    @Test
    void testMissingArgWithBursting() throws Exception {
        final String[] args = { "-acb" };
        final MissingArgumentException e = assertThrows(MissingArgumentException.class, () -> parser.parse(options, args));
        assertEquals("b", e.getOption().getOpt(), "option missing an argument");
    }

    @Test
    void testMissingRequiredGroup() throws Exception {
        final OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(OptionBuilder.create("a"));
        optionGroup.addOption(OptionBuilder.create("b"));
        optionGroup.setRequired(true);
        final Options options = new Options();
        options.addOptionGroup(optionGroup);
        options.addOption(OptionBuilder.isRequired().create("c"));
        final MissingOptionException e = assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[] { "-c" }));
        assertEquals(1, e.getMissingOptions().size());
        assertTrue(e.getMissingOptions().get(0) instanceof OptionGroup);
    }

    @Test
    void testMissingRequiredOption() {
        final String[] args = { "-a" };
        final Options options = new Options();
        options.addOption("a", "enable-a", false, null);
        options.addOption(OptionBuilder.withLongOpt("bfile").hasArg().isRequired().create('b'));
        final MissingOptionException e = assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
        assertEquals("Missing required option: b", e.getMessage(), "Incorrect exception message");
        assertTrue(e.getMissingOptions().contains("b"));
    }

    @Test
    void testMissingRequiredOptions() {
        final String[] args = { "-a" };
        final Options options = new Options();
        options.addOption("a", "enable-a", false, null);
        options.addOption(OptionBuilder.withLongOpt("bfile").hasArg().isRequired().create('b'));
        options.addOption(OptionBuilder.withLongOpt("cfile").hasArg().isRequired().create('c'));
        final MissingOptionException e = assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
        assertEquals("Missing required options: b, c", e.getMessage(), "Incorrect exception message");
        assertTrue(e.getMissingOptions().contains("b"));
        assertTrue(e.getMissingOptions().contains("c"));
    }

    @Test
    void testMultiple() throws Exception {
        final String[] args = { "-c", "foobar", "-b", "toast" };
        CommandLine cl = parser.parse(options, args, true);
        assertTrue(cl.hasOption("c"), "Confirm -c is set");
        assertEquals(3, cl.getArgList().size(), "Confirm  3 extra args: " + cl.getArgList().size());
        cl = parser.parse(options, cl.getArgs());
        assertFalse(cl.hasOption("c"), "Confirm -c is not set");
        assertTrue(cl.hasOption("b"), "Confirm -b is set");
        assertEquals("toast", cl.getOptionValue("b"), "Confirm arg of -b");
        assertEquals(1, cl.getArgList().size(), "Confirm  1 extra arg: " + cl.getArgList().size());
        assertEquals("foobar", cl.getArgList().get(0), "Confirm  value of extra arg: " + cl.getArgList().get(0));
    }

    @Test
    void testMultipleWithLong() throws Exception {
        final String[] args = { "--copt", "foobar", "--bfile", "toast" };
        CommandLine cl = parser.parse(options, args, true);
        assertTrue(cl.hasOption("c"), "Confirm -c is set");
        assertEquals(3, cl.getArgList().size(), "Confirm  3 extra args: " + cl.getArgList().size());
        cl = parser.parse(options, cl.getArgs());
        assertFalse(cl.hasOption("c"), "Confirm -c is not set");
        assertTrue(cl.hasOption("b"), "Confirm -b is set");
        assertEquals("toast", cl.getOptionValue("b"), "Confirm arg of -b");
        assertEquals(1, cl.getArgList().size(), "Confirm  1 extra arg: " + cl.getArgList().size());
        assertEquals("foobar", cl.getArgList().get(0), "Confirm  value of extra arg: " + cl.getArgList().get(0));
    }

    @Test
    void testMultipleWithNull() throws Exception {
        final String[] args = { null, "-c", null, "foobar", null, "-b", null, "toast", null };
        CommandLine cl = parser.parse(options, args, true);
        assertTrue(cl.hasOption("c"), "Confirm -c is set");
        assertEquals(3, cl.getArgList().size(), "Confirm  3 extra args: " + cl.getArgList().size());
        cl = parser.parse(options, cl.getArgs());
        assertFalse(cl.hasOption("c"), "Confirm -c is not set");
        assertTrue(cl.hasOption("b"), "Confirm -b is set");
        assertEquals("toast", cl.getOptionValue("b"), "Confirm arg of -b");
        assertEquals(1, cl.getArgList().size(), "Confirm  1 extra arg: " + cl.getArgList().size());
        assertEquals("foobar", cl.getArgList().get(0), "Confirm  value of extra arg: " + cl.getArgList().get(0));
    }

    @Test
    void testNegativeArgument() throws Exception {
        final String[] args = { "-b", "-1" };
        final CommandLine cl = parser.parse(options, args);
        assertEquals("-1", cl.getOptionValue("b"));
    }

    @Test
    void testNegativeOption() throws Exception {
        final String[] args = { "-b", "-1" };
        options.addOption("1", false, null);
        final CommandLine cl = parser.parse(options, args);
        assertEquals("-1", cl.getOptionValue("b"));
    }

    @Test
    void testOptionalArgsOptionBuilder() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.hasOptionalArgs(2).create('i'));
        final Properties properties = new Properties();

        CommandLine cmd = parse(parser, options, new String[] { "-i" }, properties);
        assertTrue(cmd.hasOption("i"));
        assertNull(cmd.getOptionValues("i"));

        cmd = parse(parser, options, new String[] { "-i", "paper" }, properties);
        assertTrue(cmd.hasOption("i"));
        assertArrayEquals(new String[] { "paper" }, cmd.getOptionValues("i"));

        cmd = parse(parser, options, new String[] { "-i", "paper", "scissors" }, properties);
        assertTrue(cmd.hasOption("i"));
        assertArrayEquals(new String[] { "paper", "scissors" }, cmd.getOptionValues("i"));

        cmd = parse(parser, options, new String[] { "-i", "paper", "scissors", "rock" }, properties);
        assertTrue(cmd.hasOption("i"));
        assertArrayEquals(new String[] { "paper", "scissors" }, cmd.getOptionValues("i"));
        assertArrayEquals(new String[] { "rock" }, cmd.getArgs());
    }

    @Test
    void testOptionalArgsOptionDotBuilder() throws Exception {
        final Options options = new Options();
        options.addOption(Option.builder("i").numberOfArgs(2).optionalArg(true).get());
        final Properties properties = new Properties();

        CommandLine cmd = parse(parser, options, new String[] { "-i" }, properties);
        assertTrue(cmd.hasOption("i"));
        assertNull(cmd.getOptionValues("i"));

        cmd = parse(parser, options, new String[] { "-i", "paper" }, properties);
        assertTrue(cmd.hasOption("i"));
        assertArrayEquals(new String[] { "paper" }, cmd.getOptionValues("i"));

        cmd = parse(parser, options, new String[] { "-i", "paper", "scissors" }, properties);
        assertTrue(cmd.hasOption("i"));
        assertArrayEquals(new String[] { "paper", "scissors" }, cmd.getOptionValues("i"));

        cmd = parse(parser, options, new String[] { "-i", "paper", "scissors", "rock" }, properties);
        assertTrue(cmd.hasOption("i"));
        assertArrayEquals(new String[] { "paper", "scissors" }, cmd.getOptionValues("i"));
        assertArrayEquals(new String[] { "rock" }, cmd.getArgs());

        options.addOption(Option.builder("j").numberOfArgs(3).optionalArg(true).get());
        cmd = parse(parser, options, new String[] { "-j" }, properties);
    }

    @Test
    void testOptionAndRequiredOption() throws Exception {
        final String[] args = { "-a", "-b", "file" };

        final Options options = new Options();
        options.addOption("a", "enable-a", false, null);
        options.addOption(OptionBuilder.withLongOpt("bfile").hasArg().isRequired().create('b'));

        final CommandLine cl = parser.parse(options, args);

        assertTrue(cl.hasOption("a"), "Confirm -a is set");
        assertTrue(cl.hasOption("b"), "Confirm -b is set");
        assertEquals("file", cl.getOptionValue("b"), "Confirm arg of -b");
        assertTrue(cl.getArgList().isEmpty(), "Confirm NO of extra args");
    }

    @Test
    void testOptionGroup() throws Exception {
        final OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isSelected());
        optionGroup.addOption(OptionBuilder.create("a"));
        optionGroup.addOption(OptionBuilder.create("b"));

        final Options options = new Options();
        options.addOptionGroup(optionGroup);

        parser.parse(options, new String[] { "-b" });

        assertTrue(optionGroup.isSelected());
        assertEquals("b", optionGroup.getSelected(), "selected option");
    }

    @Test
    void testOptionGroupLong() throws Exception {
        final OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(OptionBuilder.withLongOpt("foo").create());
        optionGroup.addOption(OptionBuilder.withLongOpt("bar").create());

        final Options options = new Options();
        options.addOptionGroup(optionGroup);

        final CommandLine cl = parser.parse(options, new String[] { "--bar" });

        assertTrue(cl.hasOption("bar"));
        assertTrue(optionGroup.isSelected());
        assertEquals("bar", optionGroup.getSelected(), "selected option");
    }

    @Test
    void testPartialLongOptionSingleDash() throws Exception {
        final String[] args = { "-ver" };

        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("version").create());
        options.addOption(OptionBuilder.hasArg().create('v'));

        final CommandLine cl = parser.parse(options, args);

        assertTrue(cl.hasOption("version"), "Confirm --version is set");
        assertFalse(cl.hasOption("v"), "Confirm -v is not set");
    }

    @Test
    void testPropertiesOption1() throws Exception {
        final String[] args = { "-Jsource=1.5", "-J", "target", "1.5", "foo" };

        final Options options = new Options();
        options.addOption(OptionBuilder.withValueSeparator().hasArgs(2).create('J'));

        final CommandLine cl = parser.parse(options, args);

        final List<String> values = Arrays.asList(cl.getOptionValues("J"));
        assertNotNull(values, "null values");
        assertEquals(4, values.size(), "number of values");
        assertEquals("source", values.get(0), "value 1");
        assertEquals("1.5", values.get(1), "value 2");
        assertEquals("target", values.get(2), "value 3");
        assertEquals("1.5", values.get(3), "value 4");

        final List<?> argsleft = cl.getArgList();
        assertEquals(1, argsleft.size(), "Should be 1 arg left");
        assertEquals("foo", argsleft.get(0), "Expecting foo");
    }

    @Test
    void testPropertiesOption2() throws Exception {
        final String[] args = { "-Dparam1", "-Dparam2=value2", "-D" };

        final Options options = new Options();
        options.addOption(OptionBuilder.withValueSeparator().hasOptionalArgs(2).create('D'));

        final CommandLine cl = parser.parse(options, args);

        final Properties props = cl.getOptionProperties("D");
        assertNotNull(props, "null properties");
        assertEquals(2, props.size(), "number of properties in " + props);
        assertEquals("true", props.getProperty("param1"), "property 1");
        assertEquals("value2", props.getProperty("param2"), "property 2");

        final List<?> argsleft = cl.getArgList();
        assertEquals(0, argsleft.size(), "Should be no arg left");
    }

    @Test
    void testPropertyOptionFlags() throws Exception {
        final Options options = new Options();
        options.addOption("a", false, "toggle -a");
        options.addOption("c", "c", false, "toggle -c");
        options.addOption(OptionBuilder.hasOptionalArg().create('e'));

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("c", "yes");
        properties.setProperty("e", "1");

        CommandLine cmd = parse(parser, options, null, properties);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("e"));

        properties = new Properties();
        properties.setProperty("a", "false");
        properties.setProperty("c", "no");
        properties.setProperty("e", "0");

        cmd = parse(parser, options, null, properties);
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("e")); // this option accepts an argument

        properties = new Properties();
        properties.setProperty("a", "TRUE");
        properties.setProperty("c", "nO");
        properties.setProperty("e", "TrUe");

        cmd = parse(parser, options, null, properties);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("e"));

        properties = new Properties();
        properties.setProperty("a", "just a string");
        properties.setProperty("e", "");

        cmd = parse(parser, options, null, properties);
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("e"));

        properties = new Properties();
        properties.setProperty("a", "0");
        properties.setProperty("c", "1");

        cmd = parse(parser, options, null, properties);
        assertFalse(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("c"));
    }

    @Test
    void testPropertyOptionGroup() throws Exception {
        final Options options = new Options();

        final OptionGroup optionGroup1 = new OptionGroup();
        optionGroup1.addOption(new Option("a", null));
        optionGroup1.addOption(new Option("b", null));
        options.addOptionGroup(optionGroup1);

        final OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.addOption(new Option("x", null));
        optionGroup2.addOption(new Option("y", null));
        options.addOptionGroup(optionGroup2);

        final String[] args = { "-a" };

        final Properties properties = new Properties();
        properties.put("b", "true");
        properties.put("x", "true");

        final CommandLine cmd = parse(parser, options, args, properties);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
    }

    @Test
    void testPropertyOptionMultipleValues() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.hasArgs().withValueSeparator(',').create('k'));

        final Properties properties = new Properties();
        properties.setProperty("k", "one,two");

        final CommandLine cmd = parse(parser, options, null, properties);
        assertTrue(cmd.hasOption("k"));
        final String[] values = { "one", "two" };
        assertArrayEquals(values, cmd.getOptionValues('k'));
    }

    @Test
    void testPropertyOptionRequired() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.isRequired().create("f"));

        final Properties properties = new Properties();
        properties.setProperty("f", "true");

        final CommandLine cmd = parse(parser, options, null, properties);
        assertTrue(cmd.hasOption("f"));
    }

    @Test
    void testPropertyOptionSingularValue() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.hasOptionalArgs(2).withLongOpt("hide").create());

        final Properties properties = new Properties();
        properties.setProperty("hide", "seek");

        final CommandLine cmd = parse(parser, options, null, properties);
        assertTrue(cmd.hasOption("hide"));
        assertEquals("seek", cmd.getOptionValue("hide"));
        assertFalse(cmd.hasOption("fake"));
    }

    @Test
    void testPropertyOptionUnexpected() throws Exception {
        final Options options = new Options();
        final Properties properties = new Properties();
        properties.setProperty("f", "true");
        assertThrows(UnrecognizedOptionException.class, () -> parse(parser, options, null, properties));
    }

    @Test
    void testPropertyOverrideValues() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.hasOptionalArgs(2).create('i'));
        options.addOption(OptionBuilder.hasOptionalArgs().create('j'));

        final String[] args = { "-j", "found", "-i", "ink" };

        final Properties properties = new Properties();
        properties.setProperty("j", "seek");

        final CommandLine cmd = parse(parser, options, args, properties);
        assertTrue(cmd.hasOption("j"));
        assertEquals("found", cmd.getOptionValue("j"));
        assertTrue(cmd.hasOption("i"));
        assertEquals("ink", cmd.getOptionValue("i"));
        assertFalse(cmd.hasOption("fake"));
    }

    @Test
    void testReuseOptionsTwice() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.isRequired().create('v'));
        // first parsing
        parser.parse(options, new String[] { "-v" });
        // second parsing, with the same Options instance and an invalid command line
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));
    }

    @Test
    void testShortOptionConcatenatedQuoteHandling() throws Exception {
        final CommandLine cl = parser.parse(options, new String[] { "-b\"quoted string\"" });
        assertEquals("quoted string", cl.getOptionValue("b"), "Confirm -b\"arg\" strips quotes");
    }

    @Test
    void testShortOptionQuoteHandling() throws Exception {
        final CommandLine cl = parser.parse(options, new String[] { "-b", "\"quoted string\"" });
        assertEquals("quoted string", cl.getOptionValue("b"), "Confirm -b \"arg\" strips quotes");
    }

    @Test
    void testShortWithEqual() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").hasArg().create('f'));
        final CommandLine cl = parser.parse(options, new String[] { "-f=bar" });
        assertEquals("bar", cl.getOptionValue("foo"));
    }

    @Test
    void testShortWithoutEqual() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").hasArg().create('f'));
        final CommandLine cl = parser.parse(options, new String[] { "-fbar" });
        assertEquals("bar", cl.getOptionValue("foo"));
    }

    @Test
    void testShortWithUnexpectedArgument() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("foo").create('f'));
        final UnrecognizedOptionException e = assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[] { "-f=bar" }));
        assertEquals("-f=bar", e.getOption());
    }

    @Test
    void testSimpleLong() throws Exception {
        final CommandLine cl = parser.parse(options, new String[] { "--enable-a", "--bfile", "toast", "foo", "bar" });
        assertTrue(cl.hasOption("a"), "Confirm -a is set");
        assertTrue(cl.hasOption("b"), "Confirm -b is set");
        assertEquals("toast", cl.getOptionValue("b"), "Confirm arg of -b");
        assertEquals("toast", cl.getOptionValue("bfile"), "Confirm arg of --bfile");
        assertEquals(2, cl.getArgList().size(), "Confirm size of extra args");
    }

    @Test
    void testSimpleShort() throws Exception {
        final CommandLine cl = parser.parse(options, new String[] { "-a", "-b", "toast", "foo", "bar" });
        assertTrue(cl.hasOption("a"), "Confirm -a is set");
        assertTrue(cl.hasOption("b"), "Confirm -b is set");
        assertEquals("toast", cl.getOptionValue("b"), "Confirm arg of -b");
        assertEquals(2, cl.getArgList().size(), "Confirm size of extra args");
    }

    @Test
    void testSingleDash() throws Exception {
        final CommandLine cl = parser.parse(options, new String[] { "--copt", "-b", "-", "-a", "-" });
        assertTrue(cl.hasOption("a"), "Confirm -a is set");
        assertTrue(cl.hasOption("b"), "Confirm -b is set");
        assertEquals("-", cl.getOptionValue("b"), "Confirm arg of -b");
        assertEquals(1, cl.getArgList().size(), "Confirm 1 extra arg: " + cl.getArgList().size());
        assertEquals("-", cl.getArgList().get(0), "Confirm value of extra arg: " + cl.getArgList().get(0));
    }

    @Test
    void testStopAtExpectedArg() throws Exception {
        final String[] args = { "-b", "foo" };
        final CommandLine cl = parser.parse(options, args, true);
        assertTrue(cl.hasOption('b'), "Confirm -b is set");
        assertEquals("foo", cl.getOptionValue('b'), "Confirm -b is set");
        assertTrue(cl.getArgList().isEmpty(), "Confirm no extra args: " + cl.getArgList().size());
    }

    @Test
    void testStopAtNonOptionLong() throws Exception {
        final String[] args = { "--zop==1", "-abtoast", "--b=bar" };
        final CommandLine cl = parser.parse(options, args, true);
        assertFalse(cl.hasOption("a"), "Confirm -a is not set");
        assertFalse(cl.hasOption("b"), "Confirm -b is not set");
        assertEquals(3, cl.getArgList().size(), "Confirm  3 extra args: " + cl.getArgList().size());
    }

    @Test
    void testStopAtNonOptionShort() throws Exception {
        final CommandLine cl = parser.parse(options, new String[] { "-z", "-a", "-btoast" }, true);
        assertFalse(cl.hasOption("a"), "Confirm -a is not set");
        assertEquals(3, cl.getArgList().size(), "Confirm  3 extra args: " + cl.getArgList().size());
    }

    @Test
    void testStopAtUnexpectedArg() throws Exception {
        final CommandLine cl = parser.parse(options, new String[] { "-c", "foober", "-b", "toast" }, true);
        assertTrue(cl.hasOption("c"), "Confirm -c is set");
        assertEquals(3, cl.getArgList().size(), "Confirm  3 extra args: " + cl.getArgList().size());
    }

    @Test
    void testStopBursting() throws Exception {
        final CommandLine cl = parser.parse(options, new String[] { "-azc" }, true);
        assertTrue(cl.hasOption("a"), "Confirm -a is set");
        assertFalse(cl.hasOption("c"), "Confirm -c is not set");
        assertEquals(1, cl.getArgList().size(), "Confirm  1 extra arg: " + cl.getArgList().size());
        assertTrue(cl.getArgList().contains("zc"));
    }

    @Test
    void testStopBursting2() throws Exception {
        CommandLine cl = parser.parse(options, new String[] { "-c", "foobar", "-btoast" }, true);
        assertTrue(cl.hasOption("c"), "Confirm -c is set");
        assertEquals(2, cl.getArgList().size(), "Confirm  2 extra args: " + cl.getArgList().size());
        cl = parser.parse(options, cl.getArgs());
        assertFalse(cl.hasOption("c"), "Confirm -c is not set");
        assertTrue(cl.hasOption("b"), "Confirm -b is set");
        assertEquals("toast", cl.getOptionValue("b"), "Confirm arg of -b");
        assertEquals(1, cl.getArgList().size(), "Confirm  1 extra arg: " + cl.getArgList().size());
        assertEquals("foobar", cl.getArgList().get(0), "Confirm  value of extra arg: " + cl.getArgList().get(0));
    }

    @Test
    void testUnambiguousPartialLongOption1() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("version").create());
        options.addOption(OptionBuilder.withLongOpt("help").create());
        final CommandLine cl = parser.parse(options, new String[] { "--ver" });
        assertTrue(cl.hasOption("version"), "Confirm --version is set");
    }

    @Test
    void testUnambiguousPartialLongOption2() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("version").create());
        options.addOption(OptionBuilder.withLongOpt("help").create());
        final CommandLine cl = parser.parse(options, new String[] { "-ver" });
        assertTrue(cl.hasOption("version"), "Confirm --version is set");
    }

    @Test
    void testUnambiguousPartialLongOption3() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("verbose").hasOptionalArg().create());
        options.addOption(OptionBuilder.withLongOpt("help").create());
        final CommandLine cl = parser.parse(options, new String[] { "--ver=1" });
        assertTrue(cl.hasOption("verbose"), "Confirm --verbose is set");
        assertEquals("1", cl.getOptionValue("verbose"));
    }

    @Test
    void testUnambiguousPartialLongOption4() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("verbose").hasOptionalArg().create());
        options.addOption(OptionBuilder.withLongOpt("help").create());
        final CommandLine cl = parser.parse(options, new String[] { "-ver=1" });
        assertTrue(cl.hasOption("verbose"), "Confirm --verbose is set");
        assertEquals("1", cl.getOptionValue("verbose"));
    }

    @Test
    void testUnlimitedArgs() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.hasArgs().create("e"));
        options.addOption(OptionBuilder.hasArgs().create("f"));
        final CommandLine cl = parser.parse(options, new String[] { "-e", "one", "two", "-f", "alpha" });
        assertTrue(cl.hasOption("e"), "Confirm -e is set");
        assertEquals(2, cl.getOptionValues("e").length, "number of arg for -e");
        assertTrue(cl.hasOption("f"), "Confirm -f is set");
        assertEquals(1, cl.getOptionValues("f").length, "number of arg for -f");
    }

    @Test
    void testUnrecognizedOption() throws Exception {
        final UnrecognizedOptionException e = assertThrows(UnrecognizedOptionException.class,
                () -> parser.parse(options, new String[] { "-a", "-d", "-b", "toast", "foo", "bar" }));
        assertEquals("-d", e.getOption());
    }

    @Test
    void testUnrecognizedOptionWithBursting() throws Exception {
        final UnrecognizedOptionException e = assertThrows(UnrecognizedOptionException.class,
                () -> parser.parse(options, new String[] { "-adbtoast", "foo", "bar" }));
        assertEquals("-adbtoast", e.getOption());
    }

    @Test
    void testWithRequiredOption() throws Exception {
        final String[] args = { "-b", "file" };
        final Options options = new Options();
        options.addOption("a", "enable-a", false, null);
        options.addOption(OptionBuilder.withLongOpt("bfile").hasArg().isRequired().create('b'));
        final CommandLine cl = parser.parse(options, args);
        assertFalse(cl.hasOption("a"), "Confirm -a is NOT set");
        assertTrue(cl.hasOption("b"), "Confirm -b is set");
        assertEquals("file", cl.getOptionValue("b"), "Confirm arg of -b");
        assertTrue(cl.getArgList().isEmpty(), "Confirm NO of extra args");
    }
}
