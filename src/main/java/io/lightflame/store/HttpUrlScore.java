package io.lightflame.store;

import java.util.ArrayList;
import java.util.List;

/**
 * HttpUrlCondition
 */
public class HttpUrlScore {

    private final static String DYNAMIC = "DYNAMIC";

    private Boolean isWideCard = false;
    private String method;
    private List<String> segments = new ArrayList<>();

    HttpUrlScore(String url, String method){
        this.method = method;
        url = url.split("\\?",0)[0];
        for (String segment : url.split("/")){
            if (segment == ""){
                continue;
            }
            if (segment == "*"){
                this.isWideCard = true;
                break;
            }
            if (segment.contains("{")){
                this.segments.add(DYNAMIC);
                continue;
            }
            this.segments.add(segment);
        }
    }

    

    int getScore(String url, String method){
        int score = 0;
        url = url.split("\\?",0)[0];
        String[] incomeSegments = url.split("/");
        if (incomeSegments.length > segments.size()){
            return 0;
        }
        if (incomeSegments.length != this.segments.size() && !this.isWideCard){
            return 0;
        }
        for (int i=0;i< incomeSegments.length;i++){
            String incomeSegm = incomeSegments[i];
            String conditionSegm = this.segments.get(i);
            if (incomeSegm == conditionSegm){
                score += 10;
            }
            if (conditionSegm == DYNAMIC){
                score += 5;
            }
        }
        return score;
    }
}