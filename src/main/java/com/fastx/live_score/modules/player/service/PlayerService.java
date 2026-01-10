package com.fastx.live_score.modules.player.service;

import com.fastx.live_score.modules.player.dto.Player;
import com.fastx.live_score.modules.player.dto.PlayerRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlayerService {

    void savePlayers(List<PlayerRequest> requests);

    void importPlayersFromCsv(MultipartFile file);

    Player getPlayerById(Long playerId);

    ByteArrayResource exportPlayersToCsv();

    Page<Player> listPlayer(String q, String teamName, String role, int page, int size);

    void updatePlayer(Long playerId, PlayerRequest request);

    void deletePlayer(Long playerId);
}
