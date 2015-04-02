package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.Module;
import edu.hm.cs.fs.app.datastore.model.ModuleCode;
import edu.hm.cs.fs.app.datastore.model.Person;
import edu.hm.cs.fs.app.datastore.model.constants.Semester;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.constants.TeachingForm;
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import edu.hm.cs.fs.app.datastore.model.impl.ModuleCodeImpl;
import edu.hm.cs.fs.app.datastore.model.impl.ModuleImpl;
import edu.hm.cs.fs.app.datastore.model.realm.RealmString;
import edu.hm.cs.fs.app.datastore.web.ModuleFetcher;
import edu.hm.cs.fs.app.util.DataUtils;
import edu.hm.cs.fs.app.util.PrefUtils;
import io.realm.Realm;

/**
 * Created by Fabio on 03.03.2015.
 */
public class ModuleHelper extends BaseHelper implements Module {
    private static final String TAG = ModuleHelper.class.getSimpleName();
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

    ModuleHelper(Context context, ModuleImpl module) {
        super(context);
        name = module.getName();
        credits = module.getCredits();
        sws = module.getSws();
        responsible = PersonHelper.findById(context, module.getResponsible());
        teachers = new ArrayList<>();
        for (RealmString teacherId : module.getTeachers()) {
            teachers.add(PersonHelper.findById(context, teacherId.getValue()));
        }
        languages = new ArrayList<>();
        for (RealmString realmString : module.getLanguages()) {
            languages.add(DataUtils.toLocale(realmString.getValue()));
        }
        teachingForm = TeachingForm.of(module.getTeachingForm());
        expenditure = module.getExpenditure();
        requirements = module.getRequirements();
        goals = module.getGoals();
        content = module.getContent();
        media = module.getMedia();
        literatur = module.getLiterature();
        if(module.getProgram() != null && GroupImpl.of(module.getProgram()) != null) {
            program = GroupImpl.of(module.getProgram()).getStudy();
        } else {
            program = null;
        }
        moduleCodes = new ArrayList<>();
        for (ModuleCodeImpl moduleCode : module.getModulCodes()) {
            moduleCodes.add(new ModuleCodeHelper(context, moduleCode));
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

    public static void getModulesByGroups(final Context context, final List<Group> groups, final Callback<List<Module>> callback) {
        listAll(context, new Callback<List<Module>>() {
            @Override
            public void onResult(final List<Module> result) {
                List<Module> filtered = new ArrayList<Module>();
                for (Module module : result) {
                    if(groups.contains(GroupImpl.of(module.getProgram().toString()))) {
                        filtered.add(module);
                    } else {
                        for (ModuleCode moduleCode : module.getModulCodes()) {
                            final String study = moduleCode.getCode().substring(0, 1);
                            for (Semester semester : moduleCode.getSemester()) {
                                if(groups.contains(GroupImpl.of(study + semester.toString()))) {
                                    filtered.add(module);
                                }
                            }
                        }
                    }
                }

                callback.onResult(filtered);
            }
        });
    }

    public static void listAll(final Context context, final Callback<List<Module>> callback) {
        PrefUtils.setUpdateInterval(context, ModuleFetcher.class, TimeUnit.MILLISECONDS.convert(30l, TimeUnit.DAYS));

        listAll(context, new ModuleFetcher(context), ModuleImpl.class, callback, new OnHelperCallback<Module, ModuleImpl>() {
            @Override
            public Module createHelper(Context context, ModuleImpl impl) {
                return new ModuleHelper(context, impl);
            }

            @Override
            public void copyToRealmOrUpdate(Realm realm, ModuleImpl impl) {
                realm.copyToRealmOrUpdate(impl);
                for (ModuleCodeImpl moduleCode : impl.getModulCodes()) {
                    realm.copyToRealmOrUpdate(moduleCode);
                }
            }
        });
    }

    static Module findById(final Context context, final String id) {
        return new RealmExecutor<Module>(context) {
            @Override
            public Module run(final Realm realm) {
                Log.d(TAG, "Search for " + id);
                ModuleImpl module = realm.where(ModuleImpl.class).equalTo("id", id).findFirst();
                if (module == null) {
                    Log.d(TAG, id + " not found -> search in web");
                    List<ModuleImpl> moduleList = fetchOnlineData(new ModuleFetcher(context, id), realm, false, new OnHelperCallback<Module, ModuleImpl>() {
                        @Override
                        public Module createHelper(Context context, ModuleImpl impl) {
                            return new ModuleHelper(context, impl);
                        }

                        @Override
                        public void copyToRealmOrUpdate(Realm realm, ModuleImpl impl) {
                            realm.copyToRealmOrUpdate(impl);
                            for (ModuleCodeImpl moduleCode : impl.getModulCodes()) {
                                realm.copyToRealmOrUpdate(moduleCode);
                            }
                        }
                    });
                    if(!moduleList.isEmpty()) {
                        module = moduleList.get(0);
                        Log.d(TAG, "Module: " + module.getName());
                    } else {
                        Log.d(TAG, "Module not found");
                    }
                } else {
                    Log.d(TAG, "Module: " + module.getName());
                }
                return new ModuleHelper(context, module);
            }
        }.execute();
    }
}
