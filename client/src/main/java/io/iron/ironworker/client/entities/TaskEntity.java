package io.iron.ironworker.client.entities;

import com.google.gson.JsonObject;

import java.util.Date;

public class TaskEntity extends BaseEntity {
    String id;
    String projectId;
    String codeId;
    String codeName;
    String status;
    Date startTime;
    Date endTime;
    int duration;
    int runTimes;
    int timeout;
    String payload;
    int percent;
    String msg;
    Date createdAt;
    Date updatedAt;

    public static TaskEntity fromJsonObject(JsonObject o) {
        TaskEntity t = new TaskEntity();

        t.id = parseString(o, "id");
        t.projectId = parseString(o, "project_id");
        t.codeId = parseString(o, "code_id");
        t.codeName = parseString(o, "code_name");
        t.status = parseString(o, "status");
        t.startTime = parseDate(o, "start_time");
        t.endTime = parseDate(o, "end_time");
        t.duration = parseInt(o, "duration");
        t.runTimes = parseInt(o, "run_times");
        t.timeout = parseInt(o, "timeout");
        t.payload = parseString(o, "payload");
        t.percent = parseInt(o, "percent");
        t.msg = parseString(o, "msg");
        t.createdAt = parseDate(o, "created_at");
        t.updatedAt = parseDate(o, "updated_at");
        
        return t;
    }

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
