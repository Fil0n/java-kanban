package com.yandex.app.model;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;

    public Task(String name) {
        this.name = name;
        this.status = Status.NEW;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
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

    @Override
    public String toString() {
        return String.format("id: %d \n name: %s \n description: %s \n status: %s \n", id, name != null ? name : "", description != null ? description : "", status != null ? status : "");
    }
}
