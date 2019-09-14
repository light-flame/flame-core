package io.lightflame.routerules;

/**
 * HttpPrefixPathRule
 */
public class HttpPrefixPathRule implements Rule<String, String>{

    private String[] segments;

    @Override
    public boolean isValid(String obj) {
        String[] incomeSegms =  Utils.extractSegments(obj);

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

    @Override
    public RuleKind kind() {
        return HttpRuleKind.PREFIX;
    }

    @Override
    public void setParam(String obj) {
        this.segments = Utils.extractSegments(obj);

    }

    @Override
    public int score() {
        return this.segments.length;
    }

    
}