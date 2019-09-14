package io.lightflame.routerules;

/**
 * HttpMethodRule
 */
public class HttpMethodRule implements Rule<String, String>{

    private String method;

    @Override
    public boolean isValid(String method) {
        return method.equals(this.method);
    }

    @Override
    public RuleKind kind() {
        return HttpRuleKind.METHOD;
    }

    @Override
    public int score() {
        return 1;
    }

    @Override
    public void setParam(String m) {
        this.method = m;
    }

    
}