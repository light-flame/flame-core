package io.lightflame.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.lightflame.functions.HttpInAdapter;
import io.lightflame.functions.HttpOutAdapter;

/**
 * AdaptorHttpStore
 */
public class AdapterHttpStore {

    static private Map<String, Function<?,?>> functionMap = new HashMap<>();

        // Criar um adaptor
    // ([in: Request/Session out: E] - [in: E out: D] - [in: D out: Response])


    
}

class AdapterHttpEntity<E,D> {
    private HttpInAdapter<E> adapterIN;
    private Function<E,D> function;
    private HttpOutAdapter<D> adapterOUT;


    public void getEntity(){
        adapterOUT.apply(function.apply(adapterIN.apply(null, null)));
    }
}