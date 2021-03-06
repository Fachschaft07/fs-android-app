package edu.hm.cs.fs.app.util;

import android.text.Html;
import android.text.Spanned;

import com.fk07.BuildConfig;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MarkdownUtilTest {
    private static final String BOLD_INPUT = "this is a *test*.";
    private static final Spanned BOLD_OUTPUT = Html.fromHtml("this is a <b>test</b>.");
    private static final String NEW_LINE_INPUT = "this #is #a #test.";
    private static final Spanned NEW_LINE_OUTPUT = Html.fromHtml("this <br/>is <br/>a <br/>test.");
    private static final String LIST_INPUT = " .Test\n.Hello\n.World\n";
    private static final Spanned LIST_OUTPUT = Html.fromHtml("<br/><br/><br/>&#8226; " +
            "Test<br/>&#8226; Hello<br/>&#8226; World<br/><br/><br/>");

    @Test
    public void testMarkdownBold() {
        final Spanned result = MarkdownUtil.toHtml(BOLD_INPUT);
        Assert.assertThat(BOLD_OUTPUT, CoreMatchers.equalTo(result));
    }

    @Test
    public void testMarkdownNewLine() {
        final Spanned result = MarkdownUtil.toHtml(NEW_LINE_INPUT);
        Assert.assertThat(NEW_LINE_OUTPUT, CoreMatchers.equalTo(result));
    }

    @Test
    public void testMarkdownList() {
        final Spanned result = MarkdownUtil.toHtml(LIST_INPUT);
        Assert.assertThat(LIST_OUTPUT, CoreMatchers.equalTo(result));
    }
}
