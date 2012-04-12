package io.iron.ironworker.client.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ScheduleEntity extends BaseEntity {
    @SerializedName("id")
    String id;
    @SerializedName("project_id")
    String projectId;
    @SerializedName("code_name")
    String codeName;
    @SerializedName("priority")
    int priority;
    @SerializedName("start_at")
    String startAt;
    @SerializedName("end_at")
    String endAt;
    @SerializedName("delay")
    int delay;
    @SerializedName("run_every")
    int runEvery;
    @SerializedName("run_times")
    int runTimes;
    @SerializedName("next_start")
    String nextStart;
    @SerializedName("status")
    String status;
    @SerializedName("last_run_time")
    String lastRunTime;
    @SerializedName("run_count")
    int runCount;
    @SerializedName("payload")
    String payload;
    @SerializedName("msg")
    String msg;
    @SerializedName("created_at")
    String createdAt;
    @SerializedName("updated_at")
    String updatedAt;

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
        return parseDate(startAt);
    }

    public Date getEndAt() {
        return parseDate(endAt);
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
        return parseDate(nextStart);
    }

    public String getStatus() {
        return status;
    }

    public Date getLastRunTime() {
        return parseDate(lastRunTime);
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
        return parseDate(createdAt);
    }

    public Date getUpdatedAt() {
        return parseDate(updatedAt);
    }
}
