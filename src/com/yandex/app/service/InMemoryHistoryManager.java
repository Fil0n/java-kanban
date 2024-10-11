package com.yandex.app.service;

import com.yandex.app.model.Task;

public class InMemoryHistoryManager implements HistoryManager{
    private final int HISTORY_MAX_COUNT = 10;
    private final Task[] history = new Task[HISTORY_MAX_COUNT];
    private int historyCount = -1;
    @Override
    public Task[] getHistory() {
        return history;
    }

    @Override
    public void add(Task task){
        if (historyCount == (HISTORY_MAX_COUNT-1)){
            for (int i = 0; i < historyCount; i++) {
                history[i] = history[i+1];
            }
        } else {
            historyCount++;
        }

        history[historyCount] = task;
    }
}
