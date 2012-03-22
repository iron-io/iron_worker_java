package io.iron.ironworker.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.iron.ironworker.client.codes.BaseCode;
import io.iron.ironworker.client.entities.CodeEntity;
import io.iron.ironworker.client.entities.CodeRevisionEntity;

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

    private List<CodeEntity> getCodes(Map<String, Object> params) throws APIException {
        JsonObject codes = api.codesList(params);
        
        List<CodeEntity> codesList = new ArrayList<CodeEntity>();
        
        for (JsonElement code : codes.get("codes").getAsJsonArray()) {
            codesList.add(CodeEntity.fromJsonObject(code.getAsJsonObject()));
        }

        return codesList;
    }

    public List<CodeEntity> getCodes() throws APIException {
        return getCodes(null);
    }

    public List<CodeEntity> getCodes(int page) throws APIException {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("page", page);

        return getCodes(params);
    }

    public List<CodeEntity> getCodes(int page, int perPage) throws APIException {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("page", page);
        params.put("per_page", perPage);

        return getCodes(params);
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

    private List<CodeRevisionEntity> getCodeRevisions(String codeId, Map<String, Object> params) throws APIException {
        JsonObject codeRevisions = api.codesRevisions(codeId, params);

        List<CodeRevisionEntity> codeRevisionsList = new ArrayList<CodeRevisionEntity>();

        for (JsonElement codeRevision : codeRevisions.get("revisions").getAsJsonArray()) {
            codeRevisionsList.add(CodeRevisionEntity.fromJsonObject(codeRevision.getAsJsonObject()));
        }

        return codeRevisionsList;
    }

    public List<CodeRevisionEntity> getCodeRevisions(String codeId) throws APIException {
        return getCodeRevisions(codeId, null);
    }

    public List<CodeRevisionEntity> getCodeRevisions(String codeId, int page) throws APIException {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("page", page);

        return getCodeRevisions(codeId, params);
    }

    public List<CodeRevisionEntity> getCodeRevisions(String codeId, int page, int perPage) throws APIException {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("page", page);
        params.put("per_page", perPage);

        return getCodeRevisions(codeId, params);
    }

    private byte[] downloadCode(String codeId, Map<String, Object> params) throws APIException {
        return api.codesDownload(codeId, params);
    }
    
    public byte[] downloadCode(String codeId) throws APIException {
        return downloadCode(codeId, null);
    }

    public byte[] downloadCode(String codeId, int revision) throws APIException {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("revision", revision);

        return downloadCode(codeId, params);
    }
}
