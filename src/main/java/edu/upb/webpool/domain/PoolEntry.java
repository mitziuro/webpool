package edu.upb.webpool.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PoolEntry.
 */
@Document(collection = "pool_entry")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PoolEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("pool")
    private String pool;

    @Field("option_value")
    private String optionValue;

    @Field("option")
    private String option;

    @Field("date")
    private Instant date;

    @Field("owner")
    private String owner;

    @Field("type")
    private String type;

    @Field("is_final")
    private Boolean isFinal;

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
