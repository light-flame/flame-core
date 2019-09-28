package io.lightflame.routerules;

import io.lightflame.websocket.WsRequestWrapper;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpPortRule
 */
public class WsPortRule implements Rule<WsRequestWrapper>{

    private Integer port;

    public WsPortRule(int p) {
        this.port = p;
    }

    @Override
    public boolean isValid(WsRequestWrapper req) {
        return req.getPort() == this.port;
    }

    @Override
    public RuleKind kind() {
        return HttpRuleKind.PORT;
    }

    @Override
    public int score() {
        return 0;
    }

    
}