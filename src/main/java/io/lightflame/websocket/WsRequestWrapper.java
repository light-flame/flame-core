package io.lightflame.websocket;

/**
 * WsRequestWrapper
 */
public class WsRequestWrapper {

    private String request;
    private String uri;

    public WsRequestWrapper(String request, String uri) {
        this.request = request;
        this.uri = uri;
    }

    /**
     * @return the request
     */
    public String getRequest() {
        return request;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    
}