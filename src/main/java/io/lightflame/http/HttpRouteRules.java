package io.lightflame.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;

import io.lightflame.http.HttpRouteRules.RuleEnum;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpRouteRule
 */

interface Rule {
    Boolean validate(List<String> s);
    RuleEnum kind();
    int score();
}


public class HttpRouteRules {
    public class HttpRouteRule{
        private String id;
        private List<Rule> rules = new ArrayList<>();

        public String getId() {
            return id;
        }

        public List<Rule> getRules() {
            return rules;
        }

        public Rule getRule(RuleEnum r) {
            Optional<Rule> ruleOpt = rules.stream().filter(x -> x.kind() == r).findFirst();
            if (ruleOpt.isPresent()){
                return ruleOpt.get();
            }
            return null;
        }

        HttpRouteRule(String id){
            this.id = id;
        }

        public HttpRouteRule addRule(RuleEnum k, String v){
            if (k.equals(RuleEnum.HEADER)){
                rules.add(new HeaderRule(v));
            }
            if (k.equals(RuleEnum.PATH) || k.equals(RuleEnum.PREFIX)){
                if (v.contains("*")){
                    rules.add(new PrefixPathRule(v));
                }else{
                    rules.add(new PathRule(v));
                }
            }
            if (k.equals(RuleEnum.QPARAM)){
                rules.add(new QParamRule(v));
            }
            if (k.equals(RuleEnum.METHOD)){
                rules.add(new MethodRule(v));
            }
            return this;
        }

        private int score(){
            int score = 0;
            for (Rule rule : rules){
                score += rule.score();
            }
            return score;
        }

        private boolean match(List<String> incomeReq){            
            for (Rule rule : rules) {
                if (!rule.validate(incomeReq)){
                    return false;
                }
            }
            return true;
        }

        public String set(){
            routeRules.add(this);
            return this.id;
        }

        public class HeaderRule implements Rule{
            private Map<String,Boolean> headerMap;

            private Map<String,Boolean> makeMap(String v){
                Map<String,Boolean> hMap = new HashMap<>();
                for(String pair : v.split("|")){
                    hMap.put(pair, true);
                }
                return hMap;
            }
    
            HeaderRule(String v){
                this.headerMap = makeMap(v);
            }

            @Override
            public Boolean validate(List<String> incomeReq) {
                String incomeHeaders =  incomeReq.get(0);
                Map<String,Boolean> incomeHeaderMap = makeMap(incomeHeaders);
                for (String incomeKey : incomeHeaderMap.keySet()){
                    if (!this.headerMap.containsKey(incomeKey)){
                        return false;
                    }
                }
                return true;
            }

            @Override
            public int score() {
                return headerMap.size();
            }

            @Override
            public RuleEnum kind() {
                return RuleEnum.HEADER;
            }
        }

        public class PathRule implements Rule{
            private String[] segments;
    
            PathRule(String v){
                this.segments = HttpUtils.extractSegments(v);
            }

            @Override
            public RuleEnum kind() {
                return RuleEnum.PATH;
            }

            public String[] getSegments() {
                return this.segments;
            }

            @Override
            public Boolean validate(List<String> incomeReq) {
                String incomePath =  incomeReq.get(2);

                String[] incomeSegments = HttpUtils.extractSegments(incomePath);

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
            public int score() {
                return segments.length * 2;
            }
        }

        public class PrefixPathRule implements Rule{
            private String[] segments;

            @Override
            public RuleEnum kind() {
                return RuleEnum.PREFIX;
            }
    
            PrefixPathRule(String v){
                v = v.endsWith("*") ? v.substring(0, v.length() -1) : v;
                this.segments = HttpUtils.extractSegments(v);
            }

            String getPrefix(){
                String prefix = "";
                for (String segm : this.segments){
                    prefix += String.format("%s/", segm);
                }
                return prefix;
            }

            @Override
            public Boolean validate(List<String> incomeReq) {
                String[] incomeSegms =  HttpUtils.extractSegments(incomeReq.get(2));

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
            public int score() {
                return segments.length;
            }
        }


        public class QParamRule implements Rule{

            private Map<String,Boolean> qparamMap;

            @Override
            public RuleEnum kind() {
                return RuleEnum.QPARAM;
            }

            private Map<String,Boolean> makeMap(String v){
                Map<String,Boolean> hMap = new HashMap<>();
                for(String pair : v.split("&")){
                    hMap.put(pair, true);
                }
                return hMap;
            }
    
            QParamRule(String v){
                this.qparamMap = makeMap(v);
            }

            @Override
            public Boolean validate(List<String> incomeReq) {
                String incomeQueryParam =  incomeReq.get(3);
                Map<String,Boolean> incomeQParam = makeMap(incomeQueryParam);
                for (String incomeKey : incomeQParam.keySet()){
                    if (!this.qparamMap.containsKey(incomeKey)){
                        return false;
                    }
                }
                return true;
            }

            @Override
            public int score() {
                return this.qparamMap.size();
            }
        }

        public class MethodRule implements Rule{
            private String value;
    
            MethodRule(String v){
                this.value = v;
            }

            @Override
            public RuleEnum kind() {
                return RuleEnum.METHOD;
            }

            @Override
            public Boolean validate(List<String> incomeReq) {
                String incomeMethod =  incomeReq.get(1);
                if (incomeMethod == value){
                    return true;
                }
                return false;
            }

            @Override
            public int score() {
                return 1;
            }
        }

    }

    static private List<HttpRouteRule> routeRules = new ArrayList<>();

    public enum RuleEnum {
        METHOD,
        PATH,
        PREFIX,
        QPARAM,
        HEADER
      }

    public HttpRouteRule newRoute(String id){
        return new HttpRouteRule(id);
    }

    public HttpRouteRule newRoute(){
        return new HttpRouteRule(UUID.randomUUID().toString());
    }


    static public HttpRouteRule processRequest(FullHttpRequest request){
        // "x-auth=abc","GET","/path/to/my","name=daniel"

        String headers = "";
        for (Entry<String,String> e : request.headers().entries()){
            headers += String.format("%s=%s|", e.getKey(), e.getValue());
        }

        String[] uriSpl =  request.uri().split("\\?");
        String qParam = uriSpl.length == 2 ? uriSpl[1] : "";
        return processRequest(Arrays.asList(
            headers, 
            request.method().name(),
            uriSpl[0],
            qParam
        ));
    }

    static public HttpRouteRule processRequest(List<String> incomeReq){

        Optional<HttpRouteRule> optRule =  routeRules
            .stream()
            .filter(x -> x.match(incomeReq))
            .max(Comparator.comparing((HttpRouteRule r) -> r.score()));
        if (!optRule.isPresent()){
            return null;
        }
        return optRule.get();
    }
    
}