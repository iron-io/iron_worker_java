package io.iron.test;

import io.iron.ironworker.client.entities.*;
import io.iron.ironworker.client.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.io.IOException;

public class IronWorkerTest {
    private String queueName = "java-testing-queue";
    private Client client;

    @Before
    public void setUp() throws Exception {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
        } catch(FileNotFoundException fnfe) {
            System.out.println("config.properties not found");
            input = new FileInputStream("../../config.properties"); //maven release hack
        }
        prop.load(input);
        String token = prop.getProperty("token");
        String projectId = prop.getProperty("project_id");
        client = new Client(token, projectId);
    }

    @Test
    public void testCreatingTask() throws IOException, APIException {
        String id = client.createTask("MonoWorker101").getId();

        Assert.assertTrue(id.length() > 0);
    }

    @Test
    public void testViewingTaskInfoAfterTaskCreation() throws IOException, APIException {
        TaskEntity task = client.createTask("MonoWorker101");

        Assert.assertTrue(task.getId().length() > 0);
        Assert.assertNull(task.getCodeId());
        Assert.assertNull(task.getCodeName());
        Assert.assertNull(task.getStatus());
        Assert.assertNull(task.getPayload());
        Assert.assertNull(task.getCreatedAt());
        Assert.assertNull(task.getUpdatedAt());
    }

    @Test
    public void testGetTaskInfo() throws IOException, APIException, InterruptedException {
        String id = client.createTask("MonoWorker101").getId();
        Thread.sleep(1000);
        TaskEntity task = client.getTask(id);

        Assert.assertTrue(task.getId().length() > 0);
        Assert.assertTrue(task.getCodeId().length() > 0);
        Assert.assertTrue(task.getCodeName().length() > 0);
        Assert.assertTrue(task.getStatus().length() > 0);
        Assert.assertTrue(task.getPayload().length() > 0);
        Assert.assertNotNull(task.getCreatedAt());
        Assert.assertNotNull(task.getUpdatedAt());
    }

    @Test
    public void testTaskReloadInfo() throws IOException, APIException, InterruptedException {
        TaskEntity task = client.createTask("MonoWorker101");
        Thread.sleep(1000);
        client.reload(task);

        Assert.assertTrue(task.getId().length() > 0);
        Assert.assertTrue(task.getCodeId().length() > 0);
        Assert.assertTrue(task.getCodeName().length() > 0);
        Assert.assertTrue(task.getStatus().length() > 0);
        Assert.assertTrue(task.getPayload().length() > 0);
        Assert.assertNotNull(task.getCreatedAt());
        Assert.assertNotNull(task.getUpdatedAt());
    }

    @Test
    public void testSchedulingTask() throws IOException, APIException {
        String id = client.createSchedule("MonoWorker101").getId();

        Assert.assertTrue(id.length() > 0);
    }

    @Test
    public void testSchedulingInfo() throws IOException, APIException, InterruptedException {
        String id = client.createSchedule("MonoWorker101").getId();
        Thread.sleep(1000);
        ScheduleEntity schedule = client.getSchedule(id);

        Assert.assertTrue(schedule.getId().length() > 0);
        Assert.assertTrue(schedule.getCodeName().length() > 0);
        Assert.assertTrue(schedule.getStatus().length() > 0);
        Assert.assertTrue(schedule.getPayload().length() > 0);
        Assert.assertNotNull(schedule.getCreatedAt());
        Assert.assertNotNull(schedule.getUpdatedAt());
    }

    @Test
    public void testReloadSchedule() throws IOException, APIException, InterruptedException {
        ScheduleEntity schedule = client.createSchedule("MonoWorker101");
        Thread.sleep(1000);
        client.reload(schedule);

        Assert.assertTrue(schedule.getId().length() > 0);
        Assert.assertTrue(schedule.getCodeName().length() > 0);
        Assert.assertTrue(schedule.getStatus().length() > 0);
        Assert.assertTrue(schedule.getPayload().length() > 0);
        Assert.assertNotNull(schedule.getCreatedAt());
        Assert.assertNotNull(schedule.getUpdatedAt());
    }
}
