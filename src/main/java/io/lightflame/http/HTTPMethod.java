package io.lightflame.http;

public enum HTTPMethod {
    POST("POST"), GET("GET"), PATCH("PATCH");

    private String httpMethod;

    private HTTPMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpMethod(){
        return httpMethod;
    }
}