package com.fastx.live_score.modules.liveMatch.response;

import org.example.Match;
import org.example.MatchControl;
import org.example.Score;
import org.example.events.BallCompletedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BallCompletedEventDto {

    private String bowlerName;
    private String strikerName;
    private String nonStrikerName;
    private int teamRuns;
    private boolean playersCrossed;
    private String dismissalType;
    private String fielderName;
    private int overNumber;
    private int numberInOver;
    private int numberInMatch;
    private Instant time;
    private String text;
    private OverEndingSummary overEndingSummary;

    public static BallCompletedEventDto toDto(BallCompletedEvent event, MatchControl control) {

        BallCompletedEventDto.BallCompletedEventDtoBuilder build = BallCompletedEventDto.builder()
                .bowlerName(event.bowler().scorecardName())
                .strikerName(event.striker().scorecardName())
                .nonStrikerName(event.nonStriker().scorecardName())
                .teamRuns(event.runsScored().teamRuns())
                .text(Score.toText(event.score()))
                .playersCrossed(event.playersCrossed())
                .dismissalType(event.dismissal() != null ? event.dismissal().type().fullName() : null)
                .fielderName(event.fielder() != null ? event.fielder().scorecardName() : null)
                .overNumber(event.overNumber())
                .numberInOver(event.numberInOver())
                .numberInMatch(event.numberInMatch())
                .time(event.time());
        MatchControl matchControl = control.asAt(event);
        Match match = matchControl.match();
        build.overEndingSummary(OverEndingSummary.fromMatch(match));

        return build.build();
    }

}