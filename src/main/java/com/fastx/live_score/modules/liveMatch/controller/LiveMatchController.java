package com.fastx.live_score.modules.liveMatch.controller;

import com.fastx.live_score.core.config.APiConfig;
import com.fastx.live_score.modules.liveMatch.request.*;
import com.fastx.live_score.modules.liveMatch.response.MatchDto;
import com.fastx.live_score.modules.liveMatch.response.MatchDtoMapper;
import com.fastx.live_score.modules.liveMatch.response.OverListResponse;
import com.fastx.live_score.modules.liveMatch.service.LiveMatchService;
import com.fastx.live_score.util.MatchUtil;
import com.fastx.live_score.modules.liveMatch.response.BallCompletedEventDto;
import lombok.RequiredArgsConstructor;
import org.example.MatchControl;
import org.example.events.BallCompletedEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(APiConfig.API_VERSION_1 + "/matches/live")
@RequiredArgsConstructor
public class LiveMatchController {

    private final LiveMatchService liveMatchService;

    @PostMapping("/start")
    public MatchDto startMatch(@RequestBody StartMatchRequest request) {
        MatchControl control = liveMatchService.startMatch(request);
        return MatchDtoMapper.toDto(control);
    }

    @PostMapping("/{matchId}/startInnings")
    public MatchDto startMatch(@PathVariable String matchId, @RequestBody StartInningRequest request) {
        MatchControl control = liveMatchService.startInnings(matchId, request.getTeamId());
        return MatchDtoMapper.toDto(control);
    }


    @PostMapping("/{matchId}/start-batter")
    public MatchDto startBatterInnings(@PathVariable String matchId, @RequestBody BatterRequest request) {
        MatchControl control = liveMatchService.startBatterInnings(matchId, request.getBatter());
        return MatchDtoMapper.toDto(control);
    }

    @PostMapping("/{matchId}/start-over")
    public MatchDto startOver(@PathVariable String matchId, @RequestBody BowlerRequest request) {
        MatchControl control = liveMatchService.startOver(matchId, request.getBowlerId());
        return MatchDtoMapper.toDto(control);
    }


    @PostMapping("/{matchId}/complete-ball")
    public MatchDto completeBall(@PathVariable String matchId, @RequestBody OutcomeRequest request) {
        MatchControl control = liveMatchService.completeBall(matchId, request.getOutcome());
        return MatchDtoMapper.toDto(control);
    }

    @GetMapping("/{matchId}/undo")
    public MatchDto undo(@PathVariable String matchId) {
        MatchControl control = liveMatchService.undoLastEvent(matchId);
        return MatchDtoMapper.toDto(control);
    }

    @PostMapping("/{matchId}/complete-ball-dismissal")
    public MatchDto completeBallWithDismissal(@PathVariable String matchId,
                                              @RequestBody DismissalRequest request) {
        MatchControl control = liveMatchService.completeBallWithDismissal(
                matchId,
                request.getOutcome(),
                request.getDismissalType(),
                request.getFielderId()
        );
        return MatchDtoMapper.toDto(control);
    }

    @GetMapping("/{matchId}/getOvers")
    public List<OverListResponse> getOvers(@PathVariable String matchId) {
        return liveMatchService.getOverList(
                matchId
        );
    }


    @GetMapping("/{matchId}")
    public MatchDto getMatch(@PathVariable String matchId) {
        MatchControl control = liveMatchService.getMatch(matchId);
        return MatchDtoMapper.toDto(control);
    }

    @GetMapping("/shortScore/{matchId}")
    public MatchDto getShortScore(@PathVariable String matchId) {
        MatchControl control = liveMatchService.getMatch(matchId);
        return MatchDtoMapper.toDto(control);
    }

    @GetMapping("/commentary/{matchId}")
    public Object commentary(@PathVariable String matchId) {
        return liveMatchService.commentary(matchId);
    }

    @GetMapping("/{matchId}/lastBalls")
    public List<BallCompletedEventDto> lastBalls(@PathVariable String matchId, @RequestParam(required = false, defaultValue = "0") Integer limit) {
        MatchControl control = liveMatchService.getMatch(matchId);
        List<BallCompletedEventDto> list = new ArrayList<>();
        for (BallCompletedEvent ballCompletedEvent : MatchUtil.getLastNBallEvents(control, limit)) {
            BallCompletedEventDto dto = BallCompletedEventDto.toDto(ballCompletedEvent, control);
            list.add(dto);
        }
        return list;
    }


}
