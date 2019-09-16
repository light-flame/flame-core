package io.lightflame.websocket;

import java.util.HashMap;
import java.util.Map;


/**
 * DefaultExceptionStore
 */
public class FlameWsExceptionStore {
    static private Map<Exception, ExceptionWsFunction> functionMap = new HashMap<>();

    public ExceptionWsFunction getFunction(Throwable e){
        ExceptionWsFunction f = functionMap.get(e);
        if (f == null){
            f = defaultFunction();
        }
        return f;
    }

    public void add(Exception e, ExceptionWsFunction function){
        functionMap.put(e, function);
    }

    private ExceptionWsFunction defaultFunction(){
        return (e) -> {
            e.printStackTrace();
        };
    }
    
}