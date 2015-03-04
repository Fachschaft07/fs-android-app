package edu.hm.cs.fs.app.datastore.model;

import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;
import edu.hm.cs.fs.app.datastore.model.constants.PersonStatus;
import edu.hm.cs.fs.app.datastore.model.constants.Sex;

/**
 * Created by Fabio on 03.03.2015.
 */
public interface Person {
    String getLastName();

    String getFirstName();

    Sex getSex();

    String getTitle();

    Faculty getFaculty();

    PersonStatus getStatus();

    boolean isHidden();

    String getEmail();

    String getWebsite();

    String getPhone();
    	
    String getFunction();

    String getFocus();

    String getPublication();

    String getOffice();

    boolean isEmailOptin();

    boolean isReferenceOptin();

    Day getOfficeHourWeekday();

    String getOfficeHourTime();

    String getOfficeHourRoom();

    String getOfficeHourComment();

    String getEinsichtDate();

    String getEinsichtTime();

    String getEinsichtRoom();

    String getEinsichtComment();
}
