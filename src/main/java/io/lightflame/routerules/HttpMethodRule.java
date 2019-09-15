package io.lightflame.routerules;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

/**
 * HttpMethodRule
 */
public class HttpMethodRule implements Rule<FullHttpRequest>{

    private HttpMethod method;

    public HttpMethodRule(HttpMethod m) {
        this.method = m;
    }

    @Override
    public boolean isValid(FullHttpRequest req) {
        return req.method().equals(this.method);
    }

    @Override
    public RuleKind kind() {
        return HttpRuleKind.METHOD;
    }

    @Override
    public int score() {
        return 1;
    }

    
}