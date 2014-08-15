package io.iron.ironworker.client.entities;

public class TaskIds {
    private TaskEntity[] tasks;

    public String[] getIds() {
        String[] result = new String[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            result[i] = tasks[i].getId();
        }
        return result;
    }
}
