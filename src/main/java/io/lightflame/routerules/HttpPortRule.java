package io.lightflame.routerules;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpPortRule
 */
public class HttpPortRule implements Rule<FullHttpRequest>{

    private Integer port;

    public HttpPortRule(int p) {
        this.port = p;
    }

    @Override
    public boolean isValid(FullHttpRequest req) {
        String host = req.headers().get("host");
        String hostPort = host.split(":")[1];
        if (this.port.toString().equals(hostPort)){
            return true;
        }
        return false;
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