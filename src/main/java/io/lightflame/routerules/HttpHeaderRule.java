package io.lightflame.routerules;

import java.util.Map.Entry;

import io.netty.handler.codec.http.HttpHeaders;

/**
 * HttpHeaderRule
 */
public class HttpHeaderRule implements Rule<Entry<String,String>, HttpHeaders>{

    private Entry<String, String> condHeader;

    @Override
    public boolean isValid(HttpHeaders h) {
        String value = h.get(condHeader.getKey());
        return value.equals(condHeader.getValue());
    }

    @Override
    public RuleKind kind() {
        return HttpRuleKind.HEADER;
    }

    @Override
    public void setParam(Entry<String, String> obj) {
        this.condHeader = obj;
    }

    @Override
    public int score() {
        return 1;
    }

    
}