package io.iron.ironworker.client.codes;

public class JavaCode extends BaseCode {
    public JavaCode(String name, String file) {
        super(name, file, "sh", "__runner__.sh");
    }
    public JavaCode(String name, String file, String stack) {
        super(name, file, stack, "sh", "__runner__.sh");
    }
}
