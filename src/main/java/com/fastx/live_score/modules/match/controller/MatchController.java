package com.fastx.live_score.modules.match.controller;

import com.fastx.live_score.core.config.APiConfig;
import com.fastx.live_score.modules.liveMatch.response.MatchListItemDto;
import com.fastx.live_score.core.utils.AppResponse;
import com.fastx.live_score.modules.match.dto.Match;
import com.fastx.live_score.modules.match.dto.MatchRequest;
import com.fastx.live_score.modules.match.service.MatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Match Admin")
@RequestMapping(APiConfig.API_VERSION_1 + "/match")
@RestController
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }


    @PostMapping("/save")
    public AppResponse<String> createMatch(@RequestBody @Valid MatchRequest request) {
        matchService.saveMatch(request);
        return AppResponse.success("Match Created Successfully");
    }

    @GetMapping("/list")
    public AppResponse<List<Match>> allMatches() {
        return AppResponse.success(matchService.listMatches(null));
    }

    @GetMapping("/getMatchInfo/{matchId}")
    public AppResponse<Match> getMatchById(@PathVariable Long matchId) {
        return AppResponse.success(matchService.getMatchById(matchId));
    }

    @GetMapping("/liatALlMatches")
    public AppResponse<List<MatchListItemDto>> listMatches() {
        return AppResponse.success(matchService.listAllMatches());
    }

    @GetMapping("/getMatchScoreById/{matchId}")
    public AppResponse<MatchListItemDto> getMatchScoreById(@PathVariable UUID matchId) {
        return AppResponse.success(matchService.getMatchScoreById(matchId));
    }

    @DeleteMapping("/delete/{matchId}")
    public AppResponse<String> deleteMatch(@PathVariable Long matchId) {
        matchService.deleteMatch(matchId);
        return AppResponse.success("Deleted Successfully");
    }

    @GetMapping("/forceRestart/{matchId}")
    public AppResponse<String> forceRestart(@PathVariable Long matchId) {
        matchService.forceRestartMatch(matchId);
        return AppResponse.success("Match restarted successfully");
    }

}
