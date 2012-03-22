package io.iron.ironworker.client.entities;

import com.google.gson.JsonObject;

import java.util.Date;

public class CodeEntity extends BaseEntity {
    String id;
    String projectId;
    String name;
    String runtime;
    int revision;
    String latestHistoryId;
    String latestChecksum;
    Date latestChange;
    Date createdAt;
    Date updatedAt;
    
    public static CodeEntity fromJsonObject(JsonObject o) {
        CodeEntity c = new CodeEntity();

        c.id = parseString(o, "id");
        c.projectId = parseString(o, "project_id");
        c.name = parseString(o, "name");
        c.runtime = parseString(o, "runtime");
        c.revision = parseInt(o, "rev");
        c.latestHistoryId = parseString(o, "latest_history_id");
        c.latestChecksum = parseString(o, "latest_checksum");
        c.latestChange = parseDate(o, "latest_change");
        c.createdAt = parseDate(o, "created_at");
        c.updatedAt = parseDate(o, "updated_at");

        return c;
    }

    protected CodeEntity() {
    }

    public String getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getRuntime() {
        return runtime;
    }

    public int getRevision() {
        return revision;
    }

    public String getLatestHistoryId() {
        return latestHistoryId;
    }

    public String getLatestChecksum() {
        return latestChecksum;
    }

    public Date getLatestChange() {
        return latestChange;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
