package io.iron.ironworker.client.builders;

import io.iron.ironworker.client.APIException;

import java.util.HashMap;
import java.util.Map;

public class ParamsObject {
    private Map<String, Object> params;

    public ParamsObject() {
        params = new HashMap<String, Object>();
    }

    public ParamsObject add(String key, Object value) {
        params.put(key, value);

        return this;
    }

    public Map<String, Object> create() {
        return params;
    }
}
