package edu.hm.cs.fs.app.util;

import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An utility class to convert a string, which contains a specific markdown language, into a html
 * based string.
 *
 * @author Fabio
 */
public final class MarkdownUtil {
    private static final Pattern REGEX_BOLD = Pattern.compile("\\*([^\\*]+)\\*");
    private static final Pattern REGEX_LIST = Pattern.compile("\\s(?:(\\.(?!\\.)[^\\n]+)+|\\.\\s)");
    private static final Pattern REGEX_LINK = Pattern.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", Pattern.CASE_INSENSITIVE);
    private static final String NEW_LINE_TAG = "<br/>";
    private static final String BOLD_OPENING_TAG = "<b>";
    private static final String BOLD_CLOSING_TAG = "</b>";

    /**
     * Converts a string, which contains a specific markdown language, into a html based string.
     *
     * @param text to convert.
     * @return the html content.
     */
    @NonNull
    public static Spanned toHtml(@NonNull final String text) {
        // The order of these statements is important!!!
        String result = replaceBoldStrings(text); // replace all bold parts
        result = result.replaceAll("#", "\n"); // replace all # with new lines
        result = replaceList(result); // replace the list syntax with a unicode sign
        result = result.replaceAll("\n", NEW_LINE_TAG); // replace all new lines with html tags
        result = replaceLink(result);
        return Html.fromHtml(result); // create the html code
    }

    @NonNull
    private static String replaceLink(@NonNull final String raw) {
        StringBuilder result = new StringBuilder();
        int lastSubStringEnd = 0;

        Matcher urlMatcher = REGEX_LINK.matcher(raw);
        while(urlMatcher.find()) {
            final int start = urlMatcher.start(0);

            result.append(raw.substring(lastSubStringEnd, start));
            result.append("<a href='").append(urlMatcher.group(0)).append("'>");
            result.append(urlMatcher.group(0));
            result.append("</a>");

            lastSubStringEnd = start + urlMatcher.group().length(); // save the last position
        }

        if (lastSubStringEnd != raw.length()) { // if the last position is not the end of string
            result.append(raw.substring(lastSubStringEnd, raw.length())); // append the rest of the string
        }

        return result.toString();
    }

    @NonNull
    private static String replaceBoldStrings(@NonNull final String raw) {
        StringBuilder result = new StringBuilder();
        int lastSubStringEnd = 0;

        final Matcher matcher = REGEX_BOLD.matcher(raw);
        while (matcher.find()) {
            int indexBegin = matcher.start(); // start position of the match

            result.append(raw.substring(lastSubStringEnd, indexBegin)); // get everything before the match
            result.append(BOLD_OPENING_TAG); // insert the opening bold html tag
            result.append(matcher.group(1)); // insert the matching part
            result.append(BOLD_CLOSING_TAG); // insert the closing bold html tag

            lastSubStringEnd = indexBegin + matcher.group().length(); // save the last position
        }

        if (lastSubStringEnd != raw.length()) { // if the last position is not the end of string
            result.append(raw.substring(lastSubStringEnd, raw.length())); // append the rest of the string
        }

        return result.toString();
    }

    @NonNull
    private static String replaceList(@NonNull final String raw) {
        StringBuilder result = new StringBuilder();
        int lastSubStringEnd = 0;
        boolean listDetected = false;

        final Matcher matcher = REGEX_LIST.matcher(raw);
        while (matcher.find()) {
            if (!listDetected) {
                result.append(NEW_LINE_TAG);
                result.append(NEW_LINE_TAG);
            }
            listDetected = true;
            final int indexBegin = matcher.start();

            final String group = matcher.group(1);
            if (group != null) {
                final String substring = group.substring(1, group.length());

                result.append(raw.substring(lastSubStringEnd, indexBegin));
                result.append(NEW_LINE_TAG);
                result.append("&#8226").append(" "); // HTML-Tag-Dot
                result.append(substring);
            }

            lastSubStringEnd = indexBegin + matcher.group().length();
        }

        if (listDetected && lastSubStringEnd != raw.length()) {
            result.append(NEW_LINE_TAG);
            result.append(NEW_LINE_TAG);
            result.append(raw.substring(lastSubStringEnd, raw.length()));
        } else if (!listDetected) {
            result.append(raw);
        }

        return result.toString();
    }
}
