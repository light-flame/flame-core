package io.lightflame.websocket;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class Session {

    private LoadingCache<String, ChannelHandlerContext> sessions;

    private CacheLoader<String, ChannelHandlerContext> loader(){
        return new CacheLoader<String, ChannelHandlerContext>() {
            public ChannelHandlerContext load(String key) throws Exception {
                return null;
            }
        };
    }

    Session() {
        sessions = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(loader());
    }

    public void addSession(String key, ChannelHandlerContext ch){
        sessions.put(key, ch);
    }

    public ChannelHandlerContext getSession(String key){
        return sessions.getUnchecked(key);
    }

    public ConcurrentMap<String, ChannelHandlerContext> getAllSessions(){
        return sessions.asMap();
    }
}
