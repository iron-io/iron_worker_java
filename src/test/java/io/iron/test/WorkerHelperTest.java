package io.iron.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import io.iron.ironworker.client.helpers.WorkerHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class WorkerHelperTest extends Assert {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReadingAttributes() {
        WorkerHelper helper = WorkerHelper.fromArgs(
                new String[]{
                        "-e", "staging",
                        "-d", "/tmp/worker",
                        "-id", "550f8f757eebbd020500eb16",
                        "-payload", "/tmp/worker/payload.json",
                        "-schedule_id", "0f8f757550205eebbd00eb16"
                });
        assertEquals("staging",                  helper.getEnvironment());
        assertEquals("/tmp/worker",              helper.getDirectoryPath());
        assertEquals("550f8f757eebbd020500eb16", helper.getTaskId());
        assertEquals("/tmp/worker/payload.json", helper.getPayloadPath());
        assertEquals("0f8f757550205eebbd00eb16", helper.getScheduleId());
    }

    @Test
    public void testIgnoringExtraAttributes() {
        WorkerHelper helper = WorkerHelper.fromArgs(
                new String[]{
                        "-try-to-read-this",
                        "-e", "staging",
                        "-d", "/tmp/worker",
                        "-provider", "iron.io",
                        "-id", "550f8f757eebbd020500eb16"
                });
        assertEquals("staging",                  helper.getEnvironment());
        assertEquals("/tmp/worker",              helper.getDirectoryPath());
        assertEquals("550f8f757eebbd020500eb16", helper.getTaskId());
    }

    @Test
    public void testParsingPayloadToObject() throws IOException {
        TestPayload payload = new TestPayload("test-name-1", 200, (float) 49.99);
        WorkerHelper helper = createHelperWithRealPayload("/tmp/worker/tests/payload.json", payload);
        TestPayload realPayload = helper.getPayload(TestPayload.class);

        assertEquals(payload.name,      realPayload.name);
        assertEquals(payload.maxAmount, realPayload.maxAmount);
        assertEquals(payload.price,     realPayload.price, (float)0.00001);
    }

    @Test
    public void testParsingPayloadToJson() throws IOException {
        TestPayload payload = new TestPayload("test-name-2", 19, (float) 33.333);
        WorkerHelper helper = createHelperWithRealPayload("/tmp/worker/tests/payload.json", payload);
        JsonObject realPayload = helper.getPayloadJson();

        assertEquals(payload.name,      realPayload.get("name").getAsString());
        assertEquals(payload.maxAmount, realPayload.get("max_amount").getAsInt());
        assertEquals(payload.price,     realPayload.get("price").getAsFloat(), (float) 0.00001);
    }

    @Test
    public void testParsingPayloadToString() throws IOException {
        TestPayload payload = new TestPayload("test-name-3", 66, (float) 99.99);
        WorkerHelper helper = createHelperWithRealPayload("/tmp/worker/tests/payload.json", payload);
        String realPayload = helper.getPayload();

        System.out.println(realPayload);
        assertTrue(realPayload.matches(".*\"name\":\\s*\"test-name-3\".*"));
        assertTrue(realPayload.matches(".*\"max_amount\":\\s*66.*"));
        assertTrue(realPayload.matches(".*\"price\":\\s*99.99.*"));
    }

    private WorkerHelper createHelperWithRealPayload(String path, TestPayload payload) throws IOException {
        File payloadFile = new File(path);
        payloadFile.mkdirs();
        payloadFile.delete();

        String contents = new Gson().toJson(payload);
        PrintWriter out = new PrintWriter(path);
        out.print(contents);
        out.flush();
        out.close();

        return WorkerHelper.fromArgs(new String[]{"-payload", path});
    }

    class TestPayload {
        String name;
        @SerializedName("max_amount")
        int maxAmount;
        float price;

        TestPayload(String name, Integer maxAmount, float price) {
            this.name = name;
            this.maxAmount = maxAmount;
            this.price = price;
        }
    }
}
