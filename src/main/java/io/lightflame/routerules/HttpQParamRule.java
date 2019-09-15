package io.lightflame.routerules;

import java.util.Map;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpHeaderRule
 */
public class HttpQParamRule implements Rule<FullHttpRequest>{

    private String key;
    private String value;

    public HttpQParamRule(String k, String v) {
        this.key = k;
        this.value = v;
    }

    @Override
    public boolean isValid(FullHttpRequest req) {
        Map<String,String> queryParam = Utils.extractQueryParam(req.uri());
        if (!queryParam.containsKey(this.key)) return false;
        return queryParam.get(this.key).equals(this.value);
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