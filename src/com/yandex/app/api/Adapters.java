package com.yandex.app.api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.yandex.app.model.Status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Adapters {
    public static LocalDateTimeAdapter localDateTimeAdapter = new LocalDateTimeAdapter();
    public static statusAdapter statusAdapter = new statusAdapter();
}

class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime LocalDateTime) throws IOException {
        if(LocalDateTime == null){
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(LocalDateTime.format(timeFormatter));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == null) {
            jsonReader.nextNull();
            return null;
        } else {
            return LocalDateTime.parse(jsonReader.nextString(), timeFormatter);
        }
    }
}

class statusAdapter extends TypeAdapter<Status> {

    @Override
    public void write(JsonWriter jsonWriter, Status status) throws IOException {
        jsonWriter.value(status.name());
    }

    @Override
    public Status read(JsonReader jsonReader) throws IOException {
        Status s = Status.valueOf(jsonReader.nextString().toString());
        return s;
    }
}
