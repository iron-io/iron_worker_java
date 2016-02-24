package io.iron.ironworker.client.builders;

import java.util.HashMap;
import java.util.Map;

public class PaginationOptionsObject {
    private final Map<String, Object> options;

    public PaginationOptionsObject() {
        options = new HashMap<String, Object>();
    }

    public PaginationOptionsObject page(int page) {
        options.put("page", page);

        return this;
    }

    public PaginationOptionsObject perPage(int perPage) {
        options.put("per_page", perPage);

        return this;
    }

    public Map<String, Object> create() {
        return options;
    }
}
