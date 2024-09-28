package main.java.practicum.yandex;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name) {
        super(name);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubtasksIds() {
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
        return super.toString() + "Subtasks: " + subtaskIds + "\n";
    }
}
