package com.yandex.app.util;

import com.yandex.app.model.Task;

public class Node {
    private Task task;
    private Node prev;
    private Node next;

    public Node(Task task) {
        setTask(task);
    }

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public Task getTask() {
        return task;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
