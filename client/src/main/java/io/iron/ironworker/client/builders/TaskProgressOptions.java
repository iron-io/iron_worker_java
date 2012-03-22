package io.iron.ironworker.client.builders;

public class TaskProgressOptions {
    public static TaskProgressOptionsObject percent(int percent) {
        return (new TaskProgressOptionsObject()).percent(percent);
    }

    public static TaskProgressOptionsObject msg(String msg) {
        return (new TaskProgressOptionsObject()).msg(msg);
    }

    protected TaskProgressOptions() {
    }
}
