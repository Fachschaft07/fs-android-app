package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.datastore.model.Exam;
import edu.hm.cs.fs.app.datastore.model.Module;
import edu.hm.cs.fs.app.datastore.model.Person;
import edu.hm.cs.fs.app.datastore.model.constants.ExamGroup;
import edu.hm.cs.fs.app.datastore.model.constants.ExamType;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.impl.ExamImpl;
import edu.hm.cs.fs.app.datastore.web.ExamFetcher;
import edu.hm.cs.fs.app.util.NetworkUtils;
import io.realm.Realm;

/**
 * Created by Fabio on 03.03.2015.
 */
public class ExamHelper implements Exam {
    private final Context mContext;
	private String code;
	private Module module;
	private Study group;
	private String subtitle;
	private List<Study> references;
	private List<Person> examiners;
	private ExamType type;
	private String material;
	private ExamGroup allocation;

    private ExamHelper(Context context, ExamImpl exam) {
        mContext = context;
        code = exam.getCode();
        module = ModuleHelper.findById(context, exam.getModule());
        group = exam.getGroup();
        subtitle = exam.getSubtitle();
        references = exam.getReferences();
        examiners = new ArrayList<>();
        for(String personId : exam.getExaminers()) {
        	examiners.add(PersonHelper.findById(context, personId));
        }
        type = exam.getType();
        material = exam.getMaterial();
        allocation = exam.getAllocation();
    }

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public Study getGroup() {
		return group;
	}

	@Override
	public Module getModule() {
		return module;
	}

	@Override
	public String getSubtitle() {
		return subtitle;
	}

	@Override
	public List<Study> getReferences() {
		return references;
	}

	@Override
	public List<Person> getExaminers() {
		return examiners;
	}

	@Override
	public ExamType getType() {
		return type;
	}

	@Override
	public String getMaterial() {
		return material;
	}

	@Override
	public ExamGroup getAllocation() {
		return allocation;
	}
    
    public static void listAll(final Context context, final Callback<List<Exam>> callback) {
    	// Request data from database...
    	new RealmExecutor<List<Exam>>(context) {
            @Override
            public List<Exam> run(final Realm realm) {
            	List<Exam> result = new ArrayList<>();
            	for(ExamImpl exam : realm.where(ExamImpl.class).findAll()) {
            		result.add(new ExamHelper(context, exam));
            	}
            	return result;
            }
    	}.executeAsync(callback);

    	// Request data from web...
    	if(NetworkUtils.isConnected(context)) {
    		// TODO Don't update every time the device is connected to the internet
        	new RealmExecutor<List<Exam>>(context) {
                @Override
                public List<Exam> run(final Realm realm) {
                	List<Exam> result = new ArrayList<>();
                	List<ExamImpl> examImplList = fetchOnlineData(context, realm);
                	for(ExamImpl exam : examImplList) {
                		result.add(new ExamHelper(context, exam));
                	}
                	return result;
                }
        	}.executeAsync(callback);
    	}
    }

    static Exam findById(final Context context, final String id) {
        return new RealmExecutor<Exam>(context) {
            @Override
            public Exam run(final Realm realm) {
            	ExamImpl exam = realm.where(ExamImpl.class).equalTo("id", id).findFirst();
            	if(exam == null) {
            		List<ExamImpl> examList = fetchOnlineData(context, realm);
            		for (ExamImpl examImpl : examList) {
						if(examImpl.getId().equals(id)) {
							exam = examImpl;
							break;
						}
					}
            	}
                return new ExamHelper(context, exam);
            }
        }.execute();
    }
    
    private static List<ExamImpl> fetchOnlineData(Context context, Realm realm) {
    	List<ExamImpl> examList = new ExamFetcher(context).fetch();
    	for(ExamImpl exam : examList) {
    		// Add to or update database
    		realm.copyToRealmOrUpdate(exam);
    	}
    	return examList;
    }
}
