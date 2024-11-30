package com.yandex.app.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();
    private final TaskType type = TaskType.EPIC;

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);

        this.subtaskIds.clear();

        for (Integer subtaskId : subtaskIds) {
            this.subtaskIds.add(subtaskId);
        }
    }

    public Epic(int id, String name, String description, Status status, int duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);

        this.subtaskIds.clear();

        for (Integer subtaskId : subtaskIds) {
            this.subtaskIds.add(subtaskId);
        }
    }

    public Epic(int id, String name, String description, Status status, LocalDateTime startTime) {
        super(id, name, description, status, startTime);

        this.subtaskIds.clear();

        for (Integer subtaskId : subtaskIds) {
            this.subtaskIds.add(subtaskId);
        }
    }

    public Epic(int id, String name, String description, Status status, int duration) {
        super(id, name, description, status, duration);

        this.subtaskIds.clear();

        for (Integer subtaskId : subtaskIds) {
            this.subtaskIds.add(subtaskId);
        }
    }

    public Epic(String name) {
        super(name);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Integer> getSubtasksIds() {
        return subtaskIds;
    }

    public void putSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void cleanSubtasksIds() {
        subtaskIds.clear();
    }

    @Override
    public String toString() {
        return super.toString(type);
    }

    public static Task fromString(String taskString) {
        String[] data = taskString.split(",");
        final int parsingParamsCount = 7;

        if (data.length != parsingParamsCount) {
            return null;
        }

        return new Epic(Integer.parseInt(data[1]), data[2], data[3], Status.valueOf(data[4]), data[5].isBlank() ? null : Integer.parseInt(data[5]), data[6].isBlank() ? null : LocalDateTime.parse(data[6]));

    }

}
