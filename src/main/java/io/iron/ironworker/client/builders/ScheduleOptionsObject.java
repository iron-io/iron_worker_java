package io.iron.ironworker.client.builders;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScheduleOptionsObject {
    private Map<String, Object> options;

    public ScheduleOptionsObject() {
        options = new HashMap<String, Object>();
    }

    public ScheduleOptionsObject priority(int priority) {
        options.put("priority", priority);

        return this;
    }

    public ScheduleOptionsObject startAt(Date startAt) {
        options.put("start_at", startAt);

        return this;
    }

    public ScheduleOptionsObject endAt(Date endAt) {
        options.put("end_at", endAt);

        return this;
    }

    public ScheduleOptionsObject delay(int delay) {
        options.put("delay", delay);

        return this;
    }

    public ScheduleOptionsObject runEvery(int runEvery) {
        options.put("run_every", runEvery);

        return this;
    }

    public ScheduleOptionsObject runTimes(int runTimes) {
        options.put("run_times", runTimes);

        return this;
    }

    public ScheduleOptionsObject cluster(String cluster) {
        options.put("cluster", cluster);

        return this;
    }

    public ScheduleOptionsObject label(String label) {
        options.put("label", label);

        return this;
    }
    
    public ScheduleOptionsObject encryptionKey(String encryptionKey) {
        options.put("encryptionKey", encryptionKey);

        return this;
    }
    
    public ScheduleOptionsObject encryptionKeyFile(String encryptionKeyFile) {
        options.put("encryptionKeyFile", encryptionKeyFile);

        return this;
    }
    
    public Map<String, Object> create() {
        return options;
    }
}
