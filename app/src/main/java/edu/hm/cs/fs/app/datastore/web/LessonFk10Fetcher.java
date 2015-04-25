package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import edu.hm.cs.fs.app.datastore.model.impl.LessonImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractHtmlFetcher;

public class LessonFk10Fetcher extends AbstractHtmlFetcher<LessonFk10Fetcher, LessonImpl> {
    private static final String URL = "http://w3bw-o.hm.edu/iframe/studieninfo_vorlesungsplan.php";
    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0";
    private final Group mGroup;

    public LessonFk10Fetcher(Context context, Group group) {
        super(context, URL);
        mGroup = group;
    }

    @Override
    protected List<LessonImpl> read(final String url) {
        List<LessonImpl> result = new ArrayList<>();
        try {
            final Connection connect = Jsoup
                    .connect(url)
                    .referrer(url)
                    .userAgent(USER_AGENT)
                    .data("semestergr", getGroupId(mGroup))
                    .data("modul", "")
                    .data("lv", "")
                    .data("dozent", "")
                    .data("kategorie", "Stundenplan")
                    .data("sem", "")
                    .data("Stundenplan anzeigen", "suchen");
            final Document document = connect.post();
            result.addAll(readFromDoc(document));
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
        return result;
    }

    @Override
    protected List<LessonImpl> readFromDoc(final Document document) {
        List<LessonImpl> result = new ArrayList<>();

        Day day = null;
        Time time = null;
        String subject = null;
        String room = null;

        final Element table = document.select("table").get(4);
        final Elements rows = table.getElementsByTag("td");
        for (final Element row : rows) {
            if (row.toString().contains("<h1")) { // i.e. <h1>Montag</h1>
                day = Day.of(row.text());
            } else if (row.toString().contains("<h3")) { // i.e. <h3>10:00-11:30 UHR</h3>
                time = Time.of(row.text().substring(0, row.text().indexOf("-")));
            } else if (row.text().matches("[A-Za-z ]+[0-9]+ .*")) { // i.e. B 24 Marketing
                subject = row.text();
            } else if (row.text().matches("[A-Za-z]+[0-9]*")) { // i.e. LE001
                room = row.text();
            } else if (!row.text().equals("")) {
                LessonImpl lesson = new LessonImpl();
                lesson.setDay(day.toString());
                lesson.setTime(time.toString());
                lesson.setModuleId(subject);
                lesson.setRoom(room);

                result.add(lesson);
            }
        }

        return result;
    }

    public static String getGroupId(final Group group) throws IOException {
        Connection conn = Jsoup.connect(URL);
        conn = conn.referrer(URL);
        conn = conn.userAgent(USER_AGENT);
        final Document doc = conn.get();
        final Elements scripts = doc.select("script");
        final Pattern pattern = Pattern.compile("addOption\\([^\\)]*\\)");
        final Matcher matcher = pattern.matcher(scripts.get(5).toString()); // SS14 = 6; WS1415 = 5
        final Map<Group, String> groups = new HashMap<>();
        while (matcher.find()) {
            final String match = matcher.group();
            if (match.contains("WIF ")) {
                String groupName = match.replaceFirst("addOption\\(\\\"schs[0-9]+\", \"", "");
                groupName = groupName.replaceFirst("", "");
                String groupId = groupName;
                groupName = groupName.replaceFirst("\", \"[0-9]*\"\\)", "");
                groupId = groupId.replaceFirst("WIF [0-9]*[ ]?[A-Za-z]*\", \"", "");
                groupId = groupId.replaceFirst("\"\\)", "");

                final Group foundGroup;
                final String[] groupSplitter = groupName.split(" ");
                if(groupSplitter.length > 2 && "M".equalsIgnoreCase(groupSplitter[2])) {
                    foundGroup = GroupImpl.of(Study.IN.toString() + groupSplitter[1]);
                } else {
                    foundGroup = GroupImpl.of(Study.IB.toString() + groupSplitter[1] + (groupSplitter.length > 2 ? groupSplitter[2] : ""));
                }

                if(group.equals(foundGroup)) {
                    return groupId;
                }
            }
        }
        return "-1";
    }
}
