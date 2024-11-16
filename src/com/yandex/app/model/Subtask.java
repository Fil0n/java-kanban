package com.yandex.app.model;

public class Subtask extends Task {
    private final int epicId;

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
        String s = super.toString();
        return String.format("%s,%d", s, epicId);
    }

    @Override
    public Task fromString(String taskString){
        String[] data = taskString.split(",");

        if(data.length != 5) {
            return null;
        }

        return new Subtask(Integer.parseInt(data[0]), data[1], data[2], Status.valueOf(data[3]), Integer.parseInt(data[4]));
    }
}
