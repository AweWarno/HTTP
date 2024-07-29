package dev.fedichkin;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private final String path;
    private final Map<String, String> queryParams = new HashMap<>();

    public Request(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void addQueryParam(String key, String value) {
        queryParams.put(key, value);
    }

    public String getQueryParam(String name) {
        return queryParams.get(name);
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }
}