package io.lightflame.websocket;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.AttributeKey;

public class WsAttributes {

    static final AttributeKey<String> uriAttKey = AttributeKey.valueOf("request.uri");
    static final AttributeKey<String> portAttKey = AttributeKey.valueOf("request.port");
    static final AttributeKey<String> requestAttKey = AttributeKey.valueOf("request.message");
    static final AttributeKey<HttpHeaders> headersAttrKey = AttributeKey.valueOf("request.headers");

}
