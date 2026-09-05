package edu.upb.webpool.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * A Pool.
 */
@Table("pools")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pool implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private String id;

    @Column("name")
    private String name;

    @Column("start_date")
    private Instant startDate;

    @Column("end_date")
    private Instant endDate;

    @Column("results_sent_at")
    private Instant resultsSentAt;

    @Column("owner")
    private String owner;

    @Column("type")
    private String type;

    @Column("options")
    private List<String> options = new ArrayList<>();

    @Column("final_value")
    private String finalValue;

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public boolean isVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }

    @Column("users")
    private Set<String> users = new HashSet<>();

    @Column("vote")
    private boolean vote;

    @Column("otp")
    private boolean otp;

    @Column("show_intermediate_results")
    private boolean showIntermediateResults;

    @Column("video_answers_enabled")
    private boolean videoAnswersEnabled;

    @Column("public_access")
    private boolean publicAccess;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Pool id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Pool name(String name) {
        this.setName(name);
        return this;
    }

    public boolean isOtp() {
        return otp;
    }

    public void setOtp(boolean freeOTP) {
        this.otp = freeOTP;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Pool startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Pool endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getResultsSentAt() {
        return resultsSentAt;
    }

    public void setResultsSentAt(Instant resultsSentAt) {
        this.resultsSentAt = resultsSentAt;
    }

    public String getOwner() {
        return this.owner;
    }

    public Pool owner(String owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return this.type;
    }

    public Pool type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public Pool options(String[] options) {
        this.setOptions(options == null ? null : Arrays.asList(options));
        return this;
    }

    public Pool options(List<String> options) {
        this.setOptions(options);
        return this;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public boolean isShowIntermediateResults() {
        return showIntermediateResults;
    }

    public void setShowIntermediateResults(boolean showIntermediateResults) {
        this.showIntermediateResults = showIntermediateResults;
    }

    public boolean isVideoAnswersEnabled() {
        return videoAnswersEnabled;
    }

    public void setVideoAnswersEnabled(boolean videoAnswersEnabled) {
        this.videoAnswersEnabled = videoAnswersEnabled;
    }

    public boolean isPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(boolean publicAccess) {
        this.publicAccess = publicAccess;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pool)) {
            return false;
        }
        return id != null && id.equals(((Pool) o).id);
    }

    public String getFinal() {
        return finalValue;
    }

    public void setFinal(String finalValue) {
        this.finalValue = finalValue;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pool{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", owner='" + getOwner() + "'" +
            ", type='" + getType() + "'" +
            ", options='" + getOptions() + "'" +
            "}";
    }
}
