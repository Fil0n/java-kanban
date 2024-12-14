package com.yandex.app.model;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer duration, String startTime, int epicId) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, Status status, Integer duration, String startTime, int epicId) {
        super(id, name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer duration, LocalDateTime startTime, int epicId) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, Status status, Integer duration, LocalDateTime startTime, int epicId) {
        super(id, name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String s = super.toString(TaskType.SUBTASK);
        return String.format("%s,%d", s, epicId);
    }

    public static Task fromString(String taskString) {
        String[] data = taskString.split(",");
        final int parsingParamsCount = 8;

        if (data.length != parsingParamsCount) {
            return null;
        }

        return new Subtask(Integer.parseInt(data[1]),
                data[2],
                data[3],
                Status.valueOf(data[4]),
                data[5].isBlank() ? null : Integer.parseInt(data[5]),
                data[6].isBlank() ? null : data[6],
                Integer.parseInt(data[7]));
    }
}
