package io.iron.ironworker.client.builders;

import java.util.HashMap;
import java.util.Map;

public class TaskOptionsObject {
    private final Map<String, Object> options;

    public TaskOptionsObject() {
        options = new HashMap<String, Object>();
    }

    public TaskOptionsObject priority(int priority) {
        options.put("priority", priority);

        return this;
    }

    public TaskOptionsObject timeout(int timeout) {
        options.put("timeout", timeout);

        return this;
    }

    public TaskOptionsObject cluster(String cluster) {
        options.put("cluster", cluster);

        return this;
    }

    public TaskOptionsObject label(String label) {
        options.put("label", label);

        return this;
    }

    public TaskOptionsObject delay(int delay) {
        options.put("delay", delay);

        return this;
    }
    
    public TaskOptionsObject encryptionKeyFile(String encryptionKeyFile) {
        options.put("encryptionKeyFile", encryptionKeyFile);

        return this;
    }

    public Map<String, Object> create() {
        return options;
    }
}
