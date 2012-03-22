package io.iron.ironworker.client.codes;

public class BaseCode {
    private String name;
    private String file;
    private String runtime;
    private String runner;

    public BaseCode(String name, String file, String runtime, String runner) {
        this.name = name;
        this.file = file;
        this.runtime = runtime;
        this.runner = runner;
    }

    public String getName() {
        return name;
    }

    public String getFile() {
        return file;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getRunner() {
        return runner;
    }
}
