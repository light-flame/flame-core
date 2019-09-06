package io.lightflame.handle;

import com.google.gson.Gson;

import org.junit.Test;

import io.lightflame.dto.UserDTO;
import io.lightflame.functions.HttpInAdapterFunction;
import io.lightflame.httpAdapters.HttpInAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

/**
 * AdaptersTest
 */
public class AdaptersTest {

    @Test
    public void adapter1Test(){
        UserDTO usrDto = new UserDTO("name", 13);
        HttpInAdapterFunction<UserDTO> f1 =  new HttpInAdapter().jsonUnmarshall(UserDTO.class);

        FullHttpRequest req = new DefaultFullHttpRequest(
            HttpVersion.HTTP_1_1,
            HttpMethod.POST,
            "/"
        );
        req.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        Gson g = new Gson();
        
        String bArray = g.toJson(usrDto);
        req.content().writeBytes(bArray.getBytes());

        UserDTO u = f1.apply(req);
        System.out.println(u.getAge());
    }
}