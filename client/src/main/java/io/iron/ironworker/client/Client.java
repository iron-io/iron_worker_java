package io.iron.ironworker.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.iron.ironworker.client.builders.*;
import io.iron.ironworker.client.codes.BaseCode;
import io.iron.ironworker.client.entities.CodeEntity;
import io.iron.ironworker.client.entities.CodeRevisionEntity;
import io.iron.ironworker.client.entities.Codes;
import io.iron.ironworker.client.entities.TaskEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    private APIClient api;
    
    public Client(String token, String projectId) {
        api = new APIClient(token, projectId);
    }

    public APIClient getAPI() {
        return api;
    }
    
    public Codes getCodes(Map<String, Object> options) throws APIException {
        JsonObject codes = api.codesList(options);
        return new Gson().fromJson(codes, Codes.class);
    }

    public Codes getCodes(PaginationOptionsObject options) throws APIException {
        return getCodes(options.create());
    }

    public Codes getCodes() throws APIException {
        return getCodes((Map<String, Object>) null);
    }

    public CodeEntity getCode(String codeId) throws APIException {
        return CodeEntity.fromJsonObject(api.codesGet(codeId));
    }
    
    public void createCode(BaseCode code) throws APIException {
        api.codesCreate(code.getName(), code.getFile(), code.getRuntime(), code.getRunner());
    }

    public void deleteCode(String codeId) throws APIException {
        api.codesDelete(codeId);
    }

    public List<CodeRevisionEntity> getCodeRevisions(String codeId, Map<String, Object> options) throws APIException {
        JsonObject codeRevisions = api.codesRevisions(codeId, options);

        List<CodeRevisionEntity> codeRevisionsList = new ArrayList<CodeRevisionEntity>();

        for (JsonElement codeRevision : codeRevisions.get("revisions").getAsJsonArray()) {
            codeRevisionsList.add(CodeRevisionEntity.fromJsonObject(codeRevision.getAsJsonObject()));
        }

        return codeRevisionsList;
    }

    public List<CodeRevisionEntity> getCodeRevisions(String codeId, PaginationOptionsObject options) throws APIException {
        return getCodeRevisions(codeId, options.create());
    }

    public List<CodeRevisionEntity> getCodeRevisions(String codeId) throws APIException {
        return getCodeRevisions(codeId, (Map<String, Object>) null);
    }

    public byte[] downloadCode(String codeId, Map<String, Object> options) throws APIException {
        return api.codesDownload(codeId, options);
    }

    public byte[] downloadCode(String codeId, int revision) throws APIException {
        return downloadCode(codeId, Params.create("revision", revision));
    }

    public byte[] downloadCode(String codeId) throws APIException {
        return downloadCode(codeId, null);
    }

    public List<TaskEntity> getTasks(Map<String, Object> options) throws APIException {
        JsonObject tasks = api.tasksList(options);

        List<TaskEntity> tasksList = new ArrayList<TaskEntity>();

        for (JsonElement task : tasks.get("tasks").getAsJsonArray()) {
            tasksList.add(TaskEntity.fromJsonObject(task.getAsJsonObject()));
        }

        return tasksList;
    }

    public List<TaskEntity> getTasks(PaginationOptionsObject options) throws APIException {
        return getTasks(options.create());
    }

    public List<TaskEntity> getTasks() throws APIException {
        return getTasks((Map<String, Object>) null);
    }

    public TaskEntity getTask(String taskId) throws APIException {
        return TaskEntity.fromJsonObject(api.tasksGet(taskId));
    }
    
    public TaskEntity createTask(String codeName, Map<String, Object> params, Map<String, Object> options) throws APIException {
        if (params == null) {
            params = new HashMap<String, Object>();
        }

        JsonObject tasks = api.tasksCreate(codeName, (new Gson()).toJson(Params.create("token", api.getToken(), "project_id", api.getProjectId(), "params", params)), options);
        JsonObject task = tasks.get("tasks").getAsJsonArray().get(0).getAsJsonObject();

        return TaskEntity.fromJsonObject(task);
    }

    public TaskEntity createTask(String codeName, Map<String, Object> params, TaskOptionsObject options) throws APIException {
        return createTask(codeName, params, options.create());
    }

    public TaskEntity createTask(String codeName, ParamsObject params, Map<String, Object> options) throws APIException {
        return createTask(codeName, params.create(), options);
    }

    public TaskEntity createTask(String codeName, ParamsObject params, TaskOptionsObject options) throws APIException {
        return createTask(codeName, params.create(), options.create());
    }

    public TaskEntity createTask(String codeName, Map<String, Object> params) throws APIException {
        return createTask(codeName, params, (Map<String, Object>) null);
    }

    public TaskEntity createTask(String codeName, ParamsObject params) throws APIException {
        return createTask(codeName, params.create(), (Map<String, Object>) null);
    }

    public TaskEntity createTask(String codeName) throws APIException {
        return createTask(codeName, (Map<String, Object>) null, (Map<String, Object>) null);
    }
    
    public void cancelTask(String taskId) throws APIException {
        api.tasksCancel(taskId);
    }

    public void cancelAllTasks(String codeId) throws APIException {
        api.tasksCancelAll(codeId);
    }
    
    public String getTaskLog(String taskId) throws APIException {
        return api.tasksLog(taskId);
    }
    
    public void setTaskProgress(String taskId, Map<String, Object> options) throws APIException {
        api.tasksSetProgress(taskId, options);
    }

    public void setTaskProgress(String taskId, TaskProgressOptionsObject options) throws APIException {
        api.tasksSetProgress(taskId, options.create());
    }

    public void setTaskProgress(String taskId) throws APIException {
        setTaskProgress(taskId, (Map<String, Object>) null);
    }
}
