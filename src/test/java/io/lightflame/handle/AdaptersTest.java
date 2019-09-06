package io.lightflame.handle;

import java.util.function.Function;

import com.google.gson.Gson;

import org.junit.Test;

import io.lightflame.dto.UserDTO;
import io.lightflame.functions.HttpInAdapterFunction;
import io.lightflame.functions.HttpOutAdapterFunction;
import io.lightflame.functions.httpAdapters.HttpInAdapter;
import io.lightflame.functions.httpAdapters.HttpOutAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

/**
 * AdaptersTest
 */
public class AdaptersTest {

    Gson g = new Gson();

    public Function<FullHttpRequest,FullHttpResponse> functionGroup(){
        HttpInAdapterFunction<UserDTO> jsonUnm =  new HttpInAdapter().jsonUnmarshall(UserDTO.class);
        HttpOutAdapterFunction<UserDTO> jsonMarsh = new HttpOutAdapter().jsonMarshall(UserDTO.class);
        Function<FullHttpRequest,FullHttpResponse> func = jsonUnm.andThen(jsonMarsh);
        return func;
    }

    @Test
    public void adapter1Test(){
        UserDTO usrDto = new UserDTO("name", 13);
        HttpInAdapterFunction<UserDTO> f1 =  new HttpInAdapter().jsonUnmarshall(UserDTO.class);
        FullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,"/");
        req.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        req.content().writeBytes(g.toJson(usrDto).getBytes());

        UserDTO u = f1.apply(req);
        assert(u.equals(usrDto));
    }
}