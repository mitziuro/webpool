package edu.upb.webpool.client.dto;

public class VerifyResponse {

    private boolean valid;
    private boolean hashMatches;
    private boolean signatureValid;
    private String keyId;

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public boolean isHashMatches() { return hashMatches; }
    public void setHashMatches(boolean hashMatches) { this.hashMatches = hashMatches; }
    public boolean isSignatureValid() { return signatureValid; }
    public void setSignatureValid(boolean signatureValid) { this.signatureValid = signatureValid; }
    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }
}
