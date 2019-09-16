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

    public String getResponse(){
        return this.response;
    }
    
}