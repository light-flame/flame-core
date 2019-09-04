package io.lightflame.http;

import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import com.google.gson.Gson;

import org.jgroups.nio.MockSocketChannel;
import org.junit.Before;
import org.junit.Test;

import io.lightflame.handle.UserDTO;
import io.lightflame.http.HTTPHandlers;
import io.lightflame.http.HTTPMethod;
import io.lightflame.http.HTTPRequest;
import io.lightflame.http.HTTPResponse;
import io.lightflame.http.HTTPSession;

/**
 * SimpleHelloWorldTest
 */
public class SimpleHelloWorldTest {

    MockSocketChannel client = new MockSocketChannel();
    HTTPSession session;
    HTTPHandlers handlers;
    Gson gson = new Gson();

    @Before
    public void initAll()throws Exception{
        client.bytesToRead("heeelo".getBytes());
        session = new HTTPSession(client);
        session.readData();
        String s = new String(session.writeBody());
        handlers = new HTTPHandlers();
        // handlers.createHandlers(SimpleHelloWorldTest.class);
    }

    // @Test
    // public void getUser()throws Exception{
    //     HTTPRequest request = new HTTPRequest(HTTPMethod.GET,"/rest/user/get",null); 
    //     HTTPResponse resp =  handlers.getHandle(session, request);

    //     UserDTO userDTO =  gson.fromJson(new String(resp.getContent()), UserDTO.class);

    //     assertTrue(userDTO.getName().equals("Daniel"));
    //     assertTrue(userDTO.getAge() == 34);
    // }

    // @Test
    // public void testBuffer(){
    //     final ByteBuffer buffer = ByteBuffer.wrap("Hello world!".getBytes());

    //     byte[] output = new byte[buffer.capacity()];
    //     buffer.get(output);
    //     String k = new String(output);

    //     byte[] output2 = new byte[buffer.capacity()];
    //     buffer.get(output2);
    //     String k2 = new String(output2);

    //     System.out.println(k);
    // }

}