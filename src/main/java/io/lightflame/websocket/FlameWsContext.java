package io.lightflame.websocket;


/**
 * FlameWebSocketCtx
 */
public class FlameWsContext {

    private String request;
    private String response;

    FlameWsContext(String r){
        this.request = r;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @return the response
     */
    String getResponse() {
        return response;
    }
    
}