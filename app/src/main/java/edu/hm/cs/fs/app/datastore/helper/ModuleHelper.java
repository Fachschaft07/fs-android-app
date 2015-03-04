package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.hm.cs.fs.app.datastore.model.Module;
import edu.hm.cs.fs.app.datastore.model.ModuleCode;
import edu.hm.cs.fs.app.datastore.model.Person;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.constants.TeachingForm;
import edu.hm.cs.fs.app.datastore.model.impl.ModuleCodeImpl;
import edu.hm.cs.fs.app.datastore.model.impl.ModuleImpl;
import edu.hm.cs.fs.app.datastore.web.ModuleFetcher;
import edu.hm.cs.fs.app.util.NetworkUtils;
import io.realm.Realm;

/**
 * Created by Fabio on 03.03.2015.
 */
public class ModuleHelper implements Module {
    private final Context mContext;
    private String name;
    private int credits;
    private int sws;
    private Person responsible;
    private List<Person> teachers;
    private List<Locale> languages;
    private TeachingForm teachingForm;
    private String expenditure;
    private String requirements;
    private String goals;
    private String content;
    private String media;
    private String literatur;
    private Study program;
    private List<ModuleCode> moduleCodes;

    private ModuleHelper(Context context, ModuleImpl module) {
        mContext = context;
        name = module.getName();
        credits = module.getCredits();
        sws = module.getSws();
        responsible = PersonHelper.findById(context, module.getResponsible());
        teachers = new ArrayList<>();
        for (String teacherId : module.getTeachers()) {
			teachers.add(PersonHelper.findById(context, teacherId));
		}
        languages = module.getLanguages();
        teachingForm = module.getTeachingForm();
        expenditure = module.getExpenditure();
        requirements = module.getRequirements();
        goals = module.getGoals();
        content = module.getContent();
        media = module.getMedia();
        literatur = module.getLiterature();
        program = module.getProgram();
        moduleCodes = new ArrayList<>();
        for (ModuleCodeImpl moduleCode : module.getModulCodes()) {
			moduleCodes.add(moduleCode);
		}
    }

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getCredits() {
		return credits;
	}

	@Override
	public int getSemesterWeekHours() {
		return sws;
	}

	@Override
	public Person getResponsible() {
		return responsible;
	}

	@Override
	public List<Person> getTeachers() {
		return teachers;
	}

	@Override
	public List<Locale> getLanguages() {
		return languages;
	}

	@Override
	public TeachingForm getTeachingForm() {
		return teachingForm;
	}

	@Override
	public String getExpenditure() {
		return expenditure;
	}

	@Override
	public String getRequirements() {
		return requirements;
	}

	@Override
	public String getGoals() {
		return goals;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public String getMedia() {
		return media;
	}

	@Override
	public String getLiterature() {
		return literatur;
	}

	@Override
	public Study getProgram() {
		return program;
	}

	@Override
	public List<ModuleCode> getModulCodes() {
		return moduleCodes;
	}

    public static void listAll(final Context context, final Callback<List<Module>> callback) {
    	// Request data from database...
    	new RealmExecutor<List<Module>>(context) {
            @Override
            public List<Module> run(final Realm realm) {
            	List<Module> result = new ArrayList<>();
            	for(ModuleImpl module : realm.where(ModuleImpl.class).findAll()) {
            		result.add(new ModuleHelper(context, module));
            	}
            	return result;
            }
    	}.executeAsync(callback);

    	// Request data from web...
    	if(NetworkUtils.isConnected(context)) {
    		// TODO Don't update every time the device is connected to the internet
        	new RealmExecutor<List<Module>>(context) {
                @Override
                public List<Module> run(final Realm realm) {
                	List<Module> result = new ArrayList<>();
                	List<ModuleImpl> moduleImplList = fetchOnlineData(context, realm);
                	for(ModuleImpl module : moduleImplList) {
                		result.add(new ModuleHelper(context, module));
                	}
                	return result;
                }
        	}.executeAsync(callback);
    	}
    }

    static Module findById(final Context context, final String id) {
        return new RealmExecutor<Module>(context) {
            @Override
            public Module run(final Realm realm) {
            	ModuleImpl module = realm.where(ModuleImpl.class).equalTo("id", id).findFirst();
            	if(module == null) {
            		List<ModuleImpl> moduleList = fetchOnlineData(context, realm);
            		for (ModuleImpl moduleImpl : moduleList) {
						if(moduleImpl.getId().equals(id)) {
							module = moduleImpl;
							break;
						}
					}
            	}
                return new ModuleHelper(context, module);
            }
        }.execute();
    }
    
    private static List<ModuleImpl> fetchOnlineData(Context context, Realm realm) {
    	List<ModuleImpl> moduleList = new ModuleFetcher(context).fetch();
    	for(ModuleImpl module : moduleList) {
    		// Add to or update database
    		realm.copyToRealmOrUpdate(module);
    		for (ModuleCodeImpl moduleCode : module.getModulCodes()) {
        		realm.copyToRealmOrUpdate(moduleCode);
			}
    	}
    	return moduleList;
    }
}
