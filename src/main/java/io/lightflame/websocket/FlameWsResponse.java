package io.lightflame.websocket;

public class FlameWsResponse {
    private String response;

    public FlameWsResponse(String response) {
        this.response = response;
    }

    String getResponse() {
        return response;
    }
}
