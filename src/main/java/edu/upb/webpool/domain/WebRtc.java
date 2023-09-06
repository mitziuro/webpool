package edu.upb.webpool.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;

/**
 * A PoolEntry.
 */
@Document(collection = "video")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WebRtc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("pool")
    private String pool;

    @Field("owner")
    private String owner;

    @Field("data")
    private byte[] data;

    @Field("date")
    private Instant date = Instant.now();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public WebRtc id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPool() {
        return this.pool;
    }

    public WebRtc pool(String pool) {
        this.setPool(pool);
        return this;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }


    public String getOwner() {
        return this.owner;
    }

    public WebRtc owner(String owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public byte[] getData() {
        return data;
    }

    public WebRtc data(byte[] data) {
        this.setData(data);
        return this;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Instant getDate() {
        return this.date;
    }

    public WebRtc date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}
