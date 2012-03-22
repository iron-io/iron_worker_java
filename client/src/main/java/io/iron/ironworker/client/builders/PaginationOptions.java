package io.iron.ironworker.client.builders;

import java.util.HashMap;
import java.util.Map;

public class PaginationOptions {
    private Map<String, Object> options;

    public PaginationOptions() {
        options = new HashMap<String, Object>();
    }

    public PaginationOptions page(int page) {
        options.put("page", page);

        return this;
    }

    public PaginationOptions perPage(int perPage) {
        options.put("perPage", perPage);

        return this;
    }

    public Map<String, Object> create() {
        return options;
    }
}
