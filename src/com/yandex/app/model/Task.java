package com.yandex.app.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private Integer duration;
    private LocalDateTime startTime;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private LocalDateTime endTime;

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

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Task(int id, String name, String description, Status status, Integer duration, LocalDateTime startTime) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        setEndTime();
    }

    public Task(int id, String name, String description, Status status, Integer duration, String startTime) {
        System.out.println(startTime);
        this.id = id;
        this.status = status;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = (startTime == null) ? null : LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        setEndTime();
    }

    public Task(int id, String name, String description, Status status, LocalDateTime startTime) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        setEndTime();
    }

    public Task(int id, String name, String description, Status status, String startTime) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        setEndTime();
    }

    public Task(int id, String name, String description, Status status, Integer duration) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.description = description;
        this.duration = duration;
    }

    public Task(String name, String description, Integer duration) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
    }

    public Task(String name, String description, Integer duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
        setEndTime();
    }

    public Task(String name, String description, Integer duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        setEndTime();
    }

    public Task(String name, String description, String status, Integer duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = Status.valueOf(status);
        this.duration = duration;
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        setEndTime();
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
        return String.format("%s,%d,%s,%s,%s,%s,%s", type,
                id,
                name != null ? name : " ",
                description != null ? description : " ",
                status != null ? status : " ",
                duration != null ? duration : " ",
                startTime != null ? startTime.format(DATE_TIME_FORMATTER) : " ");
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return toString(TaskType.TASK);
    }

    public static Task fromString(String taskString) {
        String[] data = taskString.split(",");
        final int parsingParamsCount = 7;

        if (data.length != parsingParamsCount) {
            return null;
        }


        final Task task = new Task(Integer.parseInt(data[1]),
                data[2],
                data[3],
                Status.valueOf(data[4]),
                data[5].isBlank() ? null : Integer.parseInt(data[5]),
                data[6].isBlank() ? null : LocalDateTime.parse(data[6], DATE_TIME_FORMATTER));

        return task;
    }

    public static TaskType typeFromString(String taskString) {
        String[] data = taskString.split(",");

        return TaskType.valueOf(data[0]);
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        setEndTime();
        return endTime;
    }

    public LocalDateTime getCurrentEndTime() {
        return this.endTime;
    }

    public void setEndTime() {
        endTime = duration == null ? startTime : startTime.plus(Duration.ofMinutes(duration == null ? 0 : duration));
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
