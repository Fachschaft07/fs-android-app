package edu.hm.cs.fs.app.datastore.model;

import com.google.common.base.Optional;

import edu.hm.cs.fs.app.datastore.model.constants.Letter;
import edu.hm.cs.fs.app.datastore.model.constants.Semester;
import edu.hm.cs.fs.app.datastore.model.constants.Study;

/**
 * Created by Fabio on 03.03.2015.
 */
public interface Group {
    /**
     * @return the study group.
     */
    Study getStudy();

    /**
     * @return the semester.
     */
    Optional<Semester> getSemester();

    /**
     * @return the letter.
     */
    Optional<Letter> getLetter();
}
