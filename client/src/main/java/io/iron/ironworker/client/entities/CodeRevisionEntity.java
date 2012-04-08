package io.iron.ironworker.client.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CodeRevisionEntity {
    @SerializedName("id")
    String id;
    @SerializedName("project_id")
    String projectId;
    @SerializedName("code_id")
    String codeId;
    @SerializedName("name")
    String name;
    @SerializedName("runtime")
    String runtime;
    @SerializedName("file_name")
    String runner;
    @SerializedName("rev")
    int revision;
    @SerializedName("created_at")
    Date createdAt;
    @SerializedName("updated_at")
    Date updatedAt;

    protected CodeRevisionEntity() {
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

    public String getName() {
        return name;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getRunner() {
        return runner;
    }

    public int getRevision() {
        return revision;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
