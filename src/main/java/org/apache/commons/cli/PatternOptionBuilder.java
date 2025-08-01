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

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 * Allows Options to be created from a single String. The pattern contains various single character flags and via an
 * optional punctuation character, their expected type.
 *
 * <table border="1">
 * <caption>Overview of PatternOptionBuilder patterns</caption>
 * <tr>
 * <td>a</td>
 * <td>-a flag</td>
 * </tr>
 * <tr>
 * <td>b@</td>
 * <td>-b [class name]</td>
 * </tr>
 * <tr>
 * <td>c&gt;</td>
 * <td>-c [file name]</td>
 * </tr>
 * <tr>
 * <td>d+</td>
 * <td>-d [class name] (creates object via empty constructor)</td>
 * </tr>
 * <tr>
 * <td>e%</td>
 * <td>-e [number] (creates Double/Long instance depending on existing of a '.')</td>
 * </tr>
 * <tr>
 * <td>f/</td>
 * <td>-f [URL]</td>
 * </tr>
 * <tr>
 * <td>g:</td>
 * <td>-g [string]</td>
 * </tr>
 * </table>
 *
 * <p>
 * For example, the following allows command line flags of '-v -p string-value -f /dir/file'. The exclamation mark
 * precede a mandatory option.
 * </p>
 *
 * <pre>
 * Options options = PatternOptionBuilder.parsePattern("vp:!f/");
 * </pre>
 *
 * <p>
 * TODO These need to break out to OptionType and also to be pluggable.
 * </p>
 */
public class PatternOptionBuilder {

    /** String class */
    public static final Class<String> STRING_VALUE = String.class;

    /** Object class */
    public static final Class<Object> OBJECT_VALUE = Object.class;

    /** Number class */
    public static final Class<Number> NUMBER_VALUE = Number.class;

    /** Date class */
    public static final Class<Date> DATE_VALUE = Date.class;

    /** Class class */
    public static final Class<?> CLASS_VALUE = Class.class;

    /** FileInputStream class */
    public static final Class<FileInputStream> EXISTING_FILE_VALUE = FileInputStream.class;

    /// can we do this one??
    // is meant to check that the file exists, else it errors.
    // ie) it's for reading not writing.

    /** File class */
    public static final Class<File> FILE_VALUE = File.class;

    /** File array class */
    public static final Class<File[]> FILES_VALUE = File[].class;

    /** URL class */
    public static final Class<URL> URL_VALUE = URL.class;

    /** The converter to use for Unimplemented data types */
    private static final Converter<?, UnsupportedOperationException> UNSUPPORTED = s -> {
        throw new UnsupportedOperationException("Not yet implemented");
    };

    /**
     * Retrieve the class that {@code ch} represents.
     *
     * @param ch the specified character
     * @return The class that {@code ch} represents
     * @deprecated use {@link #getValueType(char)}
     */
    @Deprecated // since="1.7.0"
    public static Object getValueClass(final char ch) {
        return getValueType(ch);
    }

    /**
     * Retrieve the class that {@code ch} represents.
     *
     * @param ch the specified character
     * @return The class that {@code ch} represents
     * @since 1.7.0
     */
    public static Class<?> getValueType(final char ch) {
        switch (ch) {
        case '@':
            return OBJECT_VALUE;
        case ':':
            return STRING_VALUE;
        case '%':
            return NUMBER_VALUE;
        case '+':
            return CLASS_VALUE;
        case '#':
            return DATE_VALUE;
        case '<':
            return EXISTING_FILE_VALUE;
        case '>':
            return FILE_VALUE;
        case '*':
            return FILES_VALUE;
        case '/':
            return URL_VALUE;
        }

        return null;
    }

    /**
     * Returns whether {@code ch} is a value code, i.e. whether it represents a class in a pattern.
     *
     * @param ch the specified character
     * @return true if {@code ch} is a value code, otherwise false.
     */
    public static boolean isValueCode(final char ch) {
        return ch == '@' || ch == ':' || ch == '%' || ch == '+' || ch == '#' || ch == '<' || ch == '>' || ch == '*' || ch == '/' || ch == '!';
    }

    /**
     * Returns the {@link Options} instance represented by {@code pattern}.
     *
     * @param pattern the pattern string
     * @return The {@link Options} instance
     */
    public static Options parsePattern(final String pattern) {
        char opt = Char.SP;
        boolean required = false;
        Class<?> type = null;
        Converter<?, ?> converter = Converter.DEFAULT;

        final Options options = new Options();

        for (int i = 0; i < pattern.length(); i++) {
            final char ch = pattern.charAt(i);

            // a value code comes after an option and specifies
            // details about it
            if (!isValueCode(ch)) {
                if (opt != Char.SP) {
                    // @formatter:off
                    final Option option = Option.builder(String.valueOf(opt))
                            .hasArg(type != null)
                            .required(required)
                            .type(type)
                            .converter(converter)
                            .get();
                    // @formatter:on
                    // we have a previous one to deal with
                    options.addOption(option);
                    required = false;
                    type = null;
                    converter = Converter.DEFAULT;
                }

                opt = ch;
            } else if (ch == '!') {
                required = true;
            } else {
                type = getValueType(ch);
                final Map<Class<?>, Converter<?, ? extends Throwable>> map = TypeHandler.createDefaultMap();
                // Backward compatibility (probably).
                map.put(FILES_VALUE, unsupported());
                converter = new TypeHandler(map).getConverter(getValueType(ch));
            }
        }

        if (opt != Char.SP) {
            final Option option = Option.builder(String.valueOf(opt)).hasArg(type != null).required(required).type(type).get();

            // we have a final one to deal with
            options.addOption(option);
        }

        return options;
    }

    @SuppressWarnings("unchecked")
    static <T> T unsupported() {
        return (T) UNSUPPORTED;
    }

    /**
     * Deprecated, only provides static methods.
     *
     * @deprecated Will be private or class will be final.
     */
    @Deprecated
    public PatternOptionBuilder() {
        // empty
    }
}
