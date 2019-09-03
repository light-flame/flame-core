package io.lightflame.http;

import io.lightflame.bean.HttpBeanStore;
import io.lightflame.functions.HttpFunction;

/**
 * HTTPHandlers
 */
public class HTTPHandlers {

    public HTTPResponse getHandle(HTTPSession session, HTTPRequest request){
        HttpFunction function = new HttpBeanStore().getFunctionByRequest(request);
        if (function == null) {
            function = handler404();
        }
        return function.apply(session, request);
    }


    HttpFunction handler404() {
        return (session, request) -> {
            return new HTTPResponse()
                .setContent("nothing here... =(".getBytes())
                .setResponseCode(404);
        };
    }

}