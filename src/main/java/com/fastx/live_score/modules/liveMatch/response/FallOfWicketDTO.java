package com.fastx.live_score.modules.liveMatch.response;

public class FallOfWicketDTO {
    private int wickets;
    private int teamRuns;
    private String batterName;
    private String overOrReason;

    public FallOfWicketDTO(int wickets, int teamRuns, String batterName, String overOrReason) {
        this.wickets = wickets;
        this.teamRuns = teamRuns;
        this.batterName = batterName;
        this.overOrReason = overOrReason;
    }

    // Getters and setters (or use Lombok if preferred)
    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public int getTeamRuns() {
        return teamRuns;
    }

    public void setTeamRuns(int teamRuns) {
        this.teamRuns = teamRuns;
    }

    public String getBatterName() {
        return batterName;
    }

    public void setBatterName(String batterName) {
        this.batterName = batterName;
    }

    public String getOverOrReason() {
        return overOrReason;
    }

    public void setOverOrReason(String overOrReason) {
        this.overOrReason = overOrReason;
    }
}
