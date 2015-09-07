package io.iron.ironworker.client.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class WorkerHelper {
    String directory;
    String environment;
    String taskId;
    String scheduleId;
    String payloadPath;
    String payload;
    JsonObject payloadJson;

    private WorkerHelper() {
    }

    public static WorkerHelper fromArgs(String[] args) {
        WorkerHelper helper = new WorkerHelper();
        HashMap<String, String> params = extractParams(args);

        helper.environment = params.get("-e");
        helper.directory = (System.getenv("TASK_DIR") != null && !System.getenv("TASK_DIR").isEmpty()) ? System.getenv("TASK_DIR") : params.get("-d");
        helper.taskId = (System.getenv("TASK_ID") != null && !System.getenv("TASK_ID").isEmpty()) ? System.getenv("TASK_ID") : params.get("-id");
        helper.payloadPath = (System.getenv("PAYLOAD_FILE") != null && !System.getenv("PAYLOAD_FILE").isEmpty()) ? System.getenv("PAYLOAD_FILE") : params.get("-payload");
        helper.scheduleId = params.get("-schedule_id");

        return helper;
    }

    protected static HashMap<String, String> extractParams(String[] args) {
        HashSet<String> supportedParams = new HashSet<String>(Arrays.asList("-e", "-d", "-id", "-payload", "-schedule_id"));
        HashMap<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < args.length - 1; i++) {
            if (supportedParams.contains(args[i])) {
                params.put(args[i], args[i+1]);
                i++;
            }
        }
        return params;
    }

    public String getDirectoryPath() {
        return directory;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public String getPayloadPath() {
        return payloadPath;
    }

    public JsonObject getPayloadJson() throws IOException {
        if (payloadJson != null) {
            return payloadJson;
        }

        FileInputStream inputStream = new FileInputStream(payloadPath);
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

        payloadJson = new Gson().fromJson(getPayload(), JsonObject.class);

        reader.close();
        inputStream.close();
        return payloadJson;
    }

    public String getPayload() throws IOException {
        if (payload != null) {
            return payload;
        }

        FileInputStream inputStream = new FileInputStream(payloadPath);
        payload = new String(Files.readAllBytes(Paths.get(payloadPath)));
        return payload;
    }

    public <T> T getPayload(java.lang.Class<T> classOfT) throws IOException {
        FileInputStream inputStream = new FileInputStream(payloadPath);
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

        T result = new Gson().fromJson(getPayload(), classOfT);

        reader.close();
        inputStream.close();
        return result;
    }
}
