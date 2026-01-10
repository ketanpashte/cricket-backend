package com.fastx.live_score.modules.team;

import com.fastx.live_score.modules.player.dto.Player;

import java.util.List;

public interface TeamService {

    void saveTeams(List<TeamRequest> request);

    Team getTeamById(Long teamId);

    List<Team> listTeams(String q);

    void updateTeam(Long teamId, TeamRequest request);

    void deleteTeam(Long teamId);

    TeamPlayerInfo getAllPlayerFromTeam(Long teamId);

    List<Player> updatePlayers(List<Long> players, Long teamId, Long captainId, Long viceCaptainId);
}
