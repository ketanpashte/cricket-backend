//package com.fastx.live_score.mapper;
//
//import com.fastx.live_score.adapter.models.response.Player;
//import com.fastx.live_score.csv.models.PlayerImportModel;
//import com.fastx.live_score.entities.PlayerEntity;
//import com.fastx.live_score.entities.TeamEntity;
//import org.junit.jupiter.api.Test;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PlayerMapperTest {
//
//    @Test
//    void toResponse_shouldMapCorrectly_whenEntityIsValid() {
//        PlayerEntity entity = getPlayerMockEntity();
//
//        Player response = PlayerMapper.toResponse(entity);
//
//        assertNotNull(response);
//        assertEquals(entity.getId(), response.getId());
//        assertEquals("John Doe", response.getFullName());
//        assertEquals("JD", response.getShortName());
//        assertEquals("Australia", response.getNationality());
//        assertEquals("Batsman", response.getRole());
//        assertEquals("Right-hand bat", response.getBattingStyle());
//        assertEquals("Off spin", response.getBowlingStyle());
//        assertEquals(100, response.getTotalMatches());
//        assertEquals(4000, response.getTotalRuns());
//        assertEquals(50, response.getTotalWickets());
//        assertTrue(response.isActive());
//        assertEquals(1, response.getTeams().size());
//        assertEquals(10L, response.getTeams().get(0).getTeamId());
//        assertEquals("Sydney Sixers", response.getTeams().get(0).getTeamName());
//    }
//
//
//    @Test
//    void toResponse_shouldReturnNull_whenEntityIsNull() {
//        assertNull(PlayerMapper.toResponse(null));
//    }
//
//    @Test
//    void toEntity_shouldMapCorrectly() {
//
//        PlayerImportModel importModel = getPlayerMock();
//
//        PlayerEntity entity = new PlayerEntity();
//
//        PlayerEntity mapped = PlayerMapper.toEntity(entity, importModel);
//
//        assertNotNull(mapped);
//        assertEquals("Jane Smith", mapped.getFullName());
//        assertEquals("JS", mapped.getShortName());
//        assertEquals("India", mapped.getNationality());
//        assertEquals("Bowler", mapped.getRole());
//        assertEquals("Left-hand bat", mapped.getBattingStyle());
//        assertEquals("Leg spin", mapped.getBowlingStyle());
//        assertTrue(mapped.isActive());
//    }
//
//    public static PlayerImportModel getPlayerMock() {
//        PlayerImportModel importModel = new PlayerImportModel();
//        importModel.setFullName("Jane Smith");
//        importModel.setShortName("JS");
//        importModel.setNationality("India");
//        importModel.setRole("Bowler");
//        importModel.setBattingStyle("Left-hand bat");
//        importModel.setBowlingStyle("Leg spin");
//        return importModel;
//    }
//
//    private static PlayerEntity getPlayerMockEntity() {
//        PlayerEntity entity = new PlayerEntity();
//        entity.setId(1L);
//        entity.setFullName("John Doe");
//        entity.setShortName("JD");
//        entity.setNationality("Australia");
//        entity.setRole("Batsman");
//        entity.setBattingStyle("Right-hand bat");
//        entity.setBowlingStyle("Off spin");
//        entity.setTotalMatches(100);
//        entity.setTotalRuns(4000);
//        entity.setTotalWickets(50);
//        entity.setActive(true);
//
//        TeamEntity team = new TeamEntity();
//        team.setId(10L);
//        team.setName("Sydney Sixers");
//        entity.setTeams(Collections.singletonList(team));
//        return entity;
//    }
//
//    @Test
//    void toEntity_shouldThrowException_whenImportModelIsNull() {
//        PlayerEntity entity = new PlayerEntity();
//        assertThrows(NullPointerException.class, () -> {
//            PlayerMapper.toEntity(entity, null);
//        });
//        assertThrows(NullPointerException.class, () -> {
//            PlayerMapper.toEntity(null, new PlayerImportModel());
//        });
//    }
//}
