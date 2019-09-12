package io.lightflame.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpRouteRule
 */
interface Rule {
    Boolean validate(List<String> s);
    int score();
}

public class HttpRouteRules {
    public class HttpRouteRule{
        private Integer id;
        private List<Rule> rules = new ArrayList<>();

        HttpRouteRule(Integer id){
            this.id = id;
        }

        public HttpRouteRule addRule(RuleEnum k, String v){
            if (k.equals(RuleEnum.HEADER)){
                rules.add(new HeaderRule(v));
            }
            if (k.equals(RuleEnum.PATH) || k.equals(RuleEnum.PREFIX)){
                if (v.contains("*")){
                    rules.add(new WideCardeRule(v));
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

        public int set(){
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
        }

        public class PathRule implements Rule{
            private String[] segments;
    
            PathRule(String v){
                this.segments = v.split("/");
            }

            @Override
            public Boolean validate(List<String> incomeReq) {
                String incomePath =  incomeReq.get(2);

                String[] incomeSegments = incomePath.split("/");

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

        public class WideCardeRule implements Rule{
            private String[] segments;

    
            WideCardeRule(String v){
                this.segments = v.split("/");
            }

            @Override
            public Boolean validate(List<String> incomeReq) {
                String[] incomeSegms =  incomeReq.get(2).split("/");

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

    public HttpRouteRule newRoute(Integer id){
        return new HttpRouteRule(id);
    }

    public HttpRouteRule newRoute(){
        return new HttpRouteRule(routeRules.size());
    }


    static public int processRequest(FullHttpRequest request){
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

    static public int processRequest(List<String> incomeReq){

        Optional<HttpRouteRule> optRule =  routeRules
            .stream()
            .filter(x -> x.match(incomeReq))
            .max(Comparator.comparing((HttpRouteRule r) -> r.score()));
        if (!optRule.isPresent()){
            return -1;
        }
        return optRule.get().id;
    }
    
}