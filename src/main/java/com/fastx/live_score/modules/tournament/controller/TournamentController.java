package com.fastx.live_score.modules.tournament.controller;

import com.fastx.live_score.core.config.ApiDocsTags;
import com.fastx.live_score.core.utils.AppResponse;
import com.fastx.live_score.modules.tournament.dto.TournamentRequest;
import com.fastx.live_score.modules.tournament.service.TournamentService;
import com.fastx.live_score.modules.tournament.dto.ListTournamentRes;
import com.fastx.live_score.modules.tournament.dto.Tournament;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fastx.live_score.core.config.APiConfig.API_VERSION_1;

@RestController
@Tag(name = "Tournament Admin")
@RequestMapping(API_VERSION_1 + "/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping("/create")
    public AppResponse<Tournament> createTournament(@RequestBody TournamentRequest request) {
        return AppResponse.success(tournamentService.createNewTournament(request));
    }

    @PutMapping("/update/{id}")
    public AppResponse<Tournament> updateTournament(@PathVariable Long id, @RequestBody TournamentRequest request) {
        return AppResponse.success(tournamentService.updateTournament(id, request));
    }

    @PatchMapping("/{id}/highlights")
    public AppResponse<Tournament> updateHighlights(
            @PathVariable Long id,
            @RequestBody com.fastx.live_score.modules.tournament.dto.TournamentHighlightsRequest request) {
        return AppResponse.success(tournamentService.updateHighlights(id, request));
    }

    @GetMapping("/getTournamentInfo/{id}")
    public AppResponse<Tournament> getTournamentById(@PathVariable Long id) {
        return AppResponse.success(tournamentService.getTournamentById(id));
    }

    @Tag(name = ApiDocsTags.SEARCH)
    @GetMapping("/searchTournament")
    public AppResponse<List<ListTournamentRes>> getAllTournaments(
            @RequestParam(name = "q", defaultValue = "") String query) {
        return AppResponse.success(tournamentService
                .searchTournament(query)
                .stream()
                .map(ListTournamentRes::from)
                .toList());
    }

    @DeleteMapping("/delete/{id}")
    public AppResponse<String> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return AppResponse.success("Tournament deleted successfully.");
    }

    @PutMapping("/{tournamentId}/assign-winner/{teamId}")
    public AppResponse<String> assignWinner(@PathVariable Long tournamentId, @PathVariable Long teamId) {
        tournamentService.assignWinner(tournamentId, teamId);
        return AppResponse.success("Winner assigned successfully.");
    }
}
