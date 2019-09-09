package io.lightflame.store;

import java.util.ArrayList;
import java.util.List;

/**
 * HttpUrlCondition
 */
public class HttpUrlScore {

    private Boolean isWideCard;
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
                this.segments.add("DYNAMIC");
                continue;
            }
            this.segments.add(segment);
        }
    }

    

    int getScore(String url, String method){
        int score = 100;
        url = url.split("\\?",0)[0];
        String[] incomeSegments = url.split("/");
        if (incomeSegments.length > segments.size()){
            return 0;
        }
        for (int i=0;i< incomeSegments.length;i++){
            String incomeSegm = incomeSegments[i];
            String conditionSegm = this.segments.get(i);
            //TODO: check if income segm is the same
        }
        return 0;
    }
}