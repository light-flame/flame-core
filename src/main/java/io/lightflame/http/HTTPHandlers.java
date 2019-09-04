package io.lightflame.http;

import java.util.function.Function;

import io.lightflame.bean.DefaultHttpStore;
import io.lightflame.functions.DefaultHttpFunction;

/**
 * HTTPHandlers
 */
public class HTTPHandlers {

    public HTTPResponse getHandle(HTTPSession session){
        Function<HTTPSession, HTTPResponse> function = new DefaultHttpStore()
            .getFunctionByRequest(session.getRequest());
        if (function == null) {
            function = handler404();
        }
        return function.apply(session);
    }


    DefaultHttpFunction handler404() {
        return (session) -> {
            return new HTTPResponse()
                .setContent("nothing here... =(".getBytes())
                .setResponseCode(404);
        };
    }

}