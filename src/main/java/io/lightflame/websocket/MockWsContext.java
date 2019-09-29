package io.lightflame.websocket;

public class MockWsContext {
    static public FlameWsContext make(String request){
        return make(request, null);
    }
    static public FlameWsContext make(String request, String uri){
        return new FlameWsContext(new WsRequestWrapper(request,uri,0));
    }
}
