package io.iron.ironworker.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.iron.ironworker.client.builders.ParamsObject;
import io.iron.ironworker.client.builders.ScheduleOptionsObject;
import io.iron.ironworker.client.entities.ScheduleEntity;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * User: Julien
 * Date: 21/03/12
 * Time: 23:42
 */
public class APIClientTest {
    //System environment variables for iron worker.
    public static String IRON_IO_TOKEN = "IRON_IO_TOKEN";
    public static String IRON_IO_PROJECT_ID = "IRON_IO_PROJECT_ID";
    public static Map<String, Object> IRON_IO_PARAMS = null;
    //Default worker
    public static final String WORKER_NAME = "TestUnitWorker";
    public static final String WORKER_FILE = "runner.rb";
    public static final String WORKER_FILE_ZIP = "hello_world_worker.zip";
    public static final String WORKER_RUNTIME = "ruby";
    //Token and project
    private static String _ironToken;
    private static String _ironProjectId;
    private static String _codeId;

    @BeforeClass
    public static void init() {
        _ironToken = System.getenv(IRON_IO_TOKEN);
        _ironProjectId = System.getenv(IRON_IO_PROJECT_ID);
    }

    @Test
    public void shouldHaveEnvironmentVariable() {
        assertNotNull(IRON_IO_PROJECT_ID + " environment variable not set", _ironProjectId);
        assertNotNull(IRON_IO_TOKEN + " environment variable not set", _ironToken);
    }

    /*@Test
    public void shouldCodesCreate() throws APIException, ParseException {
        Client client = new Client("P7g9m72eTekPHd0bNCkCeBytrhQ", "4f63775f8de4561d19000804");
        //params
        ParamsObject paramsObject = new ParamsObject();
        paramsObject.add("test", "http://");
        //options
        ScheduleOptionsObject scheduleOptionsObject = new ScheduleOptionsObject();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        scheduleOptionsObject.startAt(simpleDateFormat.parse("08/04/2012 23:45:00"));
        ScheduleEntity entity = client.createSchedule("JavaHelloWorker", paramsObject, scheduleOptionsObject);
    }*/

    @Test(expected = APIException.class)
    public void shouldConnexionFail() throws APIException {
        APIClient client = new APIClient("badToken", "badProject");
        client.codesList(IRON_IO_PARAMS);
    }

    @Test
    public void shouldCodesCreate() throws APIException {
        APIClient client = new APIClient(_ironToken, _ironProjectId);
        URL resource = getClass().getClassLoader().getResource(WORKER_FILE_ZIP);
        JsonObject codeCreate = client.codesCreate(
                WORKER_NAME, resource.getFile(), WORKER_RUNTIME, WORKER_FILE);
        assertEquals("Upload successful.", codeCreate.get("msg").getAsString());
    }

    @Test
    public void shouldCodesList() throws APIException {
        APIClient client = new APIClient(_ironToken, _ironProjectId);
        JsonObject codesList = client.codesList(IRON_IO_PARAMS);
        assertNotNull(codesList);
        JsonArray codes = codesList.getAsJsonArray("codes");
        JsonObject code = fetchCodeFromName(codes, WORKER_NAME);
        _codeId = checkCodePackageValidity(code);
    }

    @Test
    public void shouldCodesGet() throws APIException {
        APIClient client = new APIClient(_ironToken, _ironProjectId);
        JsonObject code = client.codesGet(_codeId);
        checkCodePackageValidity(code);
    }

    @Test
    public void shouldCodesRevisions() throws APIException {
        APIClient client = new APIClient(_ironToken, _ironProjectId);
        JsonObject codeRevisions = client.codesRevisions(_codeId, IRON_IO_PARAMS);
        JsonArray revisions = codeRevisions.getAsJsonArray("revisions");
        assertEquals(1, revisions.size());
        checkCodeRevisionValidity(revisions.iterator().next().getAsJsonObject());
    }

    @Test
    public void shouldCodesDownload() throws APIException, IOException {
        APIClient client = new APIClient(_ironToken, _ironProjectId);
        byte[] byteFromIron = client.codesDownload(_codeId, IRON_IO_PARAMS);
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(WORKER_FILE_ZIP);
        byte[] bytesFromClient = IOUtils.toByteArray(resourceAsStream);
        assertArrayEquals(bytesFromClient, byteFromIron);
    }

    @Test
    @Ignore //TODO : activate test when REST API could re enable deleted worker
    public void shouldCodesDelete() throws APIException {
        APIClient client = new APIClient(_ironToken, _ironProjectId);
        JsonObject deleteMessage = client.codesDelete(_codeId);
        assertNotNull(deleteMessage);
        assertEquals("Deleted", deleteMessage.get("msg").getAsString());
    }


    private void checkCodeRevisionValidity(JsonObject revision) {
        assertNotNull(revision);
        assertNotNull(revision.get("id").getAsString());
        assertNotNull(revision.get("code_id").getAsString());
        assertNotNull(revision.get("project_id").getAsString());
        assertNotNull(revision.get("rev").getAsString());
        assertEquals(WORKER_RUNTIME, revision.get("runtime").getAsString());
        assertEquals(WORKER_NAME, revision.get("name").getAsString());
        assertEquals(WORKER_FILE, revision.get("file_name").getAsString());
    }

    private String checkCodePackageValidity(JsonObject code) {
        assertNotNull(code);
        String id = code.get("id").getAsString();
        assertNotNull(id);
        assertNotNull(code.get("project_id").getAsString());
        assertNotNull(code.get("latest_change").getAsString());
        return id;
    }

    private JsonObject fetchCodeFromName(JsonArray codes, String name) {
        for (JsonElement code : codes) {
            String codeName = code.getAsJsonObject().get("name").getAsString();
            assertNotNull(codeName);
            if (codeName.equals(name)) {
                return code.getAsJsonObject();
            }
        }
        throw new IllegalArgumentException("Can't find previously uploaded worker name : " + name);
    }
}

