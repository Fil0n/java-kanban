package com.yandex.app.service;

import com.yandex.app.model.Task;
import com.yandex.app.util.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList history = new CustomLinkedList();

    @Override
    public List getHistory() {
        return history.getTasks();
    }

    @Override
    public void add(Task task) {
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(id);
    }

    private class CustomLinkedList {
        private final Map<Integer, Node> map = new HashMap<>();
        private Node tail;
        private Node head;

        public void linkLast(Task task) {
            Node node = new Node();
            node.setTask(task);

            int taskId = task.getId();

            if(map.containsKey(taskId)) {
                removeNode(map.get(taskId));
            }

            if(head == null) {
                head = node;
            } else {
                node.setPrev(tail);
                tail.setNext(node);
            }

            tail = node;

            map.put(taskId, node);
        }

        private void removeNode(Node node) {

            if(node == null) {
                return;
            }

            map.remove(node);

            Node nextNode = node.getNext();
            Node prevNode = node.getPrev();

            if(prevNode == null && nextNode == null) {
                tail = null;
                head = null;
                return;
            }

            if(prevNode != null) {
                prevNode.setNext(nextNode);
            } else {
                head = nextNode;
            }

            if(nextNode != null) {
                nextNode.setPrev(prevNode);
            } else {
                tail = prevNode;
            }

        }

        private void removeNode(int id) {
            removeNode(map.get(id));
        }

        private List<Task> getTasks() {
            List<Task> result = new ArrayList<>();
            Node element = head;
            while (element != null) {
                result.add(element.getTask());
                element = element.getNext();
            }
            return result;
        }
    }

}