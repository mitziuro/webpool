package edu.upb.webpool.client.dto;

import java.time.Instant;

public class AnalyticsVoteProjection {

    private String id;
    private String pool;
    private String optionValue;
    private String option;
    private Instant date;
    private String owner;
    private String type;
    private Boolean videoAttached;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPool() { return pool; }
    public void setPool(String pool) { this.pool = pool; }
    public String getOptionValue() { return optionValue; }
    public void setOptionValue(String optionValue) { this.optionValue = optionValue; }
    public String getOption() { return option; }
    public void setOption(String option) { this.option = option; }
    public Instant getDate() { return date; }
    public void setDate(Instant date) { this.date = date; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Boolean getVideoAttached() { return videoAttached; }
    public void setVideoAttached(Boolean videoAttached) { this.videoAttached = videoAttached; }
}
