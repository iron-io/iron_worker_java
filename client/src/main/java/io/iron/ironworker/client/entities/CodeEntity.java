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

        c.id = o.get("id").getAsString();
        c.projectId = o.get("project_id").getAsString();
        c.name = o.get("name").getAsString();
        c.runtime = o.get("runtime").getAsString();
        c.revision = o.get("rev").getAsInt();
        c.latestHistoryId = o.get("latest_history_id").getAsString();
        c.latestChecksum = o.get("latest_checksum").getAsString();
        c.latestChange = parseDate(o.get("latest_change").getAsString());
        c.createdAt = parseDate(o.get("created_at").getAsString());
        c.updatedAt = parseDate(o.get("updated_at").getAsString());

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
