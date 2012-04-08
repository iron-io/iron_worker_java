package io.iron.ironworker.client.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CodeEntity {
    @SerializedName("id")
    String id;
    @SerializedName("project_id")
    String projectId;
    @SerializedName("name")
    String name;
    @SerializedName("runtime")
    String runtime;
    @SerializedName("rev")
    int revision;
    @SerializedName("latest_history_id")
    String latestHistoryId;
    @SerializedName("latest_checksum")
    String latestChecksum;
    @SerializedName("latest_change")
    Date latestChange;
    @SerializedName("created_at")
    Date createdAt;
    @SerializedName("updated_at")
    Date updatedAt;
    
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
