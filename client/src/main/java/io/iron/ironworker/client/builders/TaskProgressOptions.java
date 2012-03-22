package io.iron.ironworker.client.builders;

import java.util.HashMap;
import java.util.Map;

public class TaskProgressOptions {
    private Map<String, Object> options;

    public TaskProgressOptions() {
        options = new HashMap<String, Object>();
    }

    public TaskProgressOptions percent(int percent) {
        options.put("percent", percent);

        return this;
    }

    public TaskProgressOptions msg(String msg) {
        options.put("msg", msg);

        return this;
    }

    public Map<String, Object> create() {
        return options;
    }
}
