package io.lightflame.routerules;

import io.lightflame.websocket.WsRequestWrapper;

/**
 * HttpPathRule
 */
public class WsPathRule implements Rule<WsRequestWrapper>{
    private String[] segments;

    public WsPathRule(String obj) {
        this.segments = Utils.extractSegments(obj);
    }

    @Override
    public boolean isValid(WsRequestWrapper req) {
        String[] incomeSegments = Utils.extractSegments(req.getUri());

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

    public String[] getSegments(){
        return this.segments;
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