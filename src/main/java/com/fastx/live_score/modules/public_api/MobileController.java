package com.fastx.live_score.modules.public_api;

import com.fastx.live_score.modules.liveMatch.response.LiveLineScore;
import com.fastx.live_score.modules.liveMatch.service.LiveMatchService;
import com.fastx.live_score.modules.match.dto.MatchInfo;
import com.fastx.live_score.modules.match.service.MatchService;
import com.fastx.live_score.modules.tournament.service.TournamentService;
import com.fastx.live_score.modules.tournament.dto.ListTournamentRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.MatchControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Mobile Controller")
public class MobileController {
    private final LiveMatchService liveMatchService;
    private final TournamentService tournamentService;
    private final MatchService matchService;

    @Autowired
    public MobileController(LiveMatchService liveMatchService, TournamentService tournamentService, MatchService matchService) {
        this.liveMatchService = liveMatchService;
        this.tournamentService = tournamentService;
        this.matchService = matchService;
    }


    @GetMapping("/listTournament")
    public List<ListTournamentRes> getAllTournaments() {
        return tournamentService.getAllTournaments()
                .stream().map(ListTournamentRes::from).toList();
    }

    @GetMapping("/getMatchInfo/{matchId}")
    public MatchInfo getMatchInfo(@PathVariable Long matchId) {
        return matchService.getMatchINfo(matchId);
    }

    @GetMapping("/getLivelineScore/{liveMatchId}")
    public LiveLineScore getLiveLineScore(@PathVariable String liveMatchId) {
        MatchControl match = liveMatchService.getMatch(liveMatchId);
        return LiveLineScore.getFromMatch(match);
    }


}
