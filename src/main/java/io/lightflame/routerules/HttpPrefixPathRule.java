package io.lightflame.routerules;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpPrefixPathRule
 */
public class HttpPrefixPathRule implements Rule<FullHttpRequest>{

    private String[] segments;

    public HttpPrefixPathRule(String v) {
        v = v.endsWith("*") ? v.substring(0, v.length() -1) : v;
        this.segments = Utils.extractSegments(v);
    }

    @Override
    public boolean isValid(FullHttpRequest req) {
        String[] incomeSegms =  Utils.extractSegments(req.uri());

        if (this.segments.length == 1 && this.segments[0].equals("")){
            return true;
        }

        if (incomeSegms.length < segments.length){
            return false;
        }
        for (int i=0;i < incomeSegms.length ;i++){
            String incomeSegm = incomeSegms[i];

            if (this.segments.length <= i){
                continue;
            }
            String conditionSegm = this.segments[i];

            if (conditionSegm.startsWith("{") || conditionSegm.equals("*")){
                continue;
            }
            if (!incomeSegm.equals(conditionSegm)){
                return false;
            }
        }
        return true;
    }

    public String getPrefix(){
        String prefix = "";
        for (String segm : this.segments){
            prefix += String.format("%s/", segm);
        }
        return prefix;
    }

    @Override
    public RuleKind kind() {
        return HttpRuleKind.PREFIX;
    }

    @Override
    public int score() {
        if (this.segments.length == 1 && this.segments[0].equals("")){
            return 0;
        }
        return this.segments.length;
    }

    
}