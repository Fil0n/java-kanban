package com.yandex.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
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
        return String.format("%s,%d,%s,%s", TaskType.EPIC,
                getId(),
                getName() != null ? getName() : " ",
                getDescription() != null ? getDescription() : " ");
    }

    public static Task fromString(String taskString) {
        String[] data = taskString.split(",");
        final int parsingParamsCount = 4;

        if (data.length != parsingParamsCount) {
            return null;
        }

        return new Epic(Integer.parseInt(data[1]),
                data[2],
                data[3]);
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.getCurrentEndTime();
    }

    public void setEndTimeFromSubtask(LocalDateTime endTime) {
        this.setEndTime(endTime);
    }
}
