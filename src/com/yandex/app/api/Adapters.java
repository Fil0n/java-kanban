package com.yandex.app.api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Adapters {
    public static LocalDateTimeAdapter localDateTimeAdapter = new LocalDateTimeAdapter();
}

class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime LocalDateTime) throws IOException {
        if(LocalDateTime == null){
            jsonWriter.value("");
            return;
        }
        jsonWriter.value(LocalDateTime.format(timeFormatter));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) {
        return LocalDateTime.parse(jsonReader.toString(), timeFormatter);
    }
}
