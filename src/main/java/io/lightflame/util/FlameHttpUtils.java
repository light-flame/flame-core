package io.lightflame.util;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpUtils
 */
public class FlameHttpUtils {

    private Map<String,Integer> dynamicSegm = new HashMap<>();

    public FlameHttpUtils(String condURI) {
        String[] segms = condURI.split("/");
        for (int i = 0; i < segms.length ; i++){
            if (segms[i].startsWith("{")){
                String uri =  segms[i].substring(1, segms[i].length()-1);
                dynamicSegm.put(uri, i);
            }
        }
    }

    public String extractQueryParam(String uri, String name){
        return "";
    }

    

    // con example: /path/to/my/{url}
    // uri example: /path/to/my/url
    public String extractUrlParam(String uri, String name){
        Integer pathI =  dynamicSegm.get(name);
        uri = uri.split("\\?",0)[0];
        String[] uriSpl =  uri.split("/");
        if (pathI != null && pathI <= uriSpl.length){
            return uriSpl[pathI];
        }
        return "";
    }


}