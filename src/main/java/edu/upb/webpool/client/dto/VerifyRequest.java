package edu.upb.webpool.client.dto;

import java.util.Map;

public class VerifyRequest {

    private Map<String, Object> payload;
    private String hash;
    private String signature;
    private String keyId;

    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }
}
