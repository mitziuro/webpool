package edu.upb.webpool.client.dto;

import java.util.Map;

public class SignRequest {

    private Map<String, Object> payload;

    public SignRequest() {}

    public SignRequest(Map<String, Object> payload) {
        this.payload = payload;
    }

    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
}
