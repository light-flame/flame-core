package io.lightflame.routerules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * RulesStore
 */
public class RouteRules<E> {

    List<Rule<E>> ruleList = new ArrayList<>();
    private String key;

    public RouteRules(String k) {
        this.key = k;
    }

    public RouteRules() {
        this.key = UUID.randomUUID().toString();
    }

    public String getKey(){
        return this.key;
    }

    void setKey(String k){
        this.key = k;
    }

    public Rule<E> getRule(RuleKind rk) {
        Optional<Rule<E>> ruleOpt = ruleList.stream().filter(x -> x.kind() == rk).findFirst();
        if (ruleOpt.isPresent()){
            return ruleOpt.get();
        }
        return null;
    }

    

    public RouteRules<E> addRule(Rule<E> rule){
        ruleList.add(rule);
        return this;
    }

    public RouteRules<E> addRules(Rule<E>[] rules){
        ruleList.addAll(Arrays.asList(rules));
        return this;
    }

    public RouteRules<E> addRules(List<Rule<E>> rules){
        ruleList.addAll(rules);
        return this;
    }

    int score(){
        int score = 0;
        for (Rule<?> rule : ruleList){
            score += rule.score();
        }
        return score;
    }

    boolean match(E income){            
        for (Rule<E> rule : ruleList) {
            if (!rule.isValid(income)){
                return false;
            }
        }
        return true;
    }
}