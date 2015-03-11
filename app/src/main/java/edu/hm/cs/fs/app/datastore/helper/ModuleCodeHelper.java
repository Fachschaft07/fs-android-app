package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.datastore.model.ModuleCode;
import edu.hm.cs.fs.app.datastore.model.constants.Offer;
import edu.hm.cs.fs.app.datastore.model.constants.Semester;
import edu.hm.cs.fs.app.datastore.model.impl.ModuleCodeImpl;
import edu.hm.cs.fs.app.datastore.model.realm.RealmString;

/**
 * Created by Fabio on 07.03.2015.
 */
public class ModuleCodeHelper extends BaseHelper implements ModuleCode {
    private final String code;
    private final String curriculum;
    private final Offer offer;
    private final String regulation;
    private final List<Semester> semester;
    private final String services;

    public ModuleCodeHelper(final Context context, final ModuleCodeImpl moduleCode) {
        super(context);
        code = moduleCode.getCode();
        curriculum = moduleCode.getCurriculum();
        offer = Offer.of(moduleCode.getOffer());
        regulation = moduleCode.getRegulation();
        semester = new ArrayList<>();
        for (RealmString realmString : moduleCode.getSemesters()) {
            semester.add(Semester.of(Integer.parseInt(realmString.getValue())));
        }
        services = moduleCode.getServices();
    }

    @Override
    public String getRegulation() {
        return regulation;
    }

    @Override
    public Offer getOffer() {
        return offer;
    }

    @Override
    public String getServices() {
        return services;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public List<Semester> getSemester() {
        return semester;
    }

    @Override
    public String getCurriculum() {
        return curriculum;
    }
}
