package io.iron.ironworker.client.builders;

import java.util.Date;

public class ScheduleOptions {
    public static ScheduleOptionsObject priority(int priority) {
        return (new ScheduleOptionsObject()).priority(priority);
    }

    public static ScheduleOptionsObject startAt(Date startAt) {
        return (new ScheduleOptionsObject()).startAt(startAt);
    }

    public static ScheduleOptionsObject endAt(Date endAt) {
        return (new ScheduleOptionsObject()).endAt(endAt);
    }

    public static ScheduleOptionsObject delay(int delay) {
        return (new ScheduleOptionsObject()).delay(delay);
    }

    public static ScheduleOptionsObject runEvery(int runEvery) {
        return (new ScheduleOptionsObject()).runEvery(runEvery);
    }

    public static ScheduleOptionsObject runTimes(int runTimes) {
        return (new ScheduleOptionsObject()).runTimes(runTimes);
    }

    public static ScheduleOptionsObject cluster(String cluster) {
        return (new ScheduleOptionsObject()).cluster(cluster);
    }

    public static ScheduleOptionsObject label(String label) {
        return (new ScheduleOptionsObject()).label(label);
    }
    
    public ScheduleOptionsObject encryptionKey(String encryptionKey){
        return (new ScheduleOptionsObject()).encryptionKey(encryptionKey);
    }
    
    public ScheduleOptionsObject encryptionKeyFile(String encryptionKeyFile){
        return (new ScheduleOptionsObject()).encryptionKeyFile(encryptionKeyFile);
    }

    protected ScheduleOptions() {
    }
}
