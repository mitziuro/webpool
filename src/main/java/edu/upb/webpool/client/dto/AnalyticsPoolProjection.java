package edu.upb.webpool.client.dto;

import java.time.Instant;

public class AnalyticsPoolProjection {

    private String id;
    private String name;
    private Instant endDate;
    private String owner;
    private String type;
    private String finalValue;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Instant getEndDate() { return endDate; }
    public void setEndDate(Instant endDate) { this.endDate = endDate; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getFinalValue() { return finalValue; }
    public void setFinalValue(String finalValue) { this.finalValue = finalValue; }
}
