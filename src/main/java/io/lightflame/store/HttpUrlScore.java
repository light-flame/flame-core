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
        for (String segment : constructSegment(url)){
            if (segment.equals("*")){
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

    private List<String> constructSegment(String url){
        List<String> segments = new ArrayList<>();
        url = url.split("\\?",0)[0];
        for (String segment : url.split("/")){
            if (segment.equals("")){
                continue;
            }
            segments.add(segment);
        }
        return segments;
    }

    

    int getScore(String url, String method){
        if (method != this.method){
            return 0;
        }
        List<String> incomeSegments = constructSegment(url);

        if (this.isWideCard){
            return wideCardScore(incomeSegments);
        }
        return normalScore(incomeSegments);

    }

    private int normalScore(List<String> incomeSegments){
        if (incomeSegments.size() != segments.size()){
            return 0;
        }
        for (int i=0;i < incomeSegments.size();i++){
            String incomeSegm = incomeSegments.get(i);
            String conditionSegm = this.segments.get(i);
            if (conditionSegm.equals(DYNAMIC)){
                continue;
            }
            if (!incomeSegm.equals(conditionSegm)){
                return 0;
            }

        }
        return 1;
    }

    private int wideCardScore(List<String> incomeSegments){
        int score = 1;
        if (incomeSegments.size() < segments.size()){
            return 0;
        }
        for (int i=0;i < incomeSegments.size();i++){
            String incomeSegm = incomeSegments.get(i);

            if (this.segments.size() <= i){
                score++;
                continue;
            }
            String conditionSegm = this.segments.get(i);

            if (conditionSegm.equals(DYNAMIC))continue;
            if (!incomeSegm.equals(conditionSegm))return 0;
            score++;
        }
        return score;
    }
}