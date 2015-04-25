package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.List;
import java.util.Locale;

import edu.hm.cs.fs.app.datastore.model.Lesson;
import edu.hm.cs.fs.app.datastore.model.Module;
import edu.hm.cs.fs.app.datastore.model.ModuleCode;
import edu.hm.cs.fs.app.datastore.model.Person;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;
import edu.hm.cs.fs.app.datastore.model.constants.PersonStatus;
import edu.hm.cs.fs.app.datastore.model.constants.Sex;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.constants.TeachingForm;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.model.impl.LessonImpl;

/**
 * Created by Fabio on 25.04.2015.
 */
public class LessonFk10Helper extends BaseHelper implements Lesson {
    private final Day day;
    private final Time time;
    private final Person teacher;
    private final String room;
    private final Module module;
    private final String suffix;

    LessonFk10Helper(Context context, LessonImpl lesson) {
        super(context);
        day = Day.of(lesson.getDay());
        time = Time.of(lesson.getTime());
        teacher = lesson.getTeacherId() == null ? null : new FakePerson(lesson.getTeacherId());
        room = lesson.getRoom();
        module = lesson.getModuleId() == null ? null : new FakeModule(lesson.getModuleId());
        suffix = lesson.getSuffix();
    }

    @Override
    public Day getDay() {
        return day;
    }

    @Override
    public Time getTime() {
        return time;
    }

    @Override
    public Person getTeacher() {
        return teacher;
    }

    @Override
    public String getRoom() {
        return room;
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    private class FakeModule implements Module {
        private final String name;

        public FakeModule(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getCredits() {
            return 0;
        }

        @Override
        public int getSemesterWeekHours() {
            return 0;
        }

        @Override
        public Person getResponsible() {
            return null;
        }

        @Override
        public List<Person> getTeachers() {
            return null;
        }

        @Override
        public List<Locale> getLanguages() {
            return null;
        }

        @Override
        public TeachingForm getTeachingForm() {
            return null;
        }

        @Override
        public String getExpenditure() {
            return null;
        }

        @Override
        public String getRequirements() {
            return null;
        }

        @Override
        public String getGoals() {
            return null;
        }

        @Override
        public String getContent() {
            return null;
        }

        @Override
        public String getMedia() {
            return null;
        }

        @Override
        public String getLiterature() {
            return null;
        }

        @Override
        public Study getProgram() {
            return null;
        }

        @Override
        public List<ModuleCode> getModulCodes() {
            return null;
        }
    }

    private class FakePerson implements Person {
        private final String firstName;
        private final String lastName;
        private String title;
        private PersonStatus status;

        public FakePerson(String fullName) {
            String[] parts = fullName.split(" ");
            if(parts.length >= 4) {
                title = parts[1];
                status = PersonStatus.PROFESSOR;
                firstName = parts[2];
                lastName = parts[3];
            } else {
                firstName = parts[0];
                lastName = parts[1];
            }
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
            return null;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public Faculty getFaculty() {
            return Faculty._10;
        }

        @Override
        public PersonStatus getStatus() {
            return status;
        }

        @Override
        public boolean isHidden() {
            return false;
        }

        @Override
        public String getEmail() {
            return null;
        }

        @Override
        public String getWebsite() {
            return null;
        }

        @Override
        public String getPhone() {
            return null;
        }

        @Override
        public String getFunction() {
            return null;
        }

        @Override
        public String getFocus() {
            return null;
        }

        @Override
        public String getPublication() {
            return null;
        }

        @Override
        public String getOffice() {
            return null;
        }

        @Override
        public boolean isEmailOptin() {
            return false;
        }

        @Override
        public boolean isReferenceOptin() {
            return false;
        }

        @Override
        public Day getOfficeHourWeekday() {
            return null;
        }

        @Override
        public String getOfficeHourTime() {
            return null;
        }

        @Override
        public String getOfficeHourRoom() {
            return null;
        }

        @Override
        public String getOfficeHourComment() {
            return null;
        }

        @Override
        public String getEinsichtDate() {
            return null;
        }

        @Override
        public String getEinsichtTime() {
            return null;
        }

        @Override
        public String getEinsichtRoom() {
            return null;
        }

        @Override
        public String getEinsichtComment() {
            return null;
        }
    }
}
