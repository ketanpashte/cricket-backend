package com.fastx.live_score.modules.player.controller;

import com.fastx.live_score.core.config.ApiDocsTags;
import com.fastx.live_score.core.utils.AppResponse;
import com.fastx.live_score.modules.player.dto.ListPlayerRes;
import com.fastx.live_score.modules.player.dto.Player;
import com.fastx.live_score.modules.player.dto.PlayerRequest;
import com.fastx.live_score.modules.player.service.PlayerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.fastx.live_score.core.config.APiConfig.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/players")
@Tag(name = "Player Admin")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Tag(name = "import")
    @PostMapping("/import")
    public AppResponse<String> importPlayer(@RequestParam("file") MultipartFile file) {
        playerService.importPlayersFromCsv(file);
        return AppResponse.success(null, "Players imported successfully.");
    }

    @PostMapping("/create")
    public AppResponse<String> savePlayer(@RequestBody PlayerRequest player) {
        playerService.savePlayers(List.of(player));
        return AppResponse.success(null, "Player created successfully.");
    }

    @GetMapping("/getPlayerInfo/{id}")
    public AppResponse<Player> getPlayerById(@PathVariable Long id) {
        return AppResponse.success(playerService.getPlayerById(id));
    }

    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportPlayersCsv() {
        ByteArrayResource csv = playerService.exportPlayersToCsv();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=players.csv")
                .header("Content-Type", "text/csv")
                .body(csv);
    }

    @Tag(name = ApiDocsTags.SEARCH)
    @GetMapping("/searchPlayers")
    public AppResponse<List<ListPlayerRes>> searchPlayer(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "nationality", required = false) String nationality,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "25") int size) {

        Page<Player> playersPage = playerService.listPlayer(q, nationality, role, page, size);
        List<ListPlayerRes> dtoList = playersPage.getContent()
                .stream()
                .map(ListPlayerRes::toShortPlayer)
                .toList();

        return AppResponse.success(dtoList);
    }

    @PutMapping("/updatePlayer/{id}")
    public AppResponse<String> updatePlayer(@PathVariable Long id, @RequestBody PlayerRequest request) {
        playerService.updatePlayer(id, request);
        return AppResponse.success("Player updated successfully");
    }

    @DeleteMapping("/deletePlayer/{id}")
    public AppResponse<String> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return AppResponse.success("Player deleted successfully.");
    }
}
