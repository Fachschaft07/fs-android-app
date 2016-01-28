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
    private static final String BOLD_OUTPUT = "this is a <b>test</b>.";
    private static final String LIST_INPUT = " .Test\n.Hello\n.World\n";
    private static final String LIST_OUTPUT = "<br/><br/><br/>&#8226; " +
            "Test<br/>&#8226; Hello<br/>&#8226; World<br/><br/><br/>";
    private static final String LINK_INPUT = "Vom 01.02. bis 05.02.2016 findet in München zum 25. " +
            "Mal die OOP-Konferenz statt, mit einer Fülle an Themen rund um Software Engineering " +
            "und Softwarearchitektur. Details finden Sie hier: http://www.oop-konferenz.de/ #Die " +
            "Teilnahme ist vergleichsweise teuer. Es gibt aber für Studierende eine begrenzte Zahl " +
            "an kostenfreien Tickets für ausgewählte Tage. Dafür bewerben können Sie sich hier: " +
            "https://www.andrena.de/oop-2016-studi #Außerdem werden noch Student Volunteers " +
            "gesucht, die bei der Organisation unterstützen (Unterlagen verteilen, " +
            "Eingangskontrolle etc.). Als Gegenleistung gibt es kostenfreien Eintritt. Weitere " +
            "Informationen finden Sie hier: " +
            "http://www.oop-konferenz.de/oop2016/konferenz/student-volunteers.html";
    private static final String LINK_OUTPUT = "Vom 01.02. bis 05.02.2016 findet in München zum 25. " +
            "Mal die OOP-Konferenz statt, mit einer Fülle an Themen rund um Software Engineering " +
            "und Softwarearchitektur. Details finden Sie hier: " +
            "<a href='http://www.oop-konferenz.de/'>http://www.oop-konferenz.de/</a> #Die " +
            "Teilnahme ist vergleichsweise teuer. Es gibt aber für Studierende eine begrenzte Zahl " +
            "an kostenfreien Tickets für ausgewählte Tage. Dafür bewerben können Sie sich hier: " +
            "<a href='https://www.andrena.de/oop-2016-studi'>" +
            "https://www.andrena.de/oop-2016-studi</a> #Außerdem werden noch Student Volunteers " +
            "gesucht, die bei der Organisation unterstützen (Unterlagen verteilen, " +
            "Eingangskontrolle etc.). Als Gegenleistung gibt es kostenfreien Eintritt. Weitere " +
            "Informationen finden Sie hier: " +
            "<a href='http://www.oop-konferenz.de/oop2016/konferenz/student-volunteers.html'>" +
            "http://www.oop-konferenz.de/oop2016/konferenz/student-volunteers.html</a>";

    @Test
    public void testMarkdownBold() {
        final String result = MarkdownUtil.replaceBoldStrings(BOLD_INPUT);
        Assert.assertThat(BOLD_OUTPUT, CoreMatchers.equalTo(result));
    }

    @Test
    public void testMarkdownList() {
        final String result = MarkdownUtil.replaceList(LIST_INPUT);
        Assert.assertThat(LIST_OUTPUT, CoreMatchers.equalTo(result));
    }

    @Test
    public void testMarkdownLink() {
        final String result = MarkdownUtil.replaceLink(LINK_INPUT);
        Assert.assertThat(LINK_OUTPUT, CoreMatchers.equalTo(result));
    }
}
