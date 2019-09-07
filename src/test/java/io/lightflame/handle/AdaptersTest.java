package io.lightflame.handle;

import java.util.function.Function;

import com.google.gson.Gson;

import org.junit.Test;

import io.lightflame.dto.UserDTO;
import io.lightflame.functions.httpAdapters.HttpInAdapter;
import io.lightflame.http2.PipelineFactory;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * AdaptersTest
 */
public class AdaptersTest {

    Gson g = new Gson();

    @Test
    public void adapter1Test(){
        UserDTO usrDto = new UserDTO("name", 13);
        Function<FullHttpRequest, UserDTO> f1 =  new HttpInAdapter().jsonUnmarshall(UserDTO.class);
        FullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.POST,"/");
        req.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        req.content().writeBytes(g.toJson(usrDto).getBytes());

        UserDTO u = f1.apply(req);
        assert(u.equals(usrDto));
    }

    @Test
    public void test2(){
        EmbeddedChannel channel = new EmbeddedChannel(PipelineFactory.createChannels());

        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, 
                HttpMethod.GET, 
                "/calculate?a=10&b=5",
                Unpooled.copiedBuffer("Hello", CharsetUtil.UTF_8)
        );
        httpRequest.headers().add("Operator", "Add");
        channel.writeInbound(httpRequest);
        long inboundChannelResponse = channel.readInbound();
        System.out.println();
    }
}