package io.iron.ironworker.client;

import io.iron.ironworker.client.entities.CodeEntity;
import io.iron.ironworker.client.entities.Codes;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: Julien
 * Date: 22/03/12
 * Time: 17:24
 */
public class ClientTest {

    private static String _ironToken;
    private static String _ironProjectId;

    @BeforeClass
    public static void init() {
        _ironToken = "P7g9m72eTekPHd0bNCkCeBytrhQ";
        _ironProjectId = "4f63775f8de4561d19000804";
    }

    @Test
    public void shouldHaveEnvironmentVariable() throws APIException {
        Client client = new Client(_ironToken, _ironProjectId);
        Codes codes = client.getCodes();
        System.out.println(codes);
    }
}
