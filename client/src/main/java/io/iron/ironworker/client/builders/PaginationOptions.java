package io.iron.ironworker.client.builders;

public class PaginationOptions {
    public static PaginationOptionsObject page(int page) {
        return (new PaginationOptionsObject()).page(page);
    }

    public static PaginationOptionsObject perPage(int perPage) {
        return (new PaginationOptionsObject()).perPage(perPage);
    }

    protected PaginationOptions() {
    }
}
