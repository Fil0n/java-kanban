package com.yandex.app.service;

import com.yandex.app.model.Task;

public class InMemoryHistoryManager implements HistoryManager{
    private final Task[] history = new Task[10];
    private int historyCount = 0;
    @Override
    public Task[] getHistory() {
        return history;
    }

    @Override
    public void add(Task task){
        if (historyCount > 8){
            for (int i = 0; i < historyCount; i++) {
                history[i] = history[i+1];
            }
        }

        history[historyCount] = task;
        historyCount++;
    }
}
