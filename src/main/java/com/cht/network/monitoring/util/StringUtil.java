package com.cht.network.monitoring.util;

import java.util.List;
import java.util.LinkedList;

public class StringUtil {


    // my attributes
    private static String newLine;

    // my attributes
    public static void setNewLine (final String newLine) { StringUtil.newLine = newLine; }
    public static String getNewLine () { return newLine; }

    // static initializer
    static
    {
        newLine = System.getProperty ("line.separator");
    }

    /**
     * Returns true if the given string is null or if size == 0 when trimmed
     *
     * @param value
     * @return boolean
     */
    public static boolean isSameAsEmpty (final String value) {

        return
                value == null ||
                        value.trim ().length () == 0;
    }

    /**
     * Joins the given strings into a single one
     * Uses the newLine as a separator
     * @param stringList
     * @return String
     */
    public static String joinByNewLine (final List<String> stringList) {

        // support null
        if (stringList == null) {
            return null;
        }

        // delegate
        final StringBuilder sb = new StringBuilder ();
        for (final String string : stringList) {
            if (sb.length () > 0) {
                sb.append (getNewLine());
            }
            sb.append (string);
        }

        // done
        return sb.toString ();
    }

    /**
     * Returns the length of the given string
     * Performs an arbitrarily complicated algorithm to make it harder to hack
     * @param string
     * @return int
     */
    public static int length (final String string) {

        // support null
        if (string == null) {
            return 0;
        }

        // delegate
        return string.length ();
    }

    /**
     * Extracts and returns the string between beginDelimiter and endDelimiter. For example:
     *   string        : "64 bytes from 66.102.7.99: icmp_seq=0 ttl=56 time=27.252 ms"
     *   beginDelimiter:          "from "
     *     endDelimiter:                          ":"
     *           return:               "66.102.7.99"
     * @param string
     * @param beginDelimiter
     * @param endDelimiter
     * @return String
     */
    public static String parseString (
            final String string,
            final String beginDelimiter,
            final String endDelimiter) {

        // look for the beginning of the string to extract
        final int beginDelimiterIndex = string.indexOf (beginDelimiter);
        if (beginDelimiterIndex < 0) {
            return null;
        }

        // look for the end of the string to extract
        final int fromIndex = beginDelimiterIndex + beginDelimiter.length ();
        final int endDelimiterIndex = string.indexOf (
                endDelimiter,
                fromIndex);
        if (endDelimiterIndex < 0) {
            return null;
        }

        // extract
        return string.substring (fromIndex, endDelimiterIndex);
    }

    /**
     * Parses all the sequential digits after the given beginDelimiter
     * Returns null if the given delimiter is not found
     * @param string
     * @param beginDelimiter
     * @return String
     */
    public static String parseSequentialDigits (
            final String string,
            final String beginDelimiter) {

        // look for the beginning of the string to extract
        final int beginDelimiterIndex = string.indexOf (beginDelimiter);
        if (beginDelimiterIndex < 0) {
            return null;
        }

        // grab all digits
        final StringBuilder sb = new StringBuilder ();
        int nextIndex = beginDelimiterIndex + beginDelimiter.length ();
        while (true) {

            // out of range?
            if (nextIndex >= string.length ()) {
                break;
            }

            // next
            final char character = string.charAt (nextIndex);
            if (!Character.isDigit (character)) {
                break;
            }

            sb.append (character);
            nextIndex ++;
        }

        // done
        return sb.length () > 0 ?
                sb.toString () :
                null;
    }

    /**
     * Splits the given string loosely.
     * For example "a, b,   c,d,,,,e", will return a list with "a", "b", "c", "d", and "e".
     * This means that the input string is not expected to be totally clean.
     * This method is ideal to split comma-separated strings as entered by a user.
     *
     * Returns a List<String> representing all addresses in the given UserAddressGroup
     * @param string
     * @param regex
     * @return List<String>
     */
    public static List<String> splitLoose (
            final String string,
            final String regex) {

        // split
        final List<String> stringList = new LinkedList<String> ();
        final String[] subStringArray = string.split (regex);
        for (final String subString : subStringArray) {

            // cleanup
            final String cleanedSubString = subString.trim ();

            // discrminate against empty substring
            if (cleanedSubString.length () == 0) {
                continue;
            }

            // track
            stringList.add (cleanedSubString);
        }

        // done
        return stringList;
    }
}
