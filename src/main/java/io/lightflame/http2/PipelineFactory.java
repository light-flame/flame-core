package io.lightflame.http2;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;

/**
 * PipelineFactory
 */
public class PipelineFactory extends ChannelInitializer<SocketChannel>{

    private final SslContext sslCtx;

    public PipelineFactory(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        // p.addLast(new HttpServerCodec());


        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(65536));
        p.addLast(new WebSocketServerCompressionHandler());
        p.addLast(new WebSocketServerProtocolHandler("/ws", null, true));

        p.addLast(new WebSocketFrameHandler());
        p.addLast(new FlameHttpServerHandler());
    }

    static public ChannelHandler[] createHttpChannels(){
        ChannelHandler[] chs = {
            new HttpServerCodec(),
            new FlameHttpServerHandler()
        };
        return chs;
    }
}