package io.lightflame.routerules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public RouteRules(StoreKind s) {
        this.store = s;
        this.key = UUID.randomUUID().toString();
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

    public Rule getRule(RuleKind rk) {
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

    public RouteRules addRules(Rule... rules){
        ruleList.addAll(Arrays.asList(rules));
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