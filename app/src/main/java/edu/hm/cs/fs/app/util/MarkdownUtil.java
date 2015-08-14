package edu.hm.cs.fs.app.util;

import android.text.Html;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fabio
 */
public final class MarkdownUtil {
    private static final Pattern PATTERN_BOLD = Pattern.compile("\\*([^\\*]+)\\*");
    private static final Pattern PATTERN_NEW_LINE = Pattern.compile("(?:#|\\n)");

    private static final String NEW_LINE_TAG = "<br>";
    private static final String BOLD_OPENING_TAG = "<b>";
    private static final String BOLD_CLOSING_TAG = "</b>";
    //private static final String LIST_OPENING_TAG = "<ul>";
    //private static final String LIST_CLOSING_TAG = "</ul>";
    //private static final String LIST_ITEM_OPENING_TAG = "<li>";
    //private static final String LIST_ITEM_CLOSING_TAG = "</li>";

    private MarkdownUtil() {
    }

    public static Spanned toHtml(final String text) {
        String result = replaceNewLines(text);
        result = replaceBoldStrings(result);
        return Html.fromHtml(result);
    }

    private static String replaceNewLines(String raw) {
        StringBuilder result = new StringBuilder();
        int lastSubStringEnd = 0;

        final Matcher matcher = PATTERN_NEW_LINE.matcher(raw);
        while(matcher.find()) {
            int indexBegin = matcher.start();

            result.append(raw.substring(lastSubStringEnd, indexBegin));
            result.append(NEW_LINE_TAG);

            lastSubStringEnd = indexBegin + matcher.group().length();
        }

        if(lastSubStringEnd != raw.length()) {
            result.append(raw.substring(lastSubStringEnd, raw.length()));
        }

        return result.toString();
    }

    private static String replaceBoldStrings(String raw) {
        StringBuilder result = new StringBuilder();
        int lastSubStringEnd = 0;

        final Matcher matcher = PATTERN_BOLD.matcher(raw);
        while(matcher.find()) {
            int indexBegin = matcher.start();

            result.append(raw.substring(lastSubStringEnd, indexBegin));
            result.append(BOLD_OPENING_TAG);
            result.append(matcher.group(1));
            result.append(BOLD_CLOSING_TAG);

            lastSubStringEnd = indexBegin + matcher.group().length();
        }

        if(lastSubStringEnd != raw.length()) {
            result.append(raw.substring(lastSubStringEnd, raw.length()));
        }

        return result.toString();
    }
}
