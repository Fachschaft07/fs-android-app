package edu.hm.cs.fs.app.datastore.web.fetcher.mapping;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;

import edu.hm.cs.fs.app.datastore.model.constants.Letter;
import edu.hm.cs.fs.app.datastore.model.constants.Semester;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.impl.StudyGroup;

/**
 * Created by Fabio on 19.02.2015.
 */
public class StudyGroupManager implements Converter<StudyGroup> {
	@Override
	public void serialize(final StudyGroup item, final ObjectWriter writer,
						  final Context ctx) throws Exception {
		writer.beginObject();

		writer.writeName("study");
		writer.writeValue(item.getStudy().name());

		writer.writeName("letter");
		if (item.getLetter().isPresent()) {
			writer.writeValue(item.getLetter().get().name());
		} else {
			writer.writeNull();
		}

		writer.writeName("semester");
		if (item.getSemester().isPresent()) {
			writer.writeValue(item.getSemester().get().getNumber());
		} else {
			writer.writeNull();
		}

		writer.endObject();
	}

	@Override
	public StudyGroup deserialize(final ObjectReader reader, final Context ctx)
			throws Exception {
		reader.beginObject();

		Study study = null;
		Letter letter = null;
		Semester semester = null;

		while (reader.hasNext()) {
			reader.next();
			final String valueAsString = reader.valueAsString();
			if ("null".equals(valueAsString) || valueAsString == null) {
				reader.skipValue();
				continue;
			}

			if ("study".equals(reader.name())) {
				study = Study.of(valueAsString);
			} else if ("letter".equals(reader.name())) {
				letter = Letter.valueOf(valueAsString);
			} else if ("semester".equals(reader.name())) {
				semester = Semester.of(Integer.parseInt(valueAsString));
			} else {
				reader.skipValue();
			}
		}

		reader.endObject();

		return new StudyGroup(study, semester, letter);
	}
}
