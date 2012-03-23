package io.iron.ironworker.client.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TaskEntity {
    @SerializedName("id")
    String id;
    @SerializedName("project_id")
    String projectId;
    @SerializedName("code_id")
    String codeId;
    @SerializedName("code_name")
    String codeName;
    @SerializedName("status")
    String status;
    @SerializedName("start_time")
    Date startTime;
    @SerializedName("end_time")
    Date endTime;
    @SerializedName("duration")
    int duration;
    @SerializedName("run_times")
    int runTimes;
    @SerializedName("timeout")
    int timeout;
    @SerializedName("payload")
    String payload;
    @SerializedName("percent")
    int percent;
    @SerializedName("msg")
    String msg;
    @SerializedName("created_at")
    Date createdAt;
    @SerializedName("updated_at")
    Date updatedAt;

    protected TaskEntity() {
    }

    public String getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getCodeId() {
        return codeId;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getStatus() {
        return status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getDuration() {
        return duration;
    }

    public int getRunTimes() {
        return runTimes;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getPayload() {
        return payload;
    }

    public int getPercent() {
        return percent;
    }

    public String getMsg() {
        return msg;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
