package io.lightflame.websocket;


/**
 * FlameWebSocketCtx
 */
public class FlameWsContext{

    private String request;
    private String response;

    FlameWsContext(String r){
        this.request = r;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    String getResponse() {
        return response;
    }

}