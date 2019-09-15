package io.lightflame.routerules;

import java.util.HashMap;
import java.util.Map;

/**
 * Utils
 */
public class Utils {

    static String[] extractSegments(String uri){
        uri = uri.split("\\?")[0];
        uri = uri.startsWith("/") ? uri.replaceFirst("/", "") : uri;
        return uri.split("/");
    }

    static Map<String,String> extractQueryParam(String uri){
        String[] uriP = uri.split("\\?");
        if (uriP.length != 2) return null;
        String qparam = uriP[1];

        Map<String,String> qparamMap = new HashMap<>();
        for(String pair : qparam.split("&")){
            String[] qpSpl = pair.split("=");
            if (qpSpl.length != 2)continue;
            qparamMap.put(qpSpl[0], qpSpl[1]);
        }
        return qparamMap;
    }
}