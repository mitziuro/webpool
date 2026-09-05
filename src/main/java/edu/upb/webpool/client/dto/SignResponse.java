package edu.upb.webpool.client.dto;

import java.time.Instant;

public class SignResponse {

    private String hash;
    private String signature;
    private String hashAlgorithm;
    private String signatureAlgorithm;
    private String keyId;
    private Instant signedAt;

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
    public String getHashAlgorithm() { return hashAlgorithm; }
    public void setHashAlgorithm(String hashAlgorithm) { this.hashAlgorithm = hashAlgorithm; }
    public String getSignatureAlgorithm() { return signatureAlgorithm; }
    public void setSignatureAlgorithm(String signatureAlgorithm) { this.signatureAlgorithm = signatureAlgorithm; }
    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }
    public Instant getSignedAt() { return signedAt; }
    public void setSignedAt(Instant signedAt) { this.signedAt = signedAt; }
}
