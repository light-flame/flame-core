package io.lightflame.http;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTPResponse
 */
public class HTTPResponse {

    private String version = "HTTP/1.1";
    private int responseCode = 200;
    private String responseReason = "OK";
    private Map<String, String> headers = new LinkedHashMap<String, String>();
    private byte[] content;

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    void addDefaultHeaders() {
        headers.put("Date", new Date().toString());
        headers.put("Server", "Java light-flame");
        headers.put("Connection", "close");
        headers.put("Content-Length", Integer.toString(content.length));
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseReason() {
        return responseReason;
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public byte[] getContent() {
        return content;
    }

    public HTTPResponse setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public HTTPResponse setResponseReason(String responseReason) {
        this.responseReason = responseReason;
        return this;
    }

    public HTTPResponse setContent(byte[] content) {
        this.content = content;
        return this;
    }

    public HTTPResponse setHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }
}