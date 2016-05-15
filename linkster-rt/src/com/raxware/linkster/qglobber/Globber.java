/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
package com.raxware.linkster.qglobber;

import java.util.ArrayList;
import java.util.List;

/**
 * This works as the logic to expand a string into multiple strings using
 * a specified formatting language.
 * <p>
 * This is only a preliminary work - needs to be revamped. Need to expand on documentation as well.
 *
 * @author Will Chapman (elitecodex)
 */
public class Globber {

    public static String PLACE_HOLDER = "{[]}";

    /**
     * Performs the logic for the <code>split</code> and
     * <code>splitPreserveAllTokens</code> methods that do not return a
     * maximum array length.
     * <p>
     * CHANGES: Renamed
     * <p>
     * Borrowed from the Apache Foundation
     *
     * @param str               the String to parse, may be <code>null</code>
     * @param separatorChar     the separate character
     * @param preserveAllTokens if <code>true</code>, adjacent separators are
     *                          treated as empty token separators; if <code>false</code>, adjacent
     *                          separators are treated as one separator.
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str, char separatorChar, boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            //return ArrayUtils.EMPTY_STRING_ARRAY;
            return new String[0];
        }
        List list = new ArrayList();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * <p>
     * Checks if a String is empty ("") or null.</p>
     * <p>
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     * <p>
     * <p>
     * NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     * <p>
     * Borrowed from the Apache Foundation
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>
     * Replaces a String with another String inside a larger String,
     * for the first <code>max</code> values of the search String.</p>
     * <p>
     * <p>
     * A <code>null</code> reference passed to this method is a no-op.</p>
     * <p>
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     * <p>
     * Borrowed from the Apache Foundation
     *
     * @param text         text to search and replace in, may be null
     * @param searchString the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @param max          maximum number of values to replace, or <code>-1</code> if no maximum
     * @return the text with any replacements processed,
     * <code>null</code> if null String input
     */
    public static String replace(String text, String searchString, String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * Takes a sequence and returns an integer array with the starting and
     * ending endpoints in elements 0 and 1 respectively.
     * <p>
     * Format: &lt;START&gt;..&lt;END&gt;
     *
     * @param seq A string representing the sequence
     * @return integer array. The zero element is the START; the one element is the END
     */
    private static int[] breakSequence(String seq) {
        int[] endpoints = new int[2];

        int dotdot_pos = seq.indexOf("..");

        int start = Integer.parseInt(seq.substring(0, dotdot_pos));
        int end = Integer.parseInt(seq.substring(dotdot_pos + 2, seq.length()));

        endpoints[0] = start;
        endpoints[1] = end;

        return endpoints;
    }

    /**
     * Same as breakSequence, just returns the ASCII code for the starting and
     * ending characters.
     * <p>
     * REQUIREMENTS: Must be explicitly asked for (prefixed with 'A:'). If not,
     * falls back to breakSequence and assumes integers
     *
     * @param seq
     * @return
     */
    private static int[] breakAlphaSequence(String seq)
            throws IllegalArgumentException {
        // minimum valid length for an alpha sequence is 6
        // A:a..b

        if (seq.length() < 6) {
            throw new IllegalArgumentException("Sequence too short for alpha-sequence");
        }

        if (seq.indexOf("A:") != -1) // alpha was explicitly asked for - good to go
        {
            int[] endpoints = new int[2];

            int dotdot_pos = seq.indexOf("..");

            String start = seq.substring(seq.indexOf("A:") + 2, dotdot_pos);
            String end = seq.substring(dotdot_pos + 2, seq.length());

            if (start.length() != 1 || end.length() != 1) {
                throw new IllegalArgumentException("The parsed arguments are invalid (" + start + "," + end + ")");
            }

            endpoints[0] = start.charAt(0);
            endpoints[1] = end.charAt(0);

            return endpoints;
        } else {
            return breakSequence(seq);
        }

    }

    /**
     * Takes a single template and a single sequence and compiles them to
     * an ArrayList of strings
     *
     * @param t
     * @param s
     * @return
     */
    public static ArrayList explodeSingleNumericalSequence(String t, String s) {
        ArrayList list = new ArrayList();

        // find out where the first placeholder is
        int position = t.indexOf(PLACE_HOLDER);
        int endPosition = position + PLACE_HOLDER.length();

        // break the string around the placeholder
        String first = t.substring(0, endPosition);
        String second = t.substring(endPosition, t.length());

        // are we looking for an alpha sequence?
        boolean alphaFlag = s.charAt(0) == 'A' && s.charAt(1) == ':';
        int[] endpoints = alphaFlag ? breakAlphaSequence(s) : breakSequence(s);
        for (int a = endpoints[0]; a <= endpoints[1]; a++) {
            String val = alphaFlag ? Character.toString((char) a) : String.valueOf(a);
            //first = replace(first,"{[]}", String.valueOf(a),-1);
            list.add(replace(first, PLACE_HOLDER, val, -1) + second);
        }
        return list;
    }

    /**
     * This is for taking a single template string and using explodeSingle... to
     * create an ArrayList for each item in the sequence array
     *
     * @param t
     * @param s
     * @return
     */
    public static ArrayList explodeSequence(String t, String[] s, int index)
            throws RuntimeException {
        if (s == null || s.length <= 0) {
            throw new RuntimeException("Invalid sequence array");
        }

        if (t == null) {
            throw new NullPointerException("Invalid template string");
        }

        ArrayList list = new ArrayList(s.length * 2);
        list.addAll(explodeSingleNumericalSequence(t, s[index]));
        String lastElement = (String) list.get(list.size() - 1);
        if (lastElement.indexOf(PLACE_HOLDER) > 0 && s.length > index) {
            ArrayList sublist = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                ArrayList temp = explodeSequence((String) list.get(i), s, index + 1);
                sublist.addAll(temp);
            }
            return sublist;
        }

        return list;
    }

    /**
     * @param t
     * @param s
     * @return
     * @throws RuntimeException
     * @deprecated
     */
    public static ArrayList explodeNumericalSequence(String t, String[] s)
            throws RuntimeException {
        return explodeSequence(t, s, 0);
    }

    /**
     * Test driver.
     *
     * @param args
     */
    public static void main(String[] args) {
        String[] seq = {"A:A..C", "1..5", "1..5", "A:A..B"};
        ArrayList files = Globber.explodeSequence(
                "slot:/Building{[]}/Floor{[]}/VAV{[]}{[]}",
                seq,
                0
        );
        /*
         String[] files = Globber.explodeNumericalSequence
         (
         "slot:/Building{[]}/Floor{[]}",
         new String[] {"10..15", "1..3"}
         );
         */

        System.out.println("Results\n================");
        for (int i = 0; i < files.size(); i++) {
            System.out.println("files[" + i + "] = " + files.get(i));
        }

        //int[] test1 = breakAlphaSequence("A:a..f");
    }
}
