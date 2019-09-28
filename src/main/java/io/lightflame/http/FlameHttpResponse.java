package io.lightflame.http;


import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class FlameHttpResponse {
   private FullHttpResponse response;

   public FlameHttpResponse(HttpResponseStatus status, ByteBuf buffer){
       this.response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buffer);
   }

   FullHttpResponse response(){
       return this.response;
   }
}
