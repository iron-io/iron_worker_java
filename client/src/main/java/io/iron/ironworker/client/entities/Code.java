package io.iron.ironworker.client.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * User: Julien
 * Date: 22/03/12
 * Time: 17:27
 */
public class Code {
    private String id;
    @SerializedName("project_id")
    private String projectId;
    private String name;
    private String runtime;
    @SerializedName("latest_checksum")
    private String latestChecksum;
    @SerializedName("rev")
    private int revision;
    @SerializedName("latest_history_id")
    private String latestHistoryId;
    @SerializedName("latest_change")
    private Date latestChange;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public String getLatestHistoryId() {
        return latestHistoryId;
    }

    public void setLatestHistoryId(String latestHistoryId) {
        this.latestHistoryId = latestHistoryId;
    }

    public String getLatestChecksum() {
        return latestChecksum;
    }

    public void setLatestChecksum(String latestChecksum) {
        this.latestChecksum = latestChecksum;
    }

    public Date getLatestChange() {
        return latestChange;
    }

    public void setLatestChange(Date latestChange) {
        this.latestChange = latestChange;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Code{" +
                "id='" + id + '\'' +
                ", projectId='" + projectId + '\'' +
                ", name='" + name + '\'' +
                ", runtime='" + runtime + '\'' +
                ", latestChecksum='" + latestChecksum + '\'' +
                ", revision=" + revision +
                ", latestHistoryId='" + latestHistoryId + '\'' +
                ", latestChange=" + latestChange +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
