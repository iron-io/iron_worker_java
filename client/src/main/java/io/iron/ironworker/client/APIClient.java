package io.iron.ironworker.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIClient {
    public static final String AWS_US_EAST_HOST = "worker-aws-us-east-1.iron.io";

    private String token;
    private String projectId;
    private String scheme;
    private String host;
    private int port;
    private int apiVersion;
    private String userAgent;
    private int maxRetries;

    public APIClient(String token, String projectId) {
        this.token = token;
        this.projectId = projectId;
        this.scheme = "https";
        this.host = AWS_US_EAST_HOST;
        this.port = 443;
        this.apiVersion = 2;
        this.userAgent = "iron_worker_java-1.0.0";
        maxRetries = 5;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getAPIVersion() {
        return apiVersion;
    }

    public void setAPIVersion(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    private HttpResponse doRequestExecute(HttpRequestBase request) throws APIException {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;

        int currentTry = 0;

        while (currentTry < maxRetries) {
            try {
                response = client.execute(request);

                if (response.getStatusLine().getStatusCode() != 503) {
                    return response;
                }
            } catch (IOException e) {
                if (currentTry == maxRetries - 1) {
                    throw new APIException(null, e);
                }
            }

            try {
                Thread.sleep((long) (Math.pow(4, currentTry) * 100 * Math.random()));
            } catch (InterruptedException e) {
                throw new APIException(null, e);
            }

            currentTry++;
        }

        return response;
    }

    private void setRequestParams(HttpRequestBase request, String method, Map<String, Object> params) throws APIException {
        String query = null;

        if (params != null && !params.isEmpty()) {
            List<NameValuePair> qParams = new ArrayList<NameValuePair>();

            for (String key : params.keySet()) {
                qParams.add(new BasicNameValuePair(key, params.get(key).toString()));
            }

            query = URLEncodedUtils.format(qParams, "UTF-8");
        }

        URI uri;

        try {
            uri = URIUtils.createURI(scheme, host, port, "" + apiVersion + "/" + method, query, null);
        } catch (URISyntaxException e) {
            throw new APIException(null, e);
        }

        request.setURI(uri);

        request.addHeader("Authorization", "OAuth " + token);
        request.addHeader("User-Agent", userAgent);
    }

    private HttpResponse doGetRequest(String method, Map<String, Object> params) throws APIException {
        HttpGet request = new HttpGet();

        setRequestParams(request, method, params);

        return doRequestExecute(request);
    }

    private HttpResponse doPostRequest(String method, String data) throws APIException {
        HttpPost request = new HttpPost();

        setRequestParams(request, method, null);

        if (data != null) {
            request.addHeader("Content-Type", "application/json");

            StringEntity entity;

            try {
                entity = new StringEntity(data);
            } catch (UnsupportedEncodingException e) {
                throw new APIException(null, e);
            }

            request.setEntity(entity);
        }

        return doRequestExecute(request);
    }

    private HttpResponse doDeleteRequest(String method, Map<String, Object> params) throws APIException {
        HttpDelete request = new HttpDelete();

        setRequestParams(request, method, params);

        return doRequestExecute(request);
    }

    private HttpResponse doFileRequest(String method, String data, File file) throws APIException {
        HttpPost request = new HttpPost();

        setRequestParams(request, method, null);

        MultipartEntity entity = new MultipartEntity();

        if (data != null) {
            try {
                entity.addPart("data", new StringBody(data));
            } catch (UnsupportedEncodingException e) {
                throw new APIException(null, e);
            }
        }

        entity.addPart("file", new FileBody(file));

        request.setEntity(entity);

        return doRequestExecute(request);
    }

    private InputStream parseResponseGeneral(HttpResponse response) throws APIException {
        InputStream result;

        try {
            result = response.getEntity().getContent();
        } catch (IOException e) {
            throw new APIException(null, e);
        }

        if (response.getStatusLine().getStatusCode() != 200) {
            try {
                throw new APIException(IOUtils.toString(result), null);
            } catch (IOException e) {
                throw new APIException(null, e);
            }
        }

        return result;
    }

    private byte[] parseResponseAsByteArray(HttpResponse response) throws APIException {
        try {
            return IOUtils.toByteArray(parseResponseGeneral(response));
        } catch (IOException e) {
            throw new APIException(null, e);
        }
    }

    private String parseResponseAsString(HttpResponse response) throws APIException {
        try {
            return IOUtils.toString(parseResponseGeneral(response));
        } catch (IOException e) {
            throw new APIException(null, e);
        }
    }

    private JsonObject parseResponseAsJson(HttpResponse response) throws APIException {
        return (new JsonParser()).parse(new InputStreamReader(parseResponseGeneral(response))).getAsJsonObject();
    }

    public JsonObject codesList(Map<String, Object> params) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/codes", projectId), params));
    }

    public JsonObject codesGet(String id) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/codes/%s", projectId, id), null));
    }

    public JsonObject codesCreate(String name, String file, String runtime, String runner) throws APIException {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("name", name);
        params.put("runtime", runtime);
        params.put("file_name", runner);

        File f = new File(file);

        if (!f.exists()) {
            throw new APIException("File " + file + " not found", null);
        }

        return parseResponseAsJson(doFileRequest(String.format("projects/%s/codes", projectId), (new Gson()).toJson(params), f));
    }

    public JsonObject codesDelete(String id) throws APIException {
        return parseResponseAsJson(doDeleteRequest(String.format("projects/%s/codes/%s", projectId, id), null));
    }

    public JsonObject codesRevisions(String id, Map<String, Object> params) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/codes/%s/revisions", projectId, id), params));
    }

    public byte[] codesDownload(String id, Map<String, Object> params) throws APIException {
        return parseResponseAsByteArray(doGetRequest(String.format("projects/%s/codes/%s/download", projectId, id), params));
    }

    public JsonObject tasksList(Map<String, Object> params) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/tasks", projectId), params));
    }

    public JsonObject tasksGet(String id) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/tasks/%s", projectId, id), null));
    }

    public JsonObject tasksCreate(String code_name, String payload, Map<String, Object> params) throws APIException {
        Map<String, Object> task = new HashMap<String, Object>();

        task.put("code_name", code_name);
        task.put("payload", payload);
        task.putAll(params);

        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();

        tasks.add(task);

        Map<String, List<Map<String, Object>>> fullParams = new HashMap<String, List<Map<String, Object>>>();

        fullParams.put("tasks", tasks);

        return parseResponseAsJson(doPostRequest(String.format("projects/%s/tasks", projectId), (new Gson()).toJson(fullParams)));
    }

    public JsonObject tasksCancel(String id) throws APIException {
        return parseResponseAsJson(doPostRequest(String.format("projects/%s/tasks/%s/cancel", projectId, id), null));
    }

    public JsonObject tasksCancelAll(String code_id) throws APIException {
        return parseResponseAsJson(doPostRequest(String.format("projects/%s/codes/%s/cancel_all", projectId, code_id), null));
    }

    public String tasksLog(String id) throws APIException {
        return parseResponseAsString(doGetRequest(String.format("projects/%s/tasks/%s/log", projectId, id), null));
    }

    public JsonObject tasksSetProgress(String id, Map<String, Object> params) throws APIException {
        return parseResponseAsJson(doPostRequest(String.format("projects/%s/tasks/%s/progress", projectId, id), (new Gson()).toJson(params)));
    }

    public JsonObject schedulesList(Map<String, Object> params) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/schedules", projectId), params));
    }

    public JsonObject schedulesGet(String id) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/schedules/%s", projectId, id), null));
    }

    public JsonObject schedulesCreate(String code_name, String payload, Map<String, Object> params) throws APIException {
        Map<String, Object> schedule = new HashMap<String, Object>();

        schedule.put("code_name", code_name);
        schedule.put("payload", payload);
        schedule.putAll(params);

        List<Map<String, Object>> schedules = new ArrayList<Map<String, Object>>();

        schedules.add(schedule);

        Map<String, List<Map<String, Object>>> fullParams = new HashMap<String, List<Map<String, Object>>>();

        fullParams.put("schedules", schedules);

        return parseResponseAsJson(doPostRequest(String.format("projects/%s/schedules", projectId), (new Gson()).toJson(fullParams)));
    }

    public JsonObject schedulesCancel(String id) throws APIException {
        return parseResponseAsJson(doPostRequest(String.format("projects/%s/schedules/%s/cancel", projectId, id), null));
    }
}
