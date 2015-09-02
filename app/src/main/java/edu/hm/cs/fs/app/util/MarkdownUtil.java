package edu.hm.cs.fs.app.util;

import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fabio
 */
public final class MarkdownUtil {
    private static final Pattern REGEX_BOLD = Pattern.compile("\\*([^\\*]+)\\*");
    private static final Pattern REGEX_LIST = Pattern.compile("\\s(?:(\\.(?!\\.)[^\\n]+)+|\\.\\s)");
    private static final String NEW_LINE_TAG = "<br/>";
    private static final String BOLD_OPENING_TAG = "<b>";
    private static final String BOLD_CLOSING_TAG = "</b>";

    @NonNull
    public static Spanned toHtml(@NonNull final String text) {
        String result = replaceBoldStrings(text);
        result = result.replaceAll("#", "\n");
        result = replaceList(result);
        result = result.replaceAll("\n", NEW_LINE_TAG);
        return Html.fromHtml(result);
    }

    @NonNull
    private static String replaceBoldStrings(@NonNull final String raw) {
        StringBuilder result = new StringBuilder();
        int lastSubStringEnd = 0;

        final Matcher matcher = REGEX_BOLD.matcher(raw);
        while (matcher.find()) {
            int indexBegin = matcher.start();

            result.append(raw.substring(lastSubStringEnd, indexBegin));
            result.append(BOLD_OPENING_TAG);
            result.append(matcher.group(1));
            result.append(BOLD_CLOSING_TAG);

            lastSubStringEnd = indexBegin + matcher.group().length();
        }

        if (lastSubStringEnd != raw.length()) {
            result.append(raw.substring(lastSubStringEnd, raw.length()));
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
