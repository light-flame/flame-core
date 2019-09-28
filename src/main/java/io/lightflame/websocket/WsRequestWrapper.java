package io.lightflame.websocket;

/**
 * WsRequestWrapper
 */
public class WsRequestWrapper {

    private int port;
    private String request;
    private String uri;

    WsRequestWrapper(String request, String uri, int port) {
        this.request = request;
        this.uri = uri;
        this.port = port;
    }

    /**
     * @return the request
     */
    public String getRequest() {
        return request;
    }

    public int getPort() {
        return port;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    
}