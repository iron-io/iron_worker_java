package io.iron.ironworker.client.entities;

public class ScheduleIds {
    private ScheduleEntity[] schedules;

    public String[] getIds() {
        String[] result = new String[schedules.length];
        for (int i = 0; i < schedules.length; i++) {
            result[i] = schedules[i].getId();
        }
        return result;
    }
}