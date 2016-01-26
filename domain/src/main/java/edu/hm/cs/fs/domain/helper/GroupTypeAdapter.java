package edu.hm.cs.fs.domain.helper;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import edu.hm.cs.fs.common.model.Group;

public class GroupTypeAdapter extends TypeAdapter<Group> {
    @Override
    public void write(JsonWriter out, Group value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public Group read(JsonReader in) throws IOException {
        return Group.of(in.nextString());
    }
}
