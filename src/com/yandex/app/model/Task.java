package com.yandex.app.model;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;

    private final TaskType type = TaskType.TASK;

    public Task(String name) {
        this.name = name;
        this.status = Status.NEW;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String toString(TaskType type) {
        return String.format("%s,%d,%s,%s,%s", type, id, name != null ? name : "", description != null ? description : "", status != null ? status : "");
    }
    @Override
    public String toString() {
        return toString(type);
    }

    public static Task fromString(String taskString) {
        String[] data = taskString.split(",");

        if (data.length != 5) {
            return null;
        }

        final Task task = new Task(Integer.parseInt(data[1]), data[2], data[3], Status.valueOf(data[4]));
        return task;
    }

    public static TaskType typeFromString(String taskString) {
        String[] data = taskString.split(",");

        return TaskType.valueOf(data[0]);
    }

    public TaskType getType() {
        return type;
    }
}
