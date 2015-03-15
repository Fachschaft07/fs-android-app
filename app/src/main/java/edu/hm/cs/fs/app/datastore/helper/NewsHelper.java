package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.News;
import edu.hm.cs.fs.app.datastore.model.Person;
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import edu.hm.cs.fs.app.datastore.model.impl.NewsImpl;
import edu.hm.cs.fs.app.datastore.model.realm.RealmString;
import edu.hm.cs.fs.app.datastore.web.NewsFetcher;
import edu.hm.cs.fs.app.util.PrefUtils;
import io.realm.Realm;

/**
 * Created by Fabio on 03.03.2015.
 */
public class NewsHelper extends BaseHelper implements News {
    private final Person author;
    private final String subject;
    private final String text;
    private final List<Person> teachers;
    private final List<Group> groups;
    private final Date publish;
    private final Date expire;
    private final String url;

    NewsHelper(Context context, NewsImpl news) {
        super(context);
        author = PersonHelper.findById(context, news.getAuthor());
        subject = news.getSubject();
        text = news.getText();
        teachers = new ArrayList<>();
        for (RealmString teacherId : news.getTeachers()) {
            teachers.add(PersonHelper.findById(context, teacherId.getValue()));
        }
        groups = new ArrayList<>();
        for (RealmString groupId : news.getGroups()) {
            groups.add(GroupImpl.of(groupId.getValue()));
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

    public static void listAll(final Context context, final Callback<List<News>> callback) {
        PrefUtils.setUpdateInterval(context, NewsFetcher.class, TimeUnit.MILLISECONDS.convert(1l, TimeUnit.HOURS));

        listAll(context, new NewsFetcher(context), NewsImpl.class, callback, new OnHelperCallback<News, NewsImpl>() {
            @Override
            public News createHelper(Context context, NewsImpl impl) {
                return new NewsHelper(context, impl);
            }

            @Override
            public void copyToRealmOrUpdate(Realm realm, NewsImpl impl) {
                realm.copyToRealmOrUpdate(impl);
            }
        });
    }

    static News findById(final Context context, final String id) {
        return new RealmExecutor<News>(context) {
            @Override
            public News run(final Realm realm) {
                NewsImpl news = realm.where(NewsImpl.class).equalTo("id", id).findFirst();
                if (news == null) {
                    List<NewsImpl> newsList = fetchOnlineData(new NewsFetcher(context), realm, new OnHelperCallback<News, NewsImpl>() {
                        @Override
                        public News createHelper(Context context, NewsImpl impl) {
                            return new NewsHelper(context, impl);
                        }

                        @Override
                        public void copyToRealmOrUpdate(Realm realm, NewsImpl impl) {
                            realm.copyToRealmOrUpdate(impl);
                        }
                    });
                    for (NewsImpl newsImpl : newsList) {
                        if (newsImpl.getId().equals(id)) {
                            news = newsImpl;
                            break;
                        }
                    }
                }
                return new NewsHelper(context, news);
            }
        }.execute();
    }
}
