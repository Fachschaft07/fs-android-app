package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.datastore.model.Person;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;
import edu.hm.cs.fs.app.datastore.model.constants.PersonStatus;
import edu.hm.cs.fs.app.datastore.model.constants.Sex;
import edu.hm.cs.fs.app.datastore.model.impl.PersonImpl;
import edu.hm.cs.fs.app.datastore.web.PersonFetcher;
import edu.hm.cs.fs.app.util.NetworkUtils;
import io.realm.Realm;

/**
 * Created by Fabio on 03.03.2015.
 */
public class PersonHelper implements Person {
    private final Context mContext;
	private String lastName;
	private String firstName;
	private Sex sex;
	private String title;
	private Faculty faculty;
	private PersonStatus status;
	private boolean hidden;
	private String email;
	private String website;
	private String phone;
	private String function;
	private String focus;
	private String publication;
	private String office;
	private boolean emailOptin;
	private boolean referenceOptin;
	private Day officeHourWeekday;
	private String officeHourTime;
	private String officeHourRoom;
	private String officeHourComment;
	private String einsichtDate;
	private String einsichtTime;
	private String einsichtRoom;
	private String einsichtComment;

    private PersonHelper(Context context, PersonImpl person) {
        mContext = context;
        lastName = person.getLastName();
        firstName = person.getFirstName();
        sex = person.getSex();
        title = person.getTitle();
        faculty = person.getFaculty();
        status = person.getStatus();
        hidden = person.isHidden();
        email = person.getEmail();
        website = person.getWebsite();
        phone = person.getPhone();
        function = person.getFunction();
        focus = person.getFocus();
        publication = person.getPublication();
        office = person.getOffice();
        emailOptin = person.isEmailOptin();
        referenceOptin = person.isReferenceOptin();
        officeHourWeekday = person.getOfficeHourWeekday();
        officeHourTime = person.getOfficeHourTime();
        officeHourRoom = person.getOfficeHourRoom();
        officeHourComment = person.getOfficeHourComment();
        einsichtDate = person.getEinsichtDate();
        einsichtTime = person.getEinsichtTime();
        einsichtRoom = person.getEinsichtRoom();
        einsichtComment = person.getEinsichtComment();
    }

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public Sex getSex() {
		return sex;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Faculty getFaculty() {
		return faculty;
	}

	@Override
	public PersonStatus getStatus() {
		return status;
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getWebsite() {
		return website;
	}

	@Override
	public String getPhone() {
		return phone;
	}

	@Override
	public String getFunction() {
		return function;
	}

	@Override
	public String getFocus() {
		return focus;
	}

	@Override
	public String getPublication() {
		return publication;
	}

	@Override
	public String getOffice() {
		return office;
	}

	@Override
	public boolean isEmailOptin() {
		return emailOptin;
	}

	@Override
	public boolean isReferenceOptin() {
		return referenceOptin;
	}

	@Override
	public Day getOfficeHourWeekday() {
		return officeHourWeekday;
	}

	@Override
	public String getOfficeHourTime() {
		return officeHourTime;
	}

	@Override
	public String getOfficeHourRoom() {
		return officeHourRoom;
	}

	@Override
	public String getOfficeHourComment() {
		return officeHourComment;
	}

	@Override
	public String getEinsichtDate() {
		return einsichtDate;
	}

	@Override
	public String getEinsichtTime() {
		return einsichtTime;
	}

	@Override
	public String getEinsichtRoom() {
		return einsichtRoom;
	}

	@Override
	public String getEinsichtComment() {
		return einsichtComment;
	}
    
    public static void listAll(final Context context, final Callback<List<Person>> callback) {
    	// Request data from database...
    	new RealmExecutor<List<Person>>(context) {
            @Override
            public List<Person> run(final Realm realm) {
            	List<Person> result = new ArrayList<>();
            	for(PersonImpl person : realm.where(PersonImpl.class).findAll()) {
            		result.add(new PersonHelper(context, person));
            	}
            	return result;
            }
    	}.executeAsync(callback);

    	// Request data from web...
    	if(NetworkUtils.isConnected(context)) {
    		// TODO Don't update every time the device is connected to the internet
        	new RealmExecutor<List<Person>>(context) {
                @Override
                public List<Person> run(final Realm realm) {
                	List<Person> result = new ArrayList<>();
                	List<PersonImpl> personImplList = fetchOnlineData(context, realm);
                	for(PersonImpl person : personImplList) {
                		result.add(new PersonHelper(context, person));
                	}
                	return result;
                }
        	}.executeAsync(callback);
    	}
    }

    static Person findById(final Context context, final String id) {
        return new RealmExecutor<Person>(context) {
            @Override
            public Person run(final Realm realm) {
            	PersonImpl person = realm.where(PersonImpl.class).equalTo("id", id).findFirst();
            	if(person == null) {
            		List<PersonImpl> personList = fetchOnlineData(context, realm);
            		for (PersonImpl personImpl : personList) {
						if(personImpl.getId().equals(id)) {
							person = personImpl;
							break;
						}
					}
            	}
                return new PersonHelper(context, person);
            }
        }.execute();
    }
    
    private static List<PersonImpl> fetchOnlineData(Context context, Realm realm) {
    	List<PersonImpl> personList = new PersonFetcher(context).fetch();
    	for(PersonImpl person : personList) {
    		// Add to or update database
    		realm.copyToRealmOrUpdate(person);
    	}
    	return personList;
    }
}
