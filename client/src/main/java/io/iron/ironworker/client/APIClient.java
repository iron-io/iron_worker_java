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
                Thread.sleep((long)(Math.pow(4, currentTry) * 100 * Math.random()));
            } catch (InterruptedException e) {
                throw new APIException(null, e);
            }

            currentTry++;
        }

        return response;
    }

    private void setRequestURI(HttpRequestBase request, String method, Map<String, String> params) throws APIException {
        List<NameValuePair> qParams = new ArrayList<NameValuePair>();

        if (params != null) {
            for (String key : params.keySet()) {
                qParams.add(new BasicNameValuePair(key, params.get(key)));
            }
        }

        String query = URLEncodedUtils.format(qParams, "UTF-8");

        if (query.equals("")) {
            query = null;
        }

        URI uri = null;
        try {
            uri = URIUtils.createURI(scheme, host, port, "" + apiVersion + "/" + method, query, null);
        } catch (URISyntaxException e) {
            throw new APIException(null, e);
        }

        request.setURI(uri);
    }

    private HttpResponse doRequest(HttpRequestBase request, String method, Map<String, String> params) throws APIException {
        setRequestURI(request, method, params);

        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "OAuth " + token);
        request.addHeader("User-Agent", userAgent);

        return doRequestExecute(request);
    }

    private HttpResponse doFileRequest(File file, String method, Map<String, String> params) throws APIException {
        HttpPost request = new HttpPost();

        setRequestURI(request, method, null);

        request.addHeader("Authorization", "OAuth " + token);
        request.addHeader("User-Agent", userAgent);

        MultipartEntity entity = new MultipartEntity();

        try {
            entity.addPart("data", new StringBody((new Gson()).toJson(params)));
            entity.addPart("file", new FileBody(file));
        } catch (UnsupportedEncodingException e) {
            throw new APIException(null, e);
        }

        request.setEntity(entity);

        return doRequestExecute(request);
    }

    private InputStream parseResponseGeneral(HttpResponse response) throws APIException {
        InputStream result = null;
        try {
            result = response.getEntity().getContent();
        } catch (IOException e) {
            throw new APIException(null, e);
        }

        if (response.getStatusLine().getStatusCode() != 200) {
            String r = null;
            try {
                r = IOUtils.toString(result);
            } catch (IOException e) {
                throw new APIException(null, e);
            }

            throw new APIException(r, null);
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

    public JsonObject codesList(Map<String, String> params) throws APIException {
        return parseResponseAsJson(doRequest(new HttpGet(), "projects/" + projectId + "/codes", params));
    }

    public JsonObject codesGet(String id) throws APIException {
        return parseResponseAsJson(doRequest(new HttpGet(), "projects/" + projectId + "/codes/" + id, null));
    }
    
    public JsonObject codesCreate(String name, String file, String runtime, String runner) throws APIException {
        Map<String, String> params = new HashMap<String, String>();

        params.put("name", name);
        params.put("runtime", runtime);
        params.put("file_name", runner);
        
        File f = new File(file);
        if (!f.exists()) {
            throw new APIException("File " + file + " not found", null);
        }

        return parseResponseAsJson(doFileRequest(f, "projects/" + projectId + "/codes", params));
    }

    public JsonObject codesDelete(String id) throws APIException {
        return parseResponseAsJson(doRequest(new HttpDelete(), "projects/" + projectId + "/codes/" + id, null));
    }

    public JsonObject codesRevisions(String id, Map<String, String> params) throws APIException {
        return parseResponseAsJson(doRequest(new HttpGet(), "projects/" + projectId + "/codes/" + id + "/revisions", params));
    }

    public byte[] codesDownload(String id, Map<String, String> params) throws APIException {
        return parseResponseAsByteArray(doRequest(new HttpGet(), "projects/" + projectId + "/codes/" + id + "/download", params));
    }
}
