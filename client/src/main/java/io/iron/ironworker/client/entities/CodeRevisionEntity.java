package io.iron.ironworker.client.entities;

import com.google.gson.JsonObject;

import java.util.Date;

public class CodeRevisionEntity extends BaseEntity {
    String id;
    String codeId;
    String projectId;
    String name;
    String runtime;
    String runner;
    int revision;
    Date createdAt;
    Date updatedAt;

    public static CodeRevisionEntity fromJsonObject(JsonObject o) {
        CodeRevisionEntity cr = new CodeRevisionEntity();

        cr.id = parseString(o, "id");
        cr.codeId = parseString(o, "code_id");
        cr.projectId = parseString(o, "project_id");
        cr.name = parseString(o, "name");
        cr.runtime = parseString(o, "runtime");
        cr.runner = parseString(o, "file_name");
        cr.revision = parseInt(o, "rev");
        cr.createdAt = parseDate(o, "created_at");
        cr.updatedAt = parseDate(o, "updated_at");

        return cr;
    }

    protected CodeRevisionEntity() {
    }

    public String getId() {
        return id;
    }

    public String getCodeId() {
        return codeId;
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
