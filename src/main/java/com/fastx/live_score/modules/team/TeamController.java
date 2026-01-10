package com.fastx.live_score.modules.team;

import com.fastx.live_score.modules.player.dto.Player;
import com.fastx.live_score.core.config.ApiDocsTags;
import com.fastx.live_score.core.utils.AppResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fastx.live_score.core.config.APiConfig.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/teams")
@Tag(name = "Teams Admin")
public class TeamController {

    private final TeamService teamService;


    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    @PostMapping("/create")
    public String createTeam(@RequestBody TeamRequest team) {
        teamService.saveTeams(List.of(team));
        return "Teams created successfully.";
    }


    @Tag(name = ApiDocsTags.SEARCH)
    @GetMapping("/searchTeams")
    public AppResponse<List<Team>> searchTeams(@RequestParam(value = "q", required = false) String q) {
        return AppResponse.success(teamService.listTeams(q));
    }

    @GetMapping("/getTeamInfo/{id}")
    public AppResponse<Team> getTeamById(@PathVariable Long id) {
        return AppResponse.success(teamService.getTeamById(id));
    }

    @PutMapping("/updateTeam/{id}")
    public AppResponse<String> updateTeam(@PathVariable Long id, @RequestBody TeamRequest teamRequest) {
        teamService.updateTeam(id, teamRequest);
        return AppResponse.success("Team updated");
    }


    @GetMapping("/getPlayersFromTeam/{teamId}")
    public AppResponse<TeamPlayerInfo> getPlayersByTeamId(@PathVariable Long teamId) {
        return AppResponse.success(teamService.getAllPlayerFromTeam(teamId));
    }

    @PostMapping("/updatePlayers/{teamId}")
    public AppResponse<List<Player>> updatePlayers(@RequestBody List<Long> players,
                                                   @PathVariable Long teamId,
                                                   @RequestParam(required = false) Long viceCaptainId,
                                                   @RequestParam(required = false) Long captainId) {
        List<Player> _players = teamService.updatePlayers(players, teamId, captainId, viceCaptainId);
        return AppResponse.success(_players);
    }

    @DeleteMapping("/{id}")
    public AppResponse<String> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return AppResponse.success(null, "Team deleted successfully.");
    }
}
