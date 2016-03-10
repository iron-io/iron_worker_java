package io.iron.ironworker.client.builders;

public class TaskOptions {
    public static TaskOptionsObject priority(int priority) {
        return (new TaskOptionsObject()).priority(priority);
    }

    public static TaskOptionsObject timeout(int timeout) {
        return (new TaskOptionsObject()).timeout(timeout);
    }

    public static TaskOptionsObject cluster(String cluster) {
        return (new TaskOptionsObject()).cluster(cluster);
    }

    public static TaskOptionsObject label(String label) {
        return (new TaskOptionsObject()).label(label);
    }

    public static TaskOptionsObject delay(int delay) {
        return (new TaskOptionsObject()).delay(delay);
    }
    
    public static TaskOptionsObject encryptionKey(String encryptionKey){
        return (new TaskOptionsObject()).encryptionKey(encryptionKey);
    }
    
    public static TaskOptionsObject encryptionKeyFile(String encryptionKeyFile){
        return (new TaskOptionsObject()).encryptionKeyFile(encryptionKeyFile);
    }

    protected TaskOptions() {
    }
}
