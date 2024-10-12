package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryHistoryManager implements HistoryManager {
    private final int HISTORY_MAX_COUNT = 10;
    private final Task[] history = new Task[HISTORY_MAX_COUNT];
    private int historyCount = -1;

    @Override
    public List<Task> getHistory() {
        return Arrays.asList(history) //TODOЖ Пока полноценно не понимаю, как с этим работать, надо поразбираться
                .stream()
                .filter(item -> !(item == null))
                .collect(Collectors.toList());
    }

    @Override
    public void add(Task task) {

        if (task == null) {
            return;
        }

        if (historyCount == (HISTORY_MAX_COUNT - 1)) {
            for (int i = 0; i < historyCount; i++) {
                history[i] = history[i + 1];
            }
        } else {
            historyCount++;
        }

        history[historyCount] = task;
    }
}
