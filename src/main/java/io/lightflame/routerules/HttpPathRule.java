package io.lightflame.routerules;

/**
 * HttpPathRule
 */
public class HttpPathRule implements Rule<String, String>{
    private String[] segments;

    @Override
    public boolean isValid(String incomePath) {
        String[] incomeSegments = Utils.extractSegments(incomePath);

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
    public void setParam(String obj) {
        this.segments = Utils.extractSegments(obj);
    }

    @Override
    public int score() {
        return this.segments.length * 2;
    }

    
}