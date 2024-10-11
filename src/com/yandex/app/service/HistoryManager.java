package com.yandex.app.service;

import com.yandex.app.model.Task;

public interface HistoryManager {
    void add(Task task);
    Task[] getHistory();
}
