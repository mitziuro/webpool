package edu.upb.webpool.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * A PoolEntry.
 */
@Table("pool_entries")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PoolEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private String id;

    @Column("pool")
    private String pool;

    @Column("option_value")
    private String optionValue;

    @Column("option")
    private String option;

    @Column("date")
    private Instant date = Instant.now();

    @Column("owner")
    private String owner;

    @Column("type")
    private String type;

    @Column("is_final")
    private Boolean isFinal;

    @Column("video_attached")
    private Boolean videoAttached = false;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column("vote_hash")
    private String voteHash;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column("digital_signature")
    private String digitalSignature;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column("hash_algorithm")
    private String hashAlgorithm;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column("signature_algorithm")
    private String signatureAlgorithm;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column("signature_key_id")
    private String signatureKeyId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column("signed_at")
    private Instant signedAt;

    @Transient
    private String otp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PoolEntry id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPool() {
        return this.pool;
    }

    public PoolEntry pool(String pool) {
        this.setPool(pool);
        return this;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public String getOptionValue() {
        return this.optionValue;
    }

    public PoolEntry optionValue(String optionValue) {
        this.setOptionValue(optionValue);
        return this;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOption() {
        return this.option;
    }

    public PoolEntry option(String option) {
        this.setOption(option);
        return this;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Instant getDate() {
        return this.date;
    }

    public PoolEntry date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getOwner() {
        return this.owner;
    }

    public PoolEntry owner(String owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return this.type;
    }

    public PoolEntry type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsFinal() {
        return this.isFinal;
    }

    public PoolEntry isFinal(Boolean isFinal) {
        this.setIsFinal(isFinal);
        return this;
    }

    public void setIsFinal(Boolean isFinal) {
        this.isFinal = isFinal;
    }

    public Boolean getVideoAttached() {
        return videoAttached;
    }

    public void setVideoAttached(Boolean videoAttached) {
        this.videoAttached = videoAttached;
    }

    public String getVoteHash() { return voteHash; }
    public void setVoteHash(String voteHash) { this.voteHash = voteHash; }
    public String getDigitalSignature() { return digitalSignature; }
    public void setDigitalSignature(String digitalSignature) { this.digitalSignature = digitalSignature; }
    public String getHashAlgorithm() { return hashAlgorithm; }
    public void setHashAlgorithm(String hashAlgorithm) { this.hashAlgorithm = hashAlgorithm; }
    public String getSignatureAlgorithm() { return signatureAlgorithm; }
    public void setSignatureAlgorithm(String signatureAlgorithm) { this.signatureAlgorithm = signatureAlgorithm; }
    public String getSignatureKeyId() { return signatureKeyId; }
    public void setSignatureKeyId(String signatureKeyId) { this.signatureKeyId = signatureKeyId; }
    public Instant getSignedAt() { return signedAt; }
    public void setSignedAt(Instant signedAt) { this.signedAt = signedAt; }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PoolEntry)) {
            return false;
        }
        return id != null && id.equals(((PoolEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PoolEntry{" +
            "id=" + getId() +
            ", pool='" + getPool() + "'" +
            ", optionValue='" + getOptionValue() + "'" +
            ", option='" + getOption() + "'" +
            ", date='" + getDate() + "'" +
            ", owner='" + getOwner() + "'" +
            ", type='" + getType() + "'" +
            ", isFinal='" + getIsFinal() + "'" +
            "}";
    }
}
