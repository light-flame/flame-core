package io.lightflame.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * DefaultExceptionStore
 */
public class DefaultExceptionStore {
    static private Map<Exception, Function<?,?>> functionMap = new HashMap<>();

    public Function<?, ?> runFunctionByException(Exception e){
        Function<?,?> f = functionMap.get(e);
        if (f == null){
            f = defaultFunction();
        }
        return functionMap.get(e);
    }

    public void add(Exception e, Function<?,?> function){
        functionMap.put(e, function);
    }

    private Function<Object, Optional<Void>> defaultFunction(){
        return (req) -> {
            return null;
        };
    }
    
}