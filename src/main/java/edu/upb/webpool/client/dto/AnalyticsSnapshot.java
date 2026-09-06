package edu.upb.webpool.client.dto;

import java.util.List;

public class AnalyticsSnapshot {

    private List<AnalyticsPoolProjection> pools;
    private List<AnalyticsVoteProjection> votes;

    public AnalyticsSnapshot(List<AnalyticsPoolProjection> pools, List<AnalyticsVoteProjection> votes) {
        this.pools = pools;
        this.votes = votes;
    }

    public List<AnalyticsPoolProjection> getPools() { return pools; }
    public void setPools(List<AnalyticsPoolProjection> pools) { this.pools = pools; }
    public List<AnalyticsVoteProjection> getVotes() { return votes; }
    public void setVotes(List<AnalyticsVoteProjection> votes) { this.votes = votes; }
}
