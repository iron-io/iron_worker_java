package io.iron.ironworker.client.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ScheduleEntity {
    @SerializedName("id")
    String id;
    @SerializedName("project_id")
    String projectId;
    @SerializedName("code_name")
    String codeName;
    @SerializedName("priority")
    int priority;
    @SerializedName("start_at")
    Date startAt;
    @SerializedName("end_at")
    Date endAt;
    @SerializedName("delay")
    int delay;
    @SerializedName("run_every")
    int runEvery;
    @SerializedName("run_times")
    int runTimes;
    @SerializedName("next_start")
    Date nextStart;
    @SerializedName("status")
    String status;
    @SerializedName("last_run_time")
    long lastRunTime;
    @SerializedName("run_count")
    int runCount;
    @SerializedName("payload")
    String payload;
    @SerializedName("msg")
    String msg;
    @SerializedName("created_at")
    long createdAt;
    @SerializedName("updated_at")
    long updatedAt;

    protected ScheduleEntity() {
    }

    public String getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getCodeName() {
        return codeName;
    }

    public int getPriority() {
        return priority;
    }

    public Date getStartAt() {
        return startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public int getDelay() {
        return delay;
    }

    public int getRunEvery() {
        return runEvery;
    }

    public int getRunTimes() {
        return runTimes;
    }

    public Date getNextStart() {
        return nextStart;
    }

    public String getStatus() {
        return status;
    }

    public Date getLastRunTime() {
        return lastRunTime == 0 ? null : new Date(lastRunTime / 1000000);
    }

    public int getRunCount() {
        return runCount;
    }

    public String getPayload() {
        return payload;
    }

    public String getMsg() {
        return msg;
    }

    public Date getCreatedAt() {
        return createdAt == 0 ? null : new Date(createdAt / 1000000);
    }

    public Date getUpdatedAt() {
        return updatedAt == 0 ? null : new Date(updatedAt / 1000000);
    }
}
