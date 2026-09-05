package edu.upb.webpool.domain;

import java.time.Instant;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("vote_confirmation_challenges")
public class VoteConfirmationChallenge {

    @PrimaryKey
    private String email;

    @Column("pool_id")
    private String poolId;

    @Column("method")
    private String method;

    @Column("code_hash")
    private String codeHash;

    @Column("expires_at")
    private Instant expiresAt;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPoolId() { return poolId; }
    public void setPoolId(String poolId) { this.poolId = poolId; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getCodeHash() { return codeHash; }
    public void setCodeHash(String codeHash) { this.codeHash = codeHash; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
