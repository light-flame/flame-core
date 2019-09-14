package io.lightflame.routerules;

/**
 * Utils
 */
public class Utils {

    static String[] extractSegments(String condURI){
        condURI = condURI.startsWith("/") ? condURI.replaceFirst("/", "") : condURI;
        return condURI.split("/");
    }
}