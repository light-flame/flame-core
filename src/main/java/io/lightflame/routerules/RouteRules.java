package io.lightflame.routerules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * RulesStore
 */
public class RouteRules<C,E> {

    List<Rule<C,E>> ruleList = new ArrayList<>();
    private String key;
    private StoreKind store;

    RouteRules(StoreKind s, String k) {
        this.store = s;
        this.key = k;
    }

    StoreKind getStore(){
        return this.store;
    }

    String getKey(){
        return this.key;
    }

    void setKey(String k){
        this.key = k;
    }

    Rule<C,E> getRule(RuleKind rk) {
        Optional<Rule<C,E>> ruleOpt = ruleList.stream().filter(x -> x.kind() == rk).findFirst();
        if (ruleOpt.isPresent()){
            return ruleOpt.get();
        }
        return null;
    }

    

    RouteRules<C,E> addRule(Rule<C,E> rule, C p){
        rule.setParam(p);
        return this;
    }

    int score(){
        int score = 0;
        for (Rule<C,E> rule : ruleList){
            score += rule.score();
        }
        return score;
    }

    boolean match(E income){            
        for (Rule<C,E> rule : ruleList) {
            if (!rule.isValid(income)){
                return false;
            }
        }
        return true;
    }
}