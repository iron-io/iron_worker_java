package io.iron.ironworker.client.entities;

import com.google.gson.JsonObject;

import java.util.Date;

public class CodeRevisionEntity extends BaseEntity {
    String id;
    String codeId;
    String projectId;
    String name;
    String runtime;
    String fileName;
    int revision;
    Date createdAt;
    Date updatedAt;

    public static CodeRevisionEntity fromJsonObject(JsonObject o) {
        CodeRevisionEntity c = new CodeRevisionEntity();

        c.id = o.get("id").getAsString();
        c.codeId = o.get("code_id").getAsString();
        c.projectId = o.get("project_id").getAsString();
        c.name = o.get("name").getAsString();
        c.runtime = o.get("runtime").getAsString();
        c.fileName = o.get("file_name").getAsString();
        c.revision = o.get("rev").getAsInt();
        c.createdAt = parseDate(o.get("created_at").getAsString());
        c.updatedAt = parseDate(o.get("updated_at").getAsString());

        return c;
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

    public String getFileName() {
        return fileName;
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
