package io.lightflame.handle;


import com.google.gson.Gson;

import io.lightflame.annotations.Endpoint;
import io.lightflame.annotations.Handler;
import io.lightflame.http.HTTPRequest;
import io.lightflame.http.HTTPResponse;
import io.lightflame.http.HTTPSession;

/**
 * HelloWorldRest
 */
@Endpoint("/rest/user")
public class UserRestController {

    private Gson gson = new Gson();

    @Handler("/get")
    public HTTPResponse getUser(HTTPSession session, HTTPRequest request){
        UserDTO sr = new UserDTO("Daniel", 34);

        return new HTTPResponse()
            .setContent(gson.toJson(sr).getBytes())
            .setHeader("Content-Type", "application/json");
    }

    @Handler("/add")
    public HTTPResponse addUser(HTTPSession session, HTTPRequest request){
        // users.add(gson.fromJson(new String(session.readLines()), UserDTO.class));
        return null;
    }


}