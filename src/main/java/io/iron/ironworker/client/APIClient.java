package io.iron.ironworker.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.iron.ironworker.client.builders.Params;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private HttpHost httpProxy;

    private Gson gson;

    public APIClient(String token, String projectId) {
        this.token = token;
        this.projectId = projectId;
        this.scheme = "https";
        this.host = AWS_US_EAST_HOST;
        this.port = 443;
        this.apiVersion = 2;
        this.userAgent = "iron_worker_java-1.0.0";
        maxRetries = 5;
        
        gson = new Gson();
    }

    public APIClient(String token, String projectId, HttpHost httpProxy){
        this(token, projectId);
        this.httpProxy = httpProxy;
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

    public HttpHost getHttpProxy() {
        return httpProxy;
    }

    public void setHttpProxy(HttpHost httpProxy) {
        this.httpProxy = httpProxy;
    }

    private HttpResponse doRequestExecute(HttpRequestBase request) throws APIException {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;

        if (this.httpProxy != null){
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, this.httpProxy);
        }

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

    public JsonObject codesList(Map<String, Object> options) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/codes", projectId), options));
    }

    public JsonObject codesGet(String id) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/codes/%s", projectId, id), null));
    }

    public JsonObject codesCreate(String name, String file, String runtime, String runner, String stack) throws APIException {
        File f = new File(file);

        if (!f.exists()) {
            throw new APIException("File " + file + " not found", null);
        }
        Map<String, Object> params = Params.create("name", name, "runtime", runtime, "file_name", runner);
        if(stack != null){
            params.put("stack", stack);
        }
        String data = gson.toJson(params);

        return parseResponseAsJson(doFileRequest(String.format("projects/%s/codes", projectId), data, f));
    }

    public JsonObject codesDelete(String id) throws APIException {
        return parseResponseAsJson(doDeleteRequest(String.format("projects/%s/codes/%s", projectId, id), null));
    }

    public JsonObject codesRevisions(String id, Map<String, Object> options) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/codes/%s/revisions", projectId, id), options));
    }

    public byte[] codesDownload(String id, Map<String, Object> options) throws APIException {
        return parseResponseAsByteArray(doGetRequest(String.format("projects/%s/codes/%s/download", projectId, id), options));
    }

    public JsonObject tasksList(Map<String, Object> options) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/tasks", projectId), options));
    }

    public JsonObject tasksGet(String id) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/tasks/%s", projectId, id), null));
    }

    public JsonObject tasksCreate(String code_name, String payload, Map<String, Object> options) throws APIException {
        Map<String, Object> task = Params.create("code_name", code_name, "payload", payload);

        if (options != null) {
            task.putAll(options);
        }

        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();

        tasks.add(task);

        return parseResponseAsJson(doPostRequest(String.format("projects/%s/tasks", projectId), gson.toJson(Params.create("tasks", tasks))));
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

    public JsonObject tasksSetProgress(String id, Map<String, Object> options) throws APIException {
        return parseResponseAsJson(doPostRequest(String.format("projects/%s/tasks/%s/progress", projectId, id), gson.toJson(options)));
    }

    public JsonObject retryTask(String id, int delay) throws APIException {
        Map<String, Integer> options = new HashMap<String, Integer>();
        options.put("delay", delay);
        return parseResponseAsJson(doPostRequest(String.format("projects/%s/tasks/%s/retry", projectId, id), gson.toJson(options)));
    }

    public JsonObject schedulesList(Map<String, Object> options) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/schedules", projectId), options));
    }

    public JsonObject schedulesGet(String id) throws APIException {
        return parseResponseAsJson(doGetRequest(String.format("projects/%s/schedules/%s", projectId, id), null));
    }

    public JsonObject schedulesCreate(String code_name, String payload, Map<String, Object> options) throws APIException {
        Map<String, Object> schedule = Params.create("code_name", code_name, "payload", payload);

        if (options != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));

            if (options.containsKey("start_at") && options.get("start_at").getClass() == Date.class) {
                Date d = (Date) options.get("start_at");
                options.put("start_at", format.format(d));
            }

            if (options.containsKey("end_at") && options.get("end_at").getClass() == Date.class) {
                Date d = (Date) options.get("end_at");
                options.put("end_at", format.format(d));
            }

            schedule.putAll(options);
        }

        List<Map<String, Object>> schedules = new ArrayList<Map<String, Object>>();

        schedules.add(schedule);

        return parseResponseAsJson(doPostRequest(String.format("projects/%s/schedules", projectId), gson.toJson(Params.create("schedules", schedules))));
    }

    public JsonObject schedulesCancel(String id) throws APIException {
        return parseResponseAsJson(doPostRequest(String.format("projects/%s/schedules/%s/cancel", projectId, id), null));
    }
}
