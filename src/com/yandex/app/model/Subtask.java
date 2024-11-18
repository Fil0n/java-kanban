package com.yandex.app.model;

public class Subtask extends Task {
    private final int epicId;
    private final TaskType type = TaskType.SUBTASK;
    private static final int parsingParamsCount = 6;

    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, int epicId) {
        super(name);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String s = super.toString(type);
        return String.format("%s,%d", s, epicId);
    }

    public static Task fromString(String taskString) {
        String[] data = taskString.split(",");

        if (data.length != parsingParamsCount) {
            return null;
        }

        return new Subtask(Integer.parseInt(data[1]), data[2], data[3], Status.valueOf(data[4]), Integer.parseInt(data[5]));
    }
}
