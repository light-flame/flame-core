package io.lightflame.http;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * HTTPRequest
 */
public class HTTPRequest {

    private final String raw;
    private HTTPMethod method;
    private String location;
    private String version;
    private Map<String, String> headers = new HashMap<String, String>();

    @Override
    public String toString() {
        return String.format("%s - %s", location, method);
    }

    public HTTPRequest(String raw) {
        this.raw = raw;
        parse();
    }

    // only for test propose
    public HTTPRequest(HTTPMethod method, String location, Map<String, String> headers) {
        this.raw = "";
        this.method = method;
        this.location = location;
        this.headers = headers;
    }

    private void parse() {
        // parse the first line
        StringTokenizer tokenizer = new StringTokenizer(raw);
        this.method = HTTPMethod.valueOf(tokenizer.nextToken().toUpperCase());
        this.location = tokenizer.nextToken();
        this.version = tokenizer.nextToken();
        // parse the headers
        String[] lines = raw.split("\r\n");
        for (int i = 1; i < lines.length; i++) {
            String[] keyVal = lines[i].split(":", 2);
            if (keyVal.length != 2){
                continue;
            }
            headers.put(keyVal[0].trim(), keyVal[1].trim());
        }
    }

    public HTTPMethod getMethod() {
        return method;
    }


    public String getVersion() {
        return version;
    }

    public String getLocation() {
        return location;
    }

    public String getHead(String key) {
        return headers.get(key);
    }
    
}