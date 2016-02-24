package io.iron.ironworker.client.builders;

import java.util.HashMap;
import java.util.Map;

public class TaskProgressOptionsObject {
    private final Map<String, Object> options;

    public TaskProgressOptionsObject() {
        options = new HashMap<String, Object>();
    }

    public TaskProgressOptionsObject percent(int percent) {
        options.put("percent", percent);

        return this;
    }

    public TaskProgressOptionsObject msg(String msg) {
        options.put("msg", msg);

        return this;
    }

    public Map<String, Object> create() {
        return options;
    }
}
