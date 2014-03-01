package io.iron.ironworker.client.builders;

public class TaskOptions {
    public static TaskOptionsObject priority(int priority) {
        return (new TaskOptionsObject()).priority(priority);
    }

    public static TaskOptionsObject timeout(int timeout) {
        return (new TaskOptionsObject()).timeout(timeout);
    }

    public static TaskOptionsObject delay(int delay) {
        return (new TaskOptionsObject()).delay(delay);
    }

    protected TaskOptions() {
    }
}
