package io.iron.test;


import io.iron.ironworker.client.entities.*;
import io.iron.ironworker.client.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class IronWorkerTest {
    private String queueName = "java-testing-queue";
    private Client client;

    @Before
    public void setUp() throws Exception {
        client = new Client("XXX", "YYY");
    }

    @Test
    public void testCreatingTask() throws IOException, APIException {
        String id = client.createTask("HelloWorkerMono");

        Assert.assertTrue(id.length() > 0);
    }

    @Test
    public void testGetTaskInfo() throws IOException, APIException, InterruptedException {
        String id = client.createTask("HelloWorkerMono");
        Thread.sleep(1000);
        TaskEntity task = client.getTask(id);

        Assert.assertTrue(task.getId().length() > 0);
        Assert.assertTrue(task.getCodeId().length() > 0);
        Assert.assertTrue(task.getCodeName().length() > 0);
        Assert.assertTrue(task.getStatus().length() > 0);
        Assert.assertTrue(task.getPayload().length() > 0);
        Assert.assertNotNull(task.getCreatedAt() != null);
        Assert.assertNotNull(task.getUpdatedAt() != null);
    }
}
