package main.java.practicum.yandex;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, int mainTaskId) {
        super(name);
        this.epicId = mainTaskId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getMainTaskId() {
        return epicId;
    }
}
