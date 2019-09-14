package io.lightflame.http;

import java.util.HashMap;
import java.util.Map;

import io.lightflame.http.HttpRouteRules.HttpRouteRule;
import io.lightflame.http.HttpRouteRules.RuleEnum;
import io.lightflame.http.HttpRouteRules.HttpRouteRule.PathRule;
import io.lightflame.http.HttpRouteRules.HttpRouteRule.PrefixPathRule;

/**
 * HttpUtils
 */
public class HttpUtils {

    static String[] extractSegments(String condURI){
        condURI = condURI.startsWith("/") ? condURI.replaceFirst("/", "") : condURI;
        return condURI.split("/");
    }

    private Map<String, Integer> makeMapUri(String[] segms){
        Map<String, Integer> mapUri = new HashMap<>();
        for (int i = 0; i < segms.length ; i++){
            if (segms[i].startsWith("{")){
                String uri =  segms[i].substring(1, segms[i].length()-1);
                mapUri.put(uri, i);
            }
        }
        return mapUri;
    }

    HttpUtils() {
    }

    String extractQueryParam(String uri, String key){
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

    
    public String getPathWithoutPrefix(String uri, HttpRouteRule routeRule){
        Rule rule = routeRule.getRule(RuleEnum.PREFIX);
        if (rule == null){
            return "";
        }
        PrefixPathRule pRule = (PrefixPathRule)rule;
        return uri.replace(pRule.getPrefix(), "");
    }

    // con example: /path/to/my/{url}
    // uri example: /path/to/my/url
    public String extractUrlParam(String uri, String name, HttpRouteRule routeRule){
        Rule rule = routeRule.getRule(RuleEnum.PATH);
        if (rule == null){
            return null;
        }
        PathRule pRule = (PathRule)rule;
        Map<String, Integer> mapUri = this.makeMapUri(pRule.getSegments());
        Integer pathI =  mapUri.get(name);
        uri = uri.split("\\?",0)[0];
        String[] uriSpl =  extractSegments(uri);
        if (pathI != null && pathI <= uriSpl.length){
            return uriSpl[pathI];
        }
        return "";
    }


}