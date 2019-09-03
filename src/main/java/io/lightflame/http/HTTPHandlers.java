package io.lightflame.http;

import io.lightflame.bean.DefaultHttpStore;
import io.lightflame.functions.DefaultHttpFunction;

/**
 * HTTPHandlers
 */
public class HTTPHandlers {

    public HTTPResponse getHandle(HTTPSession session, HTTPRequest request){
        DefaultHttpFunction function = new DefaultHttpStore().getFunctionByRequest(request);
        if (function == null) {
            function = handler404();
        }
        return function.apply(session, request);
    }


    DefaultHttpFunction handler404() {
        return (session, request) -> {
            return new HTTPResponse()
                .setContent("nothing here... =(".getBytes())
                .setResponseCode(404);
        };
    }

}