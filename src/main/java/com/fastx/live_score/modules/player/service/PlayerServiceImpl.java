package com.fastx.live_score.modules.player.service;

import com.fastx.live_score.core.exception.PlayerNotFoundException;
import com.fastx.live_score.mapper.PlayerMapper;
import com.fastx.live_score.modules.player.dto.Player;
import com.fastx.live_score.modules.player.dto.PlayerRequest;
import com.fastx.live_score.modules.player.entity.PlayerEntity;
import com.fastx.live_score.modules.player.repository.PlayerRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Sort DEFAULT_SORT = Sort.by("fullName").ascending();

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Save multiple players
     */
    @Override
    public void savePlayers(List<PlayerRequest> requests) {
        if (CollectionUtils.isEmpty(requests))
            return;
        List<PlayerEntity> entities = requests.stream()
                .map(request -> toEntity(new PlayerEntity(), request))
                .collect(Collectors.toList());
        playerRepository.saveAll(entities);
    }

    /**
     * Import players from CSV
     */
    /**
     * Import players from CSV
     */
    @Override
    public void importPlayersFromCsv(MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            // Handle UTF-8 BOM which breaks header parsing in OpenCSV
            PushbackInputStream pbis = new PushbackInputStream(is, 3);
            byte[] bom = new byte[3];
            int n = pbis.read(bom, 0, 3);
            if (n == 3 && bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF) {
                // Found BOM, skip it
            } else if (n > 0) {
                // Not BOM, unread bytes
                pbis.unread(bom, 0, n);
            }

            try (Reader reader = new InputStreamReader(pbis, StandardCharsets.UTF_8)) {
                CsvToBean<PlayerRequest> csvToBean = new CsvToBeanBuilder<PlayerRequest>(reader)
                        .withType(PlayerRequest.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSeparator(',')
                        .build();

                List<PlayerRequest> players = csvToBean.parse().stream()
                        .filter(req -> req.getFullName() != null && !req.getFullName().trim().isEmpty()) // basic
                                                                                                         // validation
                        .filter(playerRequest -> !playerRepository.existsByFullName(playerRequest.getFullName()))
                        .toList();

                if (players.isEmpty()) {
                    // Log warning but don't fail, maybe all exist already
                    System.out.println("No new players to import or CSV was empty.");
                }

                savePlayers(players);
            }
        } catch (Exception e) {
            throw new RuntimeException("CSV import failed: " + e.getMessage(), e);
        }
    }

    /**
     * Convert PlayerRequest to PlayerEntity
     */
    private PlayerEntity toEntity(PlayerEntity entity, PlayerRequest request) {
        Optional.ofNullable(request.getFullName()).ifPresent(entity::setFullName);
        Optional.ofNullable(request.getShortName()).ifPresent(entity::setShortName);
        Optional.ofNullable(request.getRole()).ifPresent(entity::setRole);
        Optional.ofNullable(request.getBattingStyle()).ifPresent(entity::setBattingStyle);
        Optional.ofNullable(request.getBowlingStyle()).ifPresent(entity::setBowlingStyle);
        Optional.ofNullable(request.getNationality()).ifPresent(entity::setNationality);
        entity.setActive(true);
        return entity;
    }

    /**
     * Get player by ID
     */
    @Override
    public Player getPlayerById(Long playerId) {
        PlayerEntity entity = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + playerId));
        return PlayerMapper.toPlayer(entity);
    }

    /**
     * Export players to CSV
     */
    @Override
    public ByteArrayResource exportPlayersToCsv() {
        List<PlayerEntity> entities = playerRepository.findAll();
        List<PlayerRequest> players = entities.stream()
                .map(entity -> {
                    PlayerRequest req = new PlayerRequest();
                    req.setFullName(entity.getFullName());
                    req.setShortName(entity.getShortName());
                    req.setRole(entity.getRole());
                    req.setBattingStyle(entity.getBattingStyle());
                    req.setBowlingStyle(entity.getBowlingStyle());
                    req.setNationality(entity.getNationality());
                    return req;
                })
                .toList();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("fullName", "shortName", "role", "battingStyle", "bowlingStyle", "nationality"))) {

            for (PlayerRequest player : players) {
                csvPrinter.printRecord(
                        player.getFullName(),
                        player.getShortName(),
                        player.getRole(),
                        player.getBattingStyle(),
                        player.getBowlingStyle(),
                        player.getNationality());
            }

            csvPrinter.flush();
            return new ByteArrayResource(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to export CSV: " + e.getMessage(), e);
        }
    }

    /**
     * List players with search, filters, and pagination
     */
    @Override
    public Page<Player> listPlayer(String q, String nationality, String role, int page, int size) {
        q = (q != null && !q.trim().isEmpty()) ? q.trim() : null;
        nationality = (nationality != null && !nationality.trim().isEmpty()) ? nationality.trim() : null;
        role = (role != null && !role.trim().isEmpty()) ? role.trim() : null;

        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());

        Page<PlayerEntity> entityPage;
        // if (q == null) {
        // entityPage = playerRepository.findAll(pageable);
        // } else if (nationality != null) {
        // entityPage = playerRepository.searchPlayers(q, role, nationality, pageable);
        // } else
        // entityPage = playerRepository.searchByName(q, pageable);

        if (q == null) {
            q = "";
        }
        entityPage = playerRepository.searchPlayers(q, role, nationality, pageable);

        return entityPage.map(PlayerMapper::toPlayer);
    }

    /**
     * Update player by ID
     */
    @Override
    public void updatePlayer(Long playerId, PlayerRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body cannot be null.");
        }

        PlayerEntity existing = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + playerId));

        PlayerEntity updated = this.toEntity(existing, request);
        playerRepository.save(updated);
    }

    /**
     * Delete player by ID
     */
    @Override
    public void deletePlayer(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException("Player not found with ID: " + playerId);
        }
        playerRepository.deleteById(playerId);
    }
}
