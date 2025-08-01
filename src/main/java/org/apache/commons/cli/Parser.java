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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

/**
 * {@code Parser} creates {@link CommandLine}s.
 *
 * @deprecated since 1.3, the two-pass parsing with the flatten method is not enough flexible to handle complex cases
 */
@Deprecated
public abstract class Parser implements CommandLineParser {

    /** CommandLine instance */
    protected CommandLine cmd;

    /** Current Options */
    private Options options;

    /** List of required options strings */
    private List requiredOptions;

    /**
     * Constructs a new instance.
     */
    public Parser() {
        // empty
    }

    /**
     * Throws a {@link MissingOptionException} if all of the required options are not present.
     *
     * @throws MissingOptionException if any of the required Options are not present.
     */
    protected void checkRequiredOptions() throws MissingOptionException {
        // if there are required options that have not been processed
        if (!getRequiredOptions().isEmpty()) {
            throw new MissingOptionException(getRequiredOptions());
        }
    }

    /**
     * Subclasses must implement this method to reduce the {@code arguments} that have been passed to the parse method.
     *
     * @param opts The Options to parse the arguments by.
     * @param arguments The arguments that have to be flattened.
     * @param stopAtNonOption specifies whether to stop flattening when a non option has been encountered
     * @return a String array of the flattened arguments
     * @throws ParseException if there are any problems encountered while parsing the command line tokens.
     */
    protected abstract String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException;

    /**
     * Gets the options.
     *
     * @return the options.
     */
    protected Options getOptions() {
        return options;
    }

    /**
     * Gets the required options.
     *
     * @return the required options.
     */
    protected List getRequiredOptions() {
        return requiredOptions;
    }

    /**
     * Parses the specified {@code arguments} based on the specified {@link Options}.
     *
     * @param options the {@code Options}
     * @param arguments the {@code arguments}
     * @return the {@code CommandLine}
     * @throws ParseException if there are any problems encountered while parsing the command line tokens.
     */
    @Override
    public CommandLine parse(final Options options, final String[] arguments) throws ParseException {
        return parse(options, arguments, null, false);
    }

    /**
     * Parses the specified {@code arguments} based on the specified {@link Options}.
     *
     * @param options the {@code Options}
     * @param arguments the {@code arguments}
     * @param stopAtNonOption if {@code true} an unrecognized argument stops the parsing and the remaining arguments
     *        are added to the {@link CommandLine}s args list. If {@code false} an unrecognized argument triggers a
     *        ParseException.
     * @return the {@code CommandLine}
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Override
    public CommandLine parse(final Options options, final String[] arguments, final boolean stopAtNonOption) throws ParseException {
        return parse(options, arguments, null, stopAtNonOption);
    }

    /**
     * Parse the arguments according to the specified options and properties.
     *
     * @param options the specified Options
     * @param arguments the command line arguments
     * @param properties command line option name-value pairs
     * @return the list of atomic option and value tokens
     * @throws ParseException if there are any problems encountered while parsing the command line tokens.
     * @since 1.1
     */
    public CommandLine parse(final Options options, final String[] arguments, final Properties properties) throws ParseException {
        return parse(options, arguments, properties, false);
    }

    /**
     * Parse the arguments according to the specified options and properties.
     *
     * @param options the specified Options
     * @param arguments the command line arguments
     * @param properties command line option name-value pairs
     * @param stopAtNonOption if {@code true} an unrecognized argument stops the parsing and the remaining arguments
     *        are added to the {@link CommandLine}s args list. If {@code false} an unrecognized argument triggers a
     *        ParseException.
     * @return the list of atomic option and value tokens
     * @throws ParseException if there are any problems encountered while parsing the command line tokens.
     * @since 1.1
     */
    public CommandLine parse(final Options options, final String[] arguments, final Properties properties, final boolean stopAtNonOption)
            throws ParseException {
        // clear out the data in options in case it's been used before (CLI-71)
        for (final Option opt : options.helpOptions()) {
            opt.clearValues();
        }
        // clear the data from the groups
        for (final OptionGroup optionGroup : options.getOptionGroups()) {
            optionGroup.setSelected(null);
        }
        // initialize members
        setOptions(options);
        cmd = CommandLine.builder().get();
        boolean eatTheRest = false;
        final List<String> tokenList = Arrays.asList(flatten(getOptions(), arguments == null ? new String[0] : arguments, stopAtNonOption));
        final ListIterator<String> iterator = tokenList.listIterator();
        // process each flattened token
        while (iterator.hasNext()) {
            final String token = iterator.next();
            if (token != null) {
                // the value is the double-dash
                if ("--".equals(token)) {
                    eatTheRest = true;
                } else if ("-".equals(token)) {
                    // the value is a single dash
                    if (stopAtNonOption) {
                        eatTheRest = true;
                    } else {
                        cmd.addArg(token);
                    }
                } else if (token.startsWith("-")) {
                    // the value is an option
                    if (stopAtNonOption && !getOptions().hasOption(token)) {
                        eatTheRest = true;
                        cmd.addArg(token);
                    } else {
                        processOption(token, iterator);
                    }
                } else {
                    // the value is an argument
                    cmd.addArg(token);
                    if (stopAtNonOption) {
                        eatTheRest = true;
                    }
                }
                // eat the remaining tokens
                if (eatTheRest) {
                    while (iterator.hasNext()) {
                        final String str = iterator.next();
                        // ensure only one double-dash is added
                        if (!"--".equals(str)) {
                            cmd.addArg(str);
                        }
                    }
                }
            }
        }
        processProperties(properties);
        checkRequiredOptions();
        return cmd;
    }

    /**
     * Process the argument values for the specified Option {@code opt} using the values retrieved from the specified
     * iterator {@code iter}.
     *
     * @param opt The current Option
     * @param iter The iterator over the flattened command line Options.
     * @throws ParseException if an argument value is required and it is has not been found.
     */
    public void processArgs(final Option opt, final ListIterator<String> iter) throws ParseException {
        // loop until an option is found
        while (iter.hasNext()) {
            final String str = iter.next();
            // found an Option, not an argument
            if (getOptions().hasOption(str) && str.startsWith("-")) {
                iter.previous();
                break;
            }
            // found a value
            try {
                opt.processValue(Util.stripLeadingAndTrailingQuotes(str));
            } catch (final RuntimeException exp) {
                iter.previous();
                break;
            }
        }
        if (opt.getValues() == null && !opt.hasOptionalArg()) {
            throw new MissingArgumentException(opt);
        }
    }

    /**
     * Process the Option specified by {@code arg} using the values retrieved from the specified iterator
     * {@code iter}.
     *
     * @param arg The String value representing an Option
     * @param iter The iterator over the flattened command line arguments.
     * @throws ParseException if {@code arg} does not represent an Option
     */
    protected void processOption(final String arg, final ListIterator<String> iter) throws ParseException {
        final boolean hasOption = getOptions().hasOption(arg);
        // if there is no option throw an UnrecognizedOptionException
        if (!hasOption) {
            throw new UnrecognizedOptionException("Unrecognized option: " + arg, arg);
        }
        // get the option represented by arg
        final Option opt = (Option) getOptions().getOption(arg).clone();
        // update the required options and groups
        updateRequiredOptions(opt);
        // if the option takes an argument value
        if (opt.hasArg()) {
            processArgs(opt, iter);
        }
        // set the option on the command line
        cmd.addOption(opt);
    }

    /**
     * Sets the values of Options using the values in {@code properties}.
     *
     * @param properties The value properties to be processed.
     * @throws ParseException if there are any problems encountered while processing the properties.
     */
    protected void processProperties(final Properties properties) throws ParseException {
        if (properties == null) {
            return;
        }
        for (final Enumeration<?> e = properties.propertyNames(); e.hasMoreElements();) {
            final String option = e.nextElement().toString();
            final Option opt = options.getOption(option);
            if (opt == null) {
                throw new UnrecognizedOptionException("Default option wasn't defined", option);
            }
            // if the option is part of a group, check if another option of the group has been selected
            final OptionGroup optionGroup = options.getOptionGroup(opt);
            final boolean selected = optionGroup != null && optionGroup.isSelected();
            if (!cmd.hasOption(option) && !selected) {
                // get the value from the properties instance
                final String value = properties.getProperty(option);
                if (opt.hasArg()) {
                    if (Util.isEmpty(opt.getValues())) {
                        try {
                            opt.processValue(value);
                        } catch (final RuntimeException exp) { // NOPMD
                            // if we cannot add the value don't worry about it
                        }
                    }
                } else if (!("yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value))) {
                    // if the value is not yes, true or 1 then don't add the
                    // option to the CommandLine
                    continue;
                }
                cmd.addOption(opt);
                updateRequiredOptions(opt);
            }
        }
    }

    /**
     * Sets the options.
     *
     * @param options the options.
     */
    protected void setOptions(final Options options) {
        this.options = options;
        this.requiredOptions = new ArrayList<>(options.getRequiredOptions());
    }

    /**
     * Removes the option or its group from the list of expected elements.
     *
     * @param opt
     */
    private void updateRequiredOptions(final Option opt) throws ParseException {
        // if the option is a required option remove the option from
        // the requiredOptions list
        if (opt.isRequired()) {
            getRequiredOptions().remove(opt.getKey());
        }
        // if the option is in an OptionGroup make that option the selected
        // option of the group
        if (getOptions().getOptionGroup(opt) != null) {
            final OptionGroup optionGroup = getOptions().getOptionGroup(opt);
            if (optionGroup.isRequired()) {
                getRequiredOptions().remove(optionGroup);
            }
            optionGroup.setSelected(opt);
        }
    }

}
