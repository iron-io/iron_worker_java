package io.iron.ironworker.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.iron.ironworker.client.builders.Params;
import io.iron.ironworker.client.codes.BaseCode;
import io.iron.ironworker.client.entities.CodeEntity;
import io.iron.ironworker.client.entities.CodeRevisionEntity;
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
    
    public List<CodeEntity> getCodes(Map<String, Object> options) throws APIException {
        JsonObject codes = api.codesList(options);
        
        List<CodeEntity> codesList = new ArrayList<CodeEntity>();
        
        for (JsonElement code : codes.get("codes").getAsJsonArray()) {
            codesList.add(CodeEntity.fromJsonObject(code.getAsJsonObject()));
        }

        return codesList;
    }

    public List<CodeEntity> getCodes() throws APIException {
        return getCodes(null);
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

    public List<CodeRevisionEntity> getCodeRevisions(String codeId) throws APIException {
        return getCodeRevisions(codeId, null);
    }

    private byte[] downloadCode(String codeId, Map<String, Object> options) throws APIException {
        return api.codesDownload(codeId, options);
    }
    
    public byte[] downloadCode(String codeId) throws APIException {
        return downloadCode(codeId, null);
    }

    private List<TaskEntity> getTasks(Map<String, Object> options) throws APIException {
        JsonObject tasks = api.tasksList(options);

        List<TaskEntity> tasksList = new ArrayList<TaskEntity>();

        for (JsonElement task : tasks.get("tasks").getAsJsonArray()) {
            tasksList.add(TaskEntity.fromJsonObject(task.getAsJsonObject()));
        }

        return tasksList;
    }

    public List<TaskEntity> getTasks() throws APIException {
        return getTasks(null);
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

    public TaskEntity createTask(String codeName, Params params, Map<String, Object> options) throws APIException {
        return createTask(codeName, params.create(), options);
    }

    public TaskEntity createTask(String codeName, Map<String, Object> params) throws APIException {
        return createTask(codeName, params, null);
    }

    public TaskEntity createTask(String codeName, Params params) throws APIException {
        return createTask(codeName, params.create(), null);
    }

    public TaskEntity createTask(String codeName) throws APIException {
        return createTask(codeName, (Map<String, Object>) null, null);
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

    public void setTaskProgress(String taskId) throws APIException {
        api.tasksSetProgress(taskId, null);
    }
}
