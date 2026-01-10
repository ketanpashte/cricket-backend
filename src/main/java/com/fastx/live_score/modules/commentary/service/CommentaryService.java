package com.fastx.live_score.modules.commentary.service;

import com.fastx.live_score.modules.commentary.entity.CommentaryEntity;
import com.fastx.live_score.modules.commentary.entity.OverSummaryEntity;
import com.fastx.live_score.modules.commentary.repository.CommentaryRepository;
import com.fastx.live_score.modules.commentary.repository.OverSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.example.BatterInnings;
import org.example.BowlerInnings;
import org.example.MatchControl;
import org.example.events.BallCompletedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentaryService {

    private final CommentaryRepository commentaryRepository;
    private final OverSummaryRepository overSummaryRepository;

    @Transactional(readOnly = true)
    public List<CommentaryEntity> getCommentary(String matchId) {
        return commentaryRepository.findByMatchIdOrderByOverNumberDescBallNumberDesc(matchId);
    }

    @Transactional(readOnly = true)
    public List<OverSummaryEntity> getOverSummaries(String matchId) {
        return overSummaryRepository.findByMatchIdOrderByOverNumberDesc(matchId);
    }

    @Transactional
    public void generateAndSaveCommentary(String matchId, MatchControl control, BallCompletedEvent event) {
        String outcome = getOutcomeString(event);
        String commentaryText = generateCommentaryText(event, outcome);

        CommentaryEntity entity = CommentaryEntity.builder()
                .matchId(matchId)
                .overNumber(event.overNumber() + 1)
                .ballNumber(event.numberInOver())
                .bowlerName(event.bowler().scorecardName())
                .batterName(event.striker().scorecardName())
                .runs(outcome)
                .commentaryText(commentaryText)
                .build();

        commentaryRepository.save(entity);
    }

    @Transactional
    public void saveOverSummary(String matchId, MatchControl control) {
        var innings = control.match().currentInnings();
        if (innings == null)
            return;

        // We need the *completed* over for the summary.
        var lastOver = innings.overs().stream()
                .filter(o -> o.isComplete())
                .reduce((first, second) -> second)
                .orElse(null);

        if (lastOver == null) {
            return;
        }

        var bowler = lastOver.bowler();
        var striker = innings.currentStriker(); // Returns BatterInnings
        var nonStriker = innings.currentNonStriker(); // Returns BatterInnings

        // Batter Stats
        int sRuns = (striker != null) ? striker.score().batterRuns() : 0;
        int sBalls = (striker != null) ? striker.score().validDeliveries() : 0;

        int nsRuns = (nonStriker != null) ? nonStriker.score().batterRuns() : 0;
        int nsBalls = (nonStriker != null) ? nonStriker.score().validDeliveries() : 0;

        // Bowler Stats
        String bOvers = "0.0";
        int bMaidens = 0;
        int bRuns = 0;
        int bWickets = 0;

        if (bowler != null) {
            var bi = innings.bowlerInningsList().stream()
                    .filter(b -> b.bowler().samePlayer(bowler))
                    .findFirst()
                    .orElse(null);
            if (bi != null) {
                bOvers = bi.overDotBallString();
                bMaidens = bi.maidens();
                bRuns = bi.score().teamRuns();
                bWickets = bi.wickets();
            }
        }

        OverSummaryEntity entity = OverSummaryEntity.builder()
                .matchId(matchId)
                .overNumber(lastOver.overNumber())
                .teamTotalRuns(innings.score().teamRuns())
                .teamTotalWickets(innings.score().wickets())
                .strikerName(striker != null ? striker.player().scorecardName() : "") // BatterInnings -> Player -> name
                .strikerRuns(sRuns)
                .strikerBalls(sBalls)
                .nonStrikerName(nonStriker != null ? nonStriker.player().scorecardName() : "")
                .nonStrikerRuns(nsRuns)
                .nonStrikerBalls(nsBalls)
                .bowlerName(bowler != null ? bowler.scorecardName() : "")
                .bowlerOvers(bOvers)
                .bowlerMaidens(bMaidens)
                .bowlerRuns(bRuns)
                .bowlerWickets(bWickets)
                .build();

        overSummaryRepository.save(entity);
    }

    @Transactional
    public CommentaryEntity updateCommentary(Long id, String text) {
        CommentaryEntity entity = commentaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Commentary not found"));
        entity.setCommentaryText(text);
        return commentaryRepository.save(entity);
    }

    private String getOutcomeString(BallCompletedEvent event) {
        if (event.dismissal() != null) {
            return "W";
        }
        int runs = event.runsScored().batterRuns();
        if (runs == 0)
            return "0";
        if (runs == 4)
            return "4";
        if (runs == 6)
            return "6";
        // Extras
        if (event.runsScored().wides() > 0)
            return "Wd";
        if (event.runsScored().noBalls() > 0)
            return "NB";
        return String.valueOf(runs);
    }

    private String generateCommentaryText(BallCompletedEvent event, String outcome) {
        String bowler = event.bowler().scorecardName();
        String batter = event.striker().scorecardName();

        StringBuilder sb = new StringBuilder();
        sb.append(bowler).append(" to ").append(batter).append(", ");

        if (event.dismissal() != null) {
            sb.append("OUT! ").append(event.dismissal().type().fullName());
            if (event.dismissal().fielder() != null) {
                sb.append(" by ").append(event.dismissal().fielder().scorecardName());
            }
        } else {
            if (event.runsScored().wides() > 0) {
                sb.append("Wide ball.");
            } else if (event.runsScored().noBalls() > 0) {
                sb.append("No Ball.");
            } else if (event.runsScored().batterRuns() == 4) {
                sb.append("FOUR!");
            } else if (event.runsScored().batterRuns() == 6) {
                sb.append("SIX!");
            } else if (event.runsScored().batterRuns() == 0) {
                sb.append("no run.");
            } else {
                sb.append(event.runsScored().batterRuns()).append(" run(s).");
            }
        }
        return sb.toString();
    }
}
