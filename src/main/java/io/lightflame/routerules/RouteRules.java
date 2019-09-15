package io.lightflame.routerules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * RulesStore
 */
public class RouteRules {

    List<Rule> ruleList = new ArrayList<>();
    private String key;
    private StoreKind store;

    public RouteRules(StoreKind s, String k) {
        this.store = s;
        this.key = k;
    }

    StoreKind getStore(){
        return this.store;
    }

    public String getKey(){
        return this.key;
    }

    void setKey(String k){
        this.key = k;
    }

    Rule getRule(RuleKind rk) {
        Optional<Rule> ruleOpt = ruleList.stream().filter(x -> x.kind() == rk).findFirst();
        if (ruleOpt.isPresent()){
            return ruleOpt.get();
        }
        return null;
    }

    

    public RouteRules addRule(Rule rule){
        ruleList.add(rule);
        return this;
    }

    int score(){
        int score = 0;
        for (Rule rule : ruleList){
            score += rule.score();
        }
        return score;
    }

    @SuppressWarnings("unchecked")
    <E> boolean match(E income){            
        for (Rule rule : ruleList) {
            if (!rule.isValid(income)){
                return false;
            }
        }
        return true;
    }
}