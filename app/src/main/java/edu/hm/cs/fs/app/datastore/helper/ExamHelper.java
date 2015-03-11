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
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import edu.hm.cs.fs.app.datastore.model.realm.RealmString;
import edu.hm.cs.fs.app.datastore.web.ExamFetcher;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Fabio on 03.03.2015.
 */
public class ExamHelper extends BaseHelper implements Exam {
	private String code;
	private Module module;
	private Study group;
	private String subtitle;
	private List<Study> references;
	private List<Person> examiners;
	private ExamType type;
	private String material;
	private ExamGroup allocation;

    ExamHelper(Context context, ExamImpl exam) {
        super(context);
        code = exam.getCode();
        module = ModuleHelper.findById(context, exam.getModule());
        group = GroupImpl.of(exam.getGroup()).getStudy();
        subtitle = exam.getSubtitle();
        references = new ArrayList<>();
        final RealmList<RealmString> referencesRealm = exam.getReferences();
        for (RealmString realmString : referencesRealm) {
            references.add(GroupImpl.of(realmString.getValue()).getStudy());
        }
        examiners = new ArrayList<>();
        for(RealmString personId : exam.getExaminers()) {
        	examiners.add(PersonHelper.findById(context, personId.getValue()));
        }
        type = ExamType.of(exam.getType());
        material = exam.getMaterial();
        allocation = ExamGroup.of(exam.getAllocation());
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
    	listAll(context, new ExamFetcher(context), ExamImpl.class, callback, new OnHelperCallback<Exam, ExamImpl>() {
			@Override
			public Exam createHelper(Context context, ExamImpl impl) {
				return new ExamHelper(context, impl);
			}

			@Override
			public void copyToRealmOrUpdate(Realm realm, ExamImpl impl) {
	    		realm.copyToRealmOrUpdate(impl);
			}
		});
    }

    static Exam findById(final Context context, final String id) {
        return new RealmExecutor<Exam>(context) {
            @Override
            public Exam run(final Realm realm) {
            	ExamImpl exam = realm.where(ExamImpl.class).equalTo("id", id).findFirst();
            	if(exam == null) {
            		List<ExamImpl> examList = fetchOnlineData(new ExamFetcher(context), realm, new OnHelperCallback<Exam, ExamImpl>() {
            			@Override
            			public Exam createHelper(Context context, ExamImpl impl) {
            				return new ExamHelper(context, impl);
            			}

            			@Override
            			public void copyToRealmOrUpdate(Realm realm, ExamImpl impl) {
            	    		realm.copyToRealmOrUpdate(impl);
            			}
            		});
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
}
