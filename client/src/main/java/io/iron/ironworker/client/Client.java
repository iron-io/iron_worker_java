package io.iron.ironworker.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.iron.ironworker.client.builders.*;
import io.iron.ironworker.client.codes.BaseCode;
import io.iron.ironworker.client.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    private APIClient api;
    private Gson gson;
    
    public Client(String token, String projectId) {
        api = new APIClient(token, projectId);
        gson = new Gson();
    }

    public APIClient getAPI() {
        return api;
    }
    
    public List<CodeEntity> getCodes(Map<String, Object> options) throws APIException {
        JsonObject codes = api.codesList(options);
        
        List<CodeEntity> codesList = new ArrayList<CodeEntity>();

        for (JsonElement code : codes.get("codes").getAsJsonArray()) {
            codesList.add(gson.fromJson(code, CodeEntity.class));
        }

        return codesList;
    }

    public List<CodeEntity> getCodes(PaginationOptionsObject options) throws APIException {
        return getCodes(options.create());
    }

    public List<CodeEntity> getCodes() throws APIException {
        return getCodes((Map<String, Object>) null);
    }

    public CodeEntity getCode(String codeId) throws APIException {
        return gson.fromJson(api.codesGet(codeId), CodeEntity.class);
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
            codeRevisionsList.add(gson.fromJson(codeRevision, CodeRevisionEntity.class));
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
            tasksList.add(gson.fromJson(task, TaskEntity.class));
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
        return gson.fromJson(api.tasksGet(taskId), TaskEntity.class);
    }
    
    public String createTask(String codeName, Map<String, Object> params, Map<String, Object> options) throws APIException {
        if (params == null) {
            params = new HashMap<String, Object>();
        }

        // TODO: implement multiple worker queueing (http://dev.iron.io/worker/reference/api/#queue_a_task)
        return gson.fromJson(api.tasksCreate(codeName, gson.toJson(params), options), TaskIds.class).getIds()[0];
    }

    public String createTask(String codeName, Map<String, Object> params, TaskOptionsObject options) throws APIException {
        return createTask(codeName, params, options.create());
    }

    public String createTask(String codeName, ParamsObject params, Map<String, Object> options) throws APIException {
        return createTask(codeName, params.create(), options);
    }

    public String createTask(String codeName, ParamsObject params, TaskOptionsObject options) throws APIException {
        return createTask(codeName, params.create(), options.create());
    }

    public String createTask(String codeName, Map<String, Object> params) throws APIException {
        return createTask(codeName, params, (Map<String, Object>) null);
    }

    public String createTask(String codeName, ParamsObject params) throws APIException {
        return createTask(codeName, params.create(), (Map<String, Object>) null);
    }

    public String createTask(String codeName) throws APIException {
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

    public List<ScheduleEntity> getSchedules(Map<String, Object> options) throws APIException {
        JsonObject schedules = api.schedulesList(options);

        List<ScheduleEntity> schedulesList = new ArrayList<ScheduleEntity>();

        for (JsonElement schedule : schedules.get("schedules").getAsJsonArray()) {
            schedulesList.add(gson.fromJson(schedule, ScheduleEntity.class));
        }

        return schedulesList;
    }

    public List<ScheduleEntity> getSchedules(PaginationOptionsObject options) throws APIException {
        return getSchedules(options.create());
    }

    public List<ScheduleEntity> getSchedules() throws APIException {
        return getSchedules((Map<String, Object>) null);
    }

    public ScheduleEntity getSchedule(String scheduleId) throws APIException {
        return gson.fromJson(api.schedulesGet(scheduleId), ScheduleEntity.class);
    }

    public ScheduleEntity createSchedule(String codeName, Map<String, Object> params, Map<String, Object> options) throws APIException {
        if (params == null) {
            params = new HashMap<String, Object>();
        }

        JsonObject schedules = api.schedulesCreate(codeName, gson.toJson(params), options);
        JsonObject schedule = schedules.get("schedules").getAsJsonArray().get(0).getAsJsonObject();

        return gson.fromJson(schedule, ScheduleEntity.class);
    }

    public ScheduleEntity createSchedule(String codeName, Map<String, Object> params, ScheduleOptionsObject options) throws APIException {
        return createSchedule(codeName, params, options.create());
    }

    public ScheduleEntity createSchedule(String codeName, ParamsObject params, Map<String, Object> options) throws APIException {
        return createSchedule(codeName, params.create(), options);
    }

    public ScheduleEntity createSchedule(String codeName, ParamsObject params, ScheduleOptionsObject options) throws APIException {
        return createSchedule(codeName, params.create(), options.create());
    }

    public ScheduleEntity createSchedule(String codeName, Map<String, Object> params) throws APIException {
        return createSchedule(codeName, params, (Map<String, Object>) null);
    }

    public ScheduleEntity createSchedule(String codeName, ParamsObject params) throws APIException {
        return createSchedule(codeName, params.create(), (Map<String, Object>) null);
    }

    public ScheduleEntity createSchedule(String codeName) throws APIException {
        return createSchedule(codeName, (Map<String, Object>) null, (Map<String, Object>) null);
    }

    public void cancelSchedule(String scheduleId) throws APIException {
        api.schedulesCancel(scheduleId);
    }
}
