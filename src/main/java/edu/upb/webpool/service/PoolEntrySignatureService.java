package edu.upb.webpool.service;

import edu.upb.webpool.client.VoteSignatureClient;
import edu.upb.webpool.client.dto.SignRequest;
import edu.upb.webpool.client.dto.SignResponse;
import edu.upb.webpool.client.dto.VerifyRequest;
import edu.upb.webpool.client.dto.VerifyResponse;
import edu.upb.webpool.domain.PoolEntry;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PoolEntrySignatureService {

    private final VoteSignatureClient signatureClient;
    private final String serviceKey;

    public PoolEntrySignatureService(
        VoteSignatureClient signatureClient,
        @Value("${webpool.signing.service-key}") String serviceKey
    ) {
        this.signatureClient = signatureClient;
        this.serviceKey = serviceKey;
    }

    public void sign(PoolEntry entry) {
        SignResponse signed = signatureClient.sign(serviceKey, new SignRequest(payload(entry)));
        entry.setVoteHash(signed.getHash());
        entry.setDigitalSignature(signed.getSignature());
        entry.setHashAlgorithm(signed.getHashAlgorithm());
        entry.setSignatureAlgorithm(signed.getSignatureAlgorithm());
        entry.setSignatureKeyId(signed.getKeyId());
        entry.setSignedAt(signed.getSignedAt());
    }

    public VerifyResponse verify(PoolEntry entry) {
        VerifyRequest request = new VerifyRequest();
        request.setPayload(payload(entry));
        request.setHash(entry.getVoteHash());
        request.setSignature(entry.getDigitalSignature());
        request.setKeyId(entry.getSignatureKeyId());
        return signatureClient.verify(request);
    }

    private Map<String, Object> payload(PoolEntry entry) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", entry.getId());
        payload.put("pool", entry.getPool());
        payload.put("optionValue", entry.getOptionValue());
        payload.put("option", entry.getOption());
        // Cassandra timestamps have millisecond precision. Canonicalize to the
        // same precision before signing so a persisted vote hashes identically
        // when it is read back and verified.
        payload.put("date", entry.getDate() == null ? null : entry.getDate().truncatedTo(ChronoUnit.MILLIS).toString());
        payload.put("owner", entry.getOwner());
        payload.put("type", entry.getType());
        payload.put("isFinal", entry.getIsFinal());
        payload.put("videoAttached", entry.getVideoAttached());
        return payload;
    }
}
