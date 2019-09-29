package io.lightflame.websocket;


/**
 * FlameWebSocketCtx
 */
public class FlameWsContext{

    private String request;

    FlameWsContext(WsRequestWrapper rw){
        this.request = rw.getRequest();
    }

    public String getRequest() {
        return request;
    }
}