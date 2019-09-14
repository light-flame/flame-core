package io.lightflame.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.lightflame.store.Rule;
import io.lightflame.store.HttpRouteRules.HttpRouteRule;
import io.lightflame.store.HttpRouteRules.RuleEnum;
import io.lightflame.store.HttpRouteRules.HttpRouteRule.PathRule;

/**
 * HttpUtils
 */
public class FlameHttpUtils {

    private HttpRouteRule routeRule;
    private Map<String,Integer> dynamicSegm = new HashMap<>();

    public FlameHttpUtils(HttpRouteRule r) {
        this.routeRule = r;
        Optional<Rule> rule = r.getRules().stream().filter(x -> x.kind() == RuleEnum.PATH).findFirst();
        if (rule.isPresent()){
            PathRule pathRule = (PathRule)rule.get();
            this.setDynamicSegm(pathRule.getSegments());
        }
    }

    private String[] extractSegments(String condURI){
        condURI = condURI.startsWith("/") ? condURI.replaceFirst("/", "") : condURI;
        return condURI.split("/");
    }

    private void setDynamicSegm(String[] segms){
        for (int i = 0; i < segms.length ; i++){
            if (segms[i].startsWith("{")){
                String uri =  segms[i].substring(1, segms[i].length()-1);
                dynamicSegm.put(uri, i);
            }
        }
    }

    public FlameHttpUtils(String condURI) {
        this.setDynamicSegm(this.extractSegments(condURI));
    }

    public String extractQueryParam(String uri, String key){
        String[] uris = uri.split("\\?",0);
        if (uris.length != 2){
            return null;
        }
        for (String keyValue : uris[1].split("\\&")){
            if (!keyValue.contains("=")){
                continue;
            }
            String[] keyValSpl = keyValue.split("=");
            if (keyValSpl[0].equals(key)){
                return keyValSpl[1];
            }
        }
        return "";
    }

    

    // con example: /path/to/my/{url}
    // uri example: /path/to/my/url
    public String extractUrlParam(String uri, String name){
        Integer pathI =  dynamicSegm.get(name);
        uri = uri.split("\\?",0)[0];
        String[] uriSpl =  this.extractSegments(uri);
        if (pathI != null && pathI <= uriSpl.length){
            return uriSpl[pathI];
        }
        return "";
    }


}