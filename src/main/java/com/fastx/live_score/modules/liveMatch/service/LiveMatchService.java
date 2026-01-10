package com.fastx.live_score.modules.liveMatch.service;

import com.fastx.live_score.modules.liveMatch.response.MatchDto;
import com.fastx.live_score.modules.liveMatch.response.MatchDtoMapper;
import com.fastx.live_score.modules.liveMatch.response.OverListResponse;
import com.fastx.live_score.modules.match.db.MatchEntity;
import com.fastx.live_score.modules.player.entity.PlayerEntity;
import com.fastx.live_score.modules.team.TeamEntity;
import com.fastx.live_score.modules.match.dto.MatchStatus;
import com.fastx.live_score.modules.match.db.MatchEntityRepository;
import com.fastx.live_score.core.file.LiveMatchFileRepository;
import com.fastx.live_score.modules.liveMatch.request.StartMatchRequest;
import org.example.*;
import org.example.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.events.MatchEvents.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveMatchService {

    private final LiveMatchFileRepository liveMatchFileRepository;
    private final MatchEntityRepository matchEntityRepository;
    private final com.fastx.live_score.modules.commentary.service.CommentaryService commentaryService;

    public MatchControl getMatch(String matchId) {
        return liveMatchFileRepository.findById(matchId);
    }

    public Object commentary(String matchId) {
        return commentaryService.getCommentary(matchId);
    }

    public MatchControl startMatch(StartMatchRequest request) {
        MatchEntity matchEntity = matchEntityRepository.findById(request.getMatchId())
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        TeamEntity teamA = matchEntity.getTeamEntityA();
        TeamEntity teamB = matchEntity.getTeamEntityB();

        Map<Long, PlayerEntity> teamAPlayerMap = teamA.getPlayers().stream()
                .collect(Collectors.toMap(PlayerEntity::getId, p -> p));
        Map<Long, PlayerEntity> teamBPlayerMap = teamB.getPlayers().stream()
                .collect(Collectors.toMap(PlayerEntity::getId, p -> p));

        ImmutableList<Player> teamAPlayers = request.getTeamAPlayers().stream()
                .map(id -> new SimplePlayer(id, teamAPlayerMap.get(id).getShortName(),
                        teamAPlayerMap.get(id).getRole()))
                .collect(ImmutableList.toImmutableList());

        ImmutableList<Player> teamBPlayers = request.getTeamBPlayers().stream()
                .map(id -> new SimplePlayer(id, teamBPlayerMap.get(id).getShortName(),
                        teamBPlayerMap.get(id).getRole()))
                .collect(ImmutableList.toImmutableList());

        // Captains, Vice-Captains, and Wicket Keepers
        SimplePlayer teamACaptain = createPlayer(request.getTeamACaptainId(), teamAPlayerMap);
        SimplePlayer teamAViceCaptain = createPlayer(request.getTeamAViceCaptainId(), teamAPlayerMap);
        SimplePlayer teamAWicketKeeper = createPlayer(request.getTeamAWicketKeeperId(), teamAPlayerMap);

        SimplePlayer teamBCaptain = createPlayer(request.getTeamBCaptainId(), teamBPlayerMap);
        SimplePlayer teamBViceCaptain = createPlayer(request.getTeamBViceCaptainId(), teamBPlayerMap);
        SimplePlayer teamBWicketKeeper = createPlayer(request.getTeamBWicketKeeperId(), teamBPlayerMap);

        SimpleLineUp lineupA = SimpleLineUp.lineUp()
                .withId(teamA.getId())
                .withTeamName(teamA.getName())
                .withBattingOrder(teamAPlayers)
                .withCaptain(teamACaptain)
                .withViceCaptain(teamAViceCaptain)
                .withWicketKeeper(teamAWicketKeeper)
                .build();

        SimpleLineUp lineupB = SimpleLineUp.lineUp()
                .withId(teamB.getId())
                .withTeamName(teamB.getName())
                .withBattingOrder(teamBPlayers)
                .withCaptain(teamBCaptain)
                .withViceCaptain(teamBViceCaptain)
                .withWicketKeeper(teamBWicketKeeper)
                .build();

        Long electedTeamId = request.getElectedTeamId();
        String electedTo = request.getElectedTo();

        LineUp<?> battingTeam = "bat".equalsIgnoreCase(electedTo)
                ? (electedTeamId.equals(teamA.getId()) ? lineupA : lineupB)
                : (electedTeamId.equals(teamA.getId()) ? lineupB : lineupA);

        Instant now = Instant.now();
        MatchControl control = MatchControl.newMatch(matchStarting()
                .withTeamLineUps(ImmutableList.of(lineupA, lineupB))
                .withOversPerInnings(matchEntity.getTotalOvers())
                .withInningsPerTeam(1)
                .withTime(now)
                .withScheduledStartTime(now)
                .withTimeZone(TimeZone.getDefault())
                .build());
        matchEntity.setLiveMatchId(control.match().id());
        matchEntity.setMatchStatus(MatchStatus.IN_PROGRESS);
        matchEntity.setTossWinner(electedTeamId.equals(teamA.getId()) ? 1 : 2);
        matchEntity.setElectedTo(electedTo);
        matchEntityRepository.save(matchEntity);

        control = control.onEvent(inningsStarting()
                .withBattingTeam(battingTeam)
                .withTime(Instant.now()));

        liveMatchFileRepository.save(control.match().id().toString(), control);
        return control;
    }

    private SimplePlayer createPlayer(Long playerId, Map<Long, PlayerEntity> playerMap) {
        PlayerEntity entity = playerMap.get(playerId);
        if (entity == null) {
            throw new IllegalArgumentException("Player not found for ID: " + playerId);
        }
        return new SimplePlayer(playerId, entity.getShortName(), entity.getRole());
    }

    public MatchControl startInnings(String matchId, Long teamId) {
        MatchControl control = liveMatchFileRepository.findById(matchId);

        LineUp<?> selectedLineUp = control.match().teams().stream()
                .filter(t -> Objects.equals(((SimpleLineUp) t).getId(), teamId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid teamId"));

        control = control.onEvent(inningsStarting()
                .withBattingTeam(selectedLineUp)
                .withTime(Instant.now()));

        liveMatchFileRepository.save(control.match().id().toString(), control);
        return control;
    }

    public MatchControl startBatterInnings(String matchId, Long batterId) {
        MatchControl control = liveMatchFileRepository.findById(matchId);
        Player batter = findPlayerById(control, batterId);

        MatchControl updated = control.onEvent(batterInningsStarting()
                .withTime(Instant.now())
                .withBatter(batter));

        liveMatchFileRepository.save(matchId, updated);
        return updated;
    }

    public MatchControl startOver(String matchId, Long bowlerId) {
        MatchControl control = liveMatchFileRepository.findById(matchId);
        Player bowler = findPlayerById(control, bowlerId);

        MatchControl updated = control.onEvent(overStarting(bowler)
                .withTime(Instant.now()));

        liveMatchFileRepository.save(matchId, updated);
        return updated;
    }

    public MatchControl completeBall(String matchId, String outcome) {
        MatchControl control = liveMatchFileRepository.findById(matchId);
        BallCompletedEvent.Builder builder = ballCompleted(outcome).withTime(Instant.now());
        // We need to apply it to get the event with full context (defaults filled)
        // Actually, the builder needs to be applied to match to resolve defaults.
        // MatchControl.onEvent does this internally.
        // But we want the fully built event for commentary.
        // The event is built INSIDE onEvent.
        // So we should capture the event via a different mechanism or just use the
        // control after update.

        // Let's rely on building it ourselves partially to get what we need, OR
        // better: The event generated by `ballCompleted` is a builder.
        // `control.onEvent` takes `MatchEvent` or `Builder`.

        control = control.onEvent(builder);

        // Retrieve the last event which is the BallCompletedEvent we just added
        // It might be wrapped or we can just look at match history.
        // MatchControl stores history.

        // Accessing the last event:
        // MatchControl doesn't expose list of events directly via public API easily
        // maybe?
        // Wait, `control.eventStream()` exists.

        BallCompletedEvent event = control.eventStream(BallCompletedEvent.class)
                .reduce((first, second) -> second) // get last
                .orElse(null);

        if (event != null) {
            commentaryService.generateAndSaveCommentary(matchId, control, event);
        }

        return applyCompletionChecks(matchId, control);
    }

    public MatchControl completeBallWithDismissal(String matchId, String outcome, DismissalType dismissalType,
            Long fielderId) {
        MatchControl control = liveMatchFileRepository.findById(matchId);
        BallCompletedEvent.Builder builder = ballCompleted(outcome).withDismissal(dismissalType);

        if (fielderId != null) {
            builder.withFielder(findPlayerById(control, fielderId));
        }

        control = control.onEvent(builder.withTime(Instant.now()));
        control = control.onEvent(batterInningsCompleted().withTime(Instant.now()));

        // Get last ball event
        BallCompletedEvent event = control.eventStream(BallCompletedEvent.class)
                .reduce((first, second) -> second)
                .orElse(null);

        if (event != null) {
            commentaryService.generateAndSaveCommentary(matchId, control, event);
        }

        return applyCompletionChecks(matchId, control);
    }

    private MatchControl applyCompletionChecks(String matchId, MatchControl control) {
        Innings innings = control.match().currentInnings();
        log.info("Applying completion checks for " + matchId);
        if (innings != null) {
            if (innings.currentOver() != null && innings.currentOver().isComplete()) {
                control = control.onEvent(overCompleted().withTime(Instant.now()));

                // IMPORTANT: Generate Over Summary for Commentary
                commentaryService.saveOverSummary(matchId, control);

                innings = control.match().currentInnings();
            }
            if (innings != null && innings.shouldBeComplete()) {
                control = control.onEvent(inningsCompleted().withTime(Instant.now()));
            }
        }

        if (control.match().completedInningsList().size() == control.match().numberOfInningsPerTeam() * 2) {
            control = control.onEvent(matchCompleted().withTime(Instant.now()));

            MatchEntity matchEntity = matchEntityRepository.findByLiveMatchId(UUID.fromString(matchId))
                    .orElseThrow();
            matchEntity.setMatchStatus(MatchStatus.COMPLETED);
            matchEntityRepository.save(matchEntity);
        }

        liveMatchFileRepository.save(matchId, control);
        return control;
    }

    private Player findPlayerById(MatchControl control, Long playerId) {
        return control.match().teams().stream()
                .flatMap(team -> team.battingOrder().stream())
                .filter(p -> ((SimplePlayer) p).id().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }

    public List<OverListResponse> getOverList(String matchId) {
        MatchControl control = liveMatchFileRepository.findById(matchId);
        Match match = control.match();

        return match.inningsList().stream().map(innings -> {
            OverListResponse overListResponse = new OverListResponse();
            LineUp lineUp = innings.battingTeam();
            if (lineUp instanceof SimpleLineUp) {
                overListResponse.setTeamId(((SimpleLineUp) lineUp).getId());
            }
            overListResponse.setTeamName(innings.battingTeam().teamName());
            overListResponse.setInningsNo(innings.inningsNumber());

            List<MatchDto.OverDto> overs = innings.overs()
                    .stream()
                    .map(over -> {
                        if (over.isComplete()) {
                            OverCompletedEvent overCompletedEvent = control.eventStream(OverCompletedEvent.class)
                                    .filter(oc -> oc.overNumber() == over.overNumber())
                                    .findFirst().orElseThrow();
                            MatchControl at = control.asAt(overCompletedEvent);
                            return MatchDtoMapper.mapOverDto(over, at.currentInnings());
                        } else
                            return MatchDtoMapper.mapOverDto(over, innings);
                    })
                    .sorted(Comparator.comparingInt(MatchDto.OverDto::getOverNumber).reversed())
                    .toList();
            overListResponse.setOvers(overs);
            return overListResponse;
        })
                .sorted(Comparator.comparingInt(OverListResponse::getInningsNo).reversed())
                .toList();
    }

    public MatchControl undoLastEvent(String matchId) {
        MatchControl control = liveMatchFileRepository.findById(matchId);
        MatchControl updated = control.undo();
        liveMatchFileRepository.save(matchId, updated);
        return updated;
    }
}
