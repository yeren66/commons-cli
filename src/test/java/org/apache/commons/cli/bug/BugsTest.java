/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation") // tests some deprecated classes
class BugsTest {
    @Test
    void test11456() throws Exception {
        // POSIX
        Options options = new Options();
        options.addOption(OptionBuilder.hasOptionalArg().create('a'));
        options.addOption(OptionBuilder.hasArg().create('b'));
        String[] args = {"-a", "-bvalue"};

        CommandLineParser parser = new PosixParser();

        CommandLine cmd = parser.parse(options, args);
        assertEquals(cmd.getOptionValue('b'), "value");

        // GNU
        options = new Options();
        options.addOption(OptionBuilder.hasOptionalArg().create('a'));
        options.addOption(OptionBuilder.hasArg().create('b'));
        args = new String[] {"-a", "-b", "value"};

        parser = new GnuParser();

        cmd = parser.parse(options, args);
        assertEquals(cmd.getOptionValue('b'), "value");
    }

    @Test
    void test11457() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("verbose").create());
        final String[] args = {"--verbose"};

        final CommandLineParser parser = new PosixParser();

        final CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("verbose"));
    }

    @Test
    void test11458() throws Exception {
        final Options options = new Options();
        options.addOption(OptionBuilder.withValueSeparator('=').hasArgs().create('D'));
        options.addOption(OptionBuilder.withValueSeparator(':').hasArgs().create('p'));
        final String[] args = {"-DJAVA_HOME=/opt/java", "-pfile1:file2:file3"};

        final CommandLineParser parser = new PosixParser();

        final CommandLine cmd = parser.parse(options, args);

        String[] values = cmd.getOptionValues('D');

        assertEquals(values[0], "JAVA_HOME");
        assertEquals(values[1], "/opt/java");

        values = cmd.getOptionValues('p');

        assertEquals(values[0], "file1");
        assertEquals(values[1], "file2");
        assertEquals(values[2], "file3");

        final Iterator<Option> iter = cmd.iterator();
        while (iter.hasNext()) {
            final Option opt = iter.next();
            switch (opt.getId()) {
            case 'D':
                assertEquals(opt.getValue(0), "JAVA_HOME");
                assertEquals(opt.getValue(1), "/opt/java");
                break;
            case 'p':
                assertEquals(opt.getValue(0), "file1");
                assertEquals(opt.getValue(1), "file2");
                assertEquals(opt.getValue(2), "file3");
                break;
            default:
                fail("-D option not found");
            }
        }
    }

    @Test
    void test11680() throws Exception {
        final Options options = new Options();
        final Option optionF = options.addOption("f", true, "foobar").getOption("f");
        final Option optionM = options.addOption("m", true, "missing").getOption("m");
        final String[] args = { "-f", "foo" };
        final CommandLineParser parser = new PosixParser();
        final CommandLine cmd = parser.parse(options, args);
        // 1.7.0 API:
        cmd.getOptionValue(optionF, () -> "default f");
        cmd.getOptionValue(optionM, () -> "default m");
        // 1.7.0 API:
        cmd.getOptionValue('f', () -> "default f");
        cmd.getOptionValue('m', () -> "default m");
        // 1.5.0 API:
        cmd.getOptionValue(optionF, "default f");
        cmd.getOptionValue(optionM, "default m");
        // Original API:
        cmd.getOptionValue("f", "default f");
        cmd.getOptionValue("m", "default m");
        //
        assertNull(cmd.getOptionValue((String) null, (String) null));
        assertEquals("default", cmd.getOptionValue((String) null, "default"));
    }

    @Test
    void test12210() throws Exception {
        // create the main options object which will handle the first parameter
        final Options mainOptions = new Options();
        // There can be 2 main exclusive options: -exec|-rep

        // Therefore, place them in an option group

        String[] argv = {"-exec", "-exec_opt1", "-exec_opt2"};
        final OptionGroup optionGroup = new OptionGroup();

        optionGroup.addOption(new Option("exec", false, "description for this option"));

        optionGroup.addOption(new Option("rep", false, "description for this option"));

        mainOptions.addOptionGroup(optionGroup);

        // for the exec option, there are 2 options...
        final Options execOptions = new Options();
        execOptions.addOption("exec_opt1", false, " desc");
        execOptions.addOption("exec_opt2", false, " desc");

        // similarly, for rep there are 2 options...
        final Options repOptions = new Options();
        repOptions.addOption("repopto", false, "desc");
        repOptions.addOption("repoptt", false, "desc");

        // create the parser
        final GnuParser parser = new GnuParser();

        // finally, parse the arguments:

        // first parse the main options to see what the user has specified
        // We set stopAtNonOption to true so it does not touch the remaining
        // options
        CommandLine cmd = parser.parse(mainOptions, argv, true);
        // get the remaining options...
        argv = cmd.getArgs();

        if (cmd.hasOption("exec")) {
            cmd = parser.parse(execOptions, argv, false);
            // process the exec_op1 and exec_opt2...
            assertTrue(cmd.hasOption("exec_opt1"));
            assertTrue(cmd.hasOption("exec_opt2"));
        } else if (cmd.hasOption("rep")) {
            cmd = parser.parse(repOptions, argv, false);
            // process the rep_op1 and rep_opt2...
        } else {
            fail("exec option not found");
        }
    }

    @Test
    void test13425() throws Exception {
        final Options options = new Options();
        //@formatter:off
        final Option oldpass = OptionBuilder.withLongOpt("old-password")
            .withDescription("Use this option to specify the old password")
            .hasArg()
            .create('o');
        final Option newpass = OptionBuilder.withLongOpt("new-password")
            .withDescription("Use this option to specify the new password")
            .hasArg()
            .create('n');
        //@formatter:on
        final String[] args = {"-o", "-n", "newpassword"};
        options.addOption(oldpass);
        options.addOption(newpass);
        assertThrows(MissingArgumentException.class, () -> new PosixParser().parse(options, args));
    }

    @Test
    void test13666() throws Exception {
        final Options options = new Options();
        final Option dirOption = OptionBuilder.withDescription("dir").hasArg().create('d');
        options.addOption(dirOption);
        final PrintStream oldSystemOut = System.out;
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final String eol = System.lineSeparator();
            System.setOut(new PrintStream(baos));
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("dir", options);
            assertEquals("usage: dir" + eol + " -d <arg>   dir" + eol, baos.toString());
        } finally {
            System.setOut(oldSystemOut);
        }
    }

    @Test
    void test13666_Builder() throws Exception {
        final Options options = new Options();
        final Option dirOption = OptionBuilder.withDescription("dir").hasArg().create('d');
        options.addOption(dirOption);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String eol = System.lineSeparator();
        final HelpFormatter formatter = HelpFormatter.builder().setPrintWriter(new PrintWriter(baos)).get();
        formatter.printHelp("dir", options);
        assertEquals("usage: dir" + eol + " -d <arg>   dir" + eol, baos.toString());
    }

    @Test
    void test13935() throws Exception {
        final OptionGroup directions = new OptionGroup();

        final Option left = new Option("l", "left", false, "go left");
        final Option right = new Option("r", "right", false, "go right");
        final Option straight = new Option("s", "straight", false, "go straight");
        final Option forward = new Option("f", "forward", false, "go forward");
        forward.setRequired(true);

        directions.addOption(left);
        directions.addOption(right);
        directions.setRequired(true);

        final Options opts = new Options();
        opts.addOptionGroup(directions);
        opts.addOption(straight);

        final CommandLineParser parser = new PosixParser();

        assertThrows(ParseException.class, () -> parser.parse(opts, ArrayUtils.EMPTY_STRING_ARRAY));
        assertThrows(ParseException.class, () -> parser.parse(opts, new String[] {"-s"}));

        String[] args = {"-s", "-l"};
        CommandLine line = parser.parse(opts, args);
        assertNotNull(line);

        opts.addOption(forward);
        args = new String[] {"-s", "-l", "-f"};
        line = parser.parse(opts, args);
        assertNotNull(line);
    }

    @Test
    void test14786() throws Exception {
        final Option o = OptionBuilder.isRequired().withDescription("test").create("test");
        final Options opts = new Options();
        opts.addOption(o);
        opts.addOption(o);

        final CommandLineParser parser = new GnuParser();

        final String[] args = {"-test"};

        final CommandLine line = parser.parse(opts, args);
        assertTrue(line.hasOption("test"));
    }

    @Test
    void test15046() throws Exception {
        final CommandLineParser parser = new PosixParser();
        final String[] cliArgs = {"-z", "c"};

        final Options options = new Options();
        options.addOption(new Option("z", "timezone", true, "affected option"));

        parser.parse(options, cliArgs);

        // now add conflicting option
        options.addOption("c", "conflict", true, "conflict option");
        final CommandLine line = parser.parse(options, cliArgs);
        assertEquals(line.getOptionValue('z'), "c");
        assertFalse(line.hasOption("c"));
    }

    @Test
    void test15648() throws Exception {
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-m", "\"Two Words\""};
        final Option m = OptionBuilder.hasArgs().create("m");
        final Options options = new Options();
        options.addOption(m);
        final CommandLine line = parser.parse(options, args);
        assertEquals("Two Words", line.getOptionValue("m"));
    }

    @Test
    void test31148() throws ParseException {
        final Option multiArgOption = new Option("o", "option with multiple args");
        multiArgOption.setArgs(1);

        final Options options = new Options();
        options.addOption(multiArgOption);

        final Parser parser = new PosixParser();
        final String[] args = {};
        final Properties props = new Properties();
        props.setProperty("o", "ovalue");
        final CommandLine cl = parser.parse(options, args, props);

        assertTrue(cl.hasOption('o'));
        assertEquals("ovalue", cl.getOptionValue('o'));
    }

}
