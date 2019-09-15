package io.lightflame.routerules;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpPathRule
 */
public class HttpPathRule implements Rule<FullHttpRequest>{
    private String[] segments;

    public HttpPathRule(String obj) {
        this.segments = Utils.extractSegments(obj);
    }

    @Override
    public boolean isValid(FullHttpRequest req) {
        String[] incomeSegments = Utils.extractSegments(req.uri());

        if (incomeSegments.length != segments.length){
            return false;
        }
        for (int i=0;i < incomeSegments.length ;i++){
            String incomeSegm = incomeSegments[i];
            String conditionSegm = this.segments[i];
            if (conditionSegm.startsWith("{")){
                continue;
            }
            if (!incomeSegm.equals(conditionSegm)){
                return false;
            }
        }
        return true;
    }

    @Override
    public RuleKind kind() {
        return HttpRuleKind.PATH;
    }

    @Override
    public int score() {
        return this.segments.length * 2;
    }

    
}