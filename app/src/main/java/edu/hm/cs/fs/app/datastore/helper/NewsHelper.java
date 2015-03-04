package edu.hm.cs.fs.app.datastore.helper;

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
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import edu.hm.cs.fs.app.datastore.web.NewsFetcher;
import edu.hm.cs.fs.app.util.NetworkUtils;
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
            groups.add(GroupImpl.of(groupId));
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
    	// Request data from database...
    	new RealmExecutor<List<News>>(context) {
            @Override
            public List<News> run(final Realm realm) {
            	List<News> result = new ArrayList<>();
            	for(NewsImpl news : realm.where(NewsImpl.class).findAll()) {
            		result.add(new NewsHelper(context, news));
            	}
            	return result;
            }
    	}.executeAsync(callback);

    	// Request data from web...
    	if(NetworkUtils.isConnected(context)) {
    		// TODO Don't update every time the device is connected to the internet
        	new RealmExecutor<List<News>>(context) {
                @Override
                public List<News> run(final Realm realm) {
                	List<News> result = new ArrayList<>();
                	List<NewsImpl> newsImplList = fetchOnlineData(context, realm);
                	for(NewsImpl news : newsImplList) {
                		result.add(new NewsHelper(context, news));
                	}
                	return result;
                }
        	}.executeAsync(callback);
    	}
    }

    static News findById(final Context context, final String id) {
        return new RealmExecutor<News>(context) {
            @Override
            public News run(final Realm realm) {
            	NewsImpl news = realm.where(NewsImpl.class).equalTo("id", id).findFirst();
            	if(news == null) {
            		List<NewsImpl> newsList = fetchOnlineData(context, realm);
            		for (NewsImpl newsImpl : newsList) {
						if(newsImpl.getId().equals(id)) {
							news = newsImpl;
							break;
						}
					}
            	}
                return new NewsHelper(context, news);
            }
        }.execute();
    }
    
    private static List<NewsImpl> fetchOnlineData(Context context, Realm realm) {
    	List<NewsImpl> newsList = new NewsFetcher(context).build();
    	for(NewsImpl news : newsList) {
    		// Add to or update database
    		realm.copyToRealmOrUpdate(news);
    	}
    	return newsList;
    }
}
