package main.java.practicum.yandex;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public ArrayList<Integer> getSubtasksIds() {
        return subtaskIds;
    }

    public void putSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        for (int i = 0; i < subtaskIds.size(); i++) {
            if (subtaskIds.get(i) == subtaskId) {
                subtaskIds.remove(i);
                return;
            }
        }
    }

    public void cleanSubtasksIds() {
        subtaskIds = new ArrayList<>();
    }

    @Override
    public String toString() {
        String s = super.toString();
        s += "Subtasks: ";
        for (int subtaskId : subtaskIds) {
            s += subtaskId + ", ";
        }

        s += "\n";
        return s;
    }
}
