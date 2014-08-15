package io.iron.test;


import io.iron.ironworker.client.entities.*;
import io.iron.ironworker.client.*;
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
        TaskEntity helloWorkerMono = client.createTask("HelloWorkerMono");
    }
}
