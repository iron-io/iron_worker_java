package io.iron.ironworker.client.builders;

import io.iron.ironworker.client.APIException;

import java.util.HashMap;
import java.util.Map;

public class Params {
    public static Map<String, Object> create(Object... os) throws APIException {
        if (os.length % 2 != 0) {
            throw new APIException("Odd params number", null);
        }

        Map<String, Object> params = new HashMap<String, Object>();

        for (int i = 0; i < os.length; i += 2) {
            params.put(os[i].toString(), os[i + 1]);
        }

        return params;
    }

    public static ParamsObject add(String key, Object value) {
        return (new ParamsObject()).add(key, value);
    }

    protected Params() {
    }
}
