package io.lightflame.routerules;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpHeaderRule
 */
public class HttpHeaderRule implements Rule<FullHttpRequest>{

    private String key;
    private String value;

    public HttpHeaderRule(String k, String v) {
        this.key = k;
        this.value = v;
    }

    @Override
    public boolean isValid(FullHttpRequest req) {
        String v = req.headers().get(key);
        return v.equals(this.value);
    }

    @Override
    public RuleKind kind() {
        return HttpRuleKind.HEADER;
    }


    @Override
    public int score() {
        return 1;
    }

    
}