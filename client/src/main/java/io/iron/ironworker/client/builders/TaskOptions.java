package io.iron.ironworker.client.builders;

import java.util.HashMap;
import java.util.Map;

public class TaskOptions {
    private Map<String, Object> options;

    public TaskOptions() {
        options = new HashMap<String, Object>();
    }

    public TaskOptions priority(int priority) {
        options.put("priority", priority);

        return this;
    }

    public TaskOptions timeout(int timeout) {
        options.put("timeout", timeout);

        return this;
    }

    public TaskOptions delay(int delay) {
        options.put("delay", delay);

        return this;
    }

    public Map<String, Object> create() {
        return options;
    }
}
