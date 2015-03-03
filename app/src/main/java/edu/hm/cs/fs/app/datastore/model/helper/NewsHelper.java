package edu.hm.cs.fs.app.datastore.model.helper;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.News;
import edu.hm.cs.fs.app.datastore.model.Person;
import edu.hm.cs.fs.app.datastore.model.impl.NewsImpl;
import edu.hm.cs.fs.app.datastore.model.impl.StudyGroup;
import io.realm.Realm;

/**
 * Created by Fabio on 03.03.2015.
 */
public class NewsHelper implements News {
    private final Context mContext;
    private final Person author;
    private final String subject;
    private final String text;
    private final List<Person> teachers;
    private final List<Group> groups;
    private final Date publish;
    private final Date expire;
    private final String url;

    private NewsHelper(Context context, NewsImpl news) {
        mContext = context;
        author = PersonHelper.findById(context, news.getAuthor());
        subject = news.getSubject();
        text = news.getText();
        teachers = new ArrayList<>();
        for (String teacherId : news.getTeachers()) {
            teachers.add(PersonHelper.findById(context, teacherId));
        }
        groups = new ArrayList<>();
        for (String groupId : news.getGroups()) {
            groups.add(StudyGroup.of(groupId));
        }
        publish = news.getPublish();
        expire = news.getExpire();
        url = news.getUrl();
    }

    @Override
    public Person getAuthor() {
        return author;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<Person> getTeachers() {
        return teachers;
    }

    @Override
    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public Date getPublish() {
        return publish;
    }

    @Override
    public Date getExpire() {
        return expire;
    }

    @Override
    public String getUrl() {
        return url;
    }

    static News parse(Context context, JSONObject jsonObject) {
        NewsImpl news = new NewsImpl();
        news.setId(jsonObject.getString("id"));
        news.setContact(jsonObject.getString("contact"));
        news.setDescription(jsonObject.getString("description"));
        news.setExpire(new Date(jsonObject.getLong("expire")));
        news.setProgram(Study.of(jsonObject.getString("program")));
        news.setProvider(jsonObject.getString("provider"));
        news.setTitle(jsonObject.getString("title"));
        news.setUrl(jsonObject.getString("url"));
        return new RealmExecutor<NewsImpl>(context) {
            @Override
            public NewsImpl run(final Realm realm) {
                return new NewsHelper(
                        context,
                        realm.where(NewsImpl.class).equalTo("id", id).findFirst()
                );
            }
        }.execute();
    }

    static News findById(final Context context, final String id) {
        return new RealmExecutor<News>(context) {
            @Override
            public News run(final Realm realm) {
                return new NewsHelper(
                        context,
                        realm.where(NewsImpl.class).equalTo("id", id).findFirst()
                );
            }
        }.execute();
    }

    static List<News> listAll(final Context context) {
        return new RealmExecutor<List<News>>(context) {
            @Override
            public List<News> run(final Realm realm) {
                List<News> helperList = new ArrayList<>();
                for (NewsImpl news : realm.where(NewsImpl.class).findAll()) {
                    helperList.add(new NewsHelper(context, news));
                }
                return helperList;
            }
        }.execute();
    }
}
