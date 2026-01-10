//package com.fastx.live_score.services.servicesImpl;
//
//
//import com.fastx.live_score.csv.models.PlayerImportModel;
//import com.fastx.live_score.adapter.models.response.Player;
//import com.fastx.live_score.entities.PlayerEntity;
//import com.fastx.live_score.exception.PlayerNotFoundException;
//import com.fastx.live_score.repository.PlayerRepository;
//import com.fastx.live_score.repository.TeamRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class PlayerServiceImplTest {
//
//    @Mock
//    private PlayerRepository playerRepository;
//
//    @Mock
//    private TeamRepository teamRepository;
//
//    @InjectMocks
//    private PlayerServiceImpl playerService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    // --- createPlayer ---
//    @Test
//    void createPlayer_shouldSaveEntities_whenValidRequestList() {
//        List<PlayerImportModel> requests = List.of(
//                new PlayerImportModel("John Doe", "JD", "USA", "Batsman", "Right", "None")
//        );
//
//        playerService.createPlayer(requests);
//
//        verify(playerRepository, times(1)).saveAll(anyList());
//    }
//
//    @Test
//    void createPlayer_shouldNotSave_whenEmptyList() {
//        playerService.createPlayer(Collections.emptyList());
//        verify(playerRepository, never()).saveAll(anyList());
//    }
//
//    // --- getPlayerById ---
//    @Test
//    void getPlayerById_shouldReturnPlayer_whenFound() {
//        PlayerEntity entity = new PlayerEntity();
//        entity.setId(1L);
//        entity.setFullName("John Doe");
//
//        when(playerRepository.findById(1L)).thenReturn(Optional.of(entity));
//
//        Player result = playerService.getPlayerById(1L);
//        assertEquals("John Doe", result.getFullName());
//    }
//
//    @Test
//    void getPlayerById_shouldThrow_whenNotFound() {
//        when(playerRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(PlayerNotFoundException.class, () -> playerService.getPlayerById(1L));
//    }
//
//    // --- getPlayersByTeamId ---
//    @Test
//    void getPlayersByTeamId_shouldReturnList_whenTeamExists() {
//        when(teamRepository.existsById(1L)).thenReturn(true);
//
//        PlayerEntity player = new PlayerEntity();
//        player.setId(1L);
//        player.setFullName("John");
//
//        when(playerRepository.findByTeamId(1L)).thenReturn(List.of(player));
//
//        List<Player> players = playerService.getPlayersByTeamId(1L);
//
//        assertEquals(1, players.size());
//    }
//
//    @Test
//    void getPlayersByTeamId_shouldThrow_whenTeamDoesNotExist() {
//        when(teamRepository.existsById(1L)).thenReturn(false);
//
//        assertThrows(PlayerNotFoundException.class, () -> playerService.getPlayersByTeamId(1L));
//    }
//
//    // --- getAllPlayers ---
//    @Test
//    void getAllPlayers_shouldReturnAll() {
//        PlayerEntity player = new PlayerEntity();
//        player.setId(1L);
//        player.setFullName("John");
//
//        when(playerRepository.findAll()).thenReturn(List.of(player));
//
//        List<Player> all = playerService.getAllPlayers();
//
//        assertEquals(1, all.size());
//    }
//
//    // --- updatePlayer ---
//    @Test
//    void updatePlayer_shouldUpdate_whenValid() {
//        PlayerEntity existing = new PlayerEntity();
//        existing.setId(1L);
//        existing.setFullName("Old");
//
//        PlayerImportModel update = new PlayerImportModel("New", "N", "IND", "Bowler", "Right", "Off");
//
//        when(playerRepository.findById(1L)).thenReturn(Optional.of(existing));
//        when(playerRepository.save(any())).thenAnswer(i -> i.getArgument(0));
//
//        Player result = playerService.updatePlayer(1L, update);
//
//        assertEquals("New", result.getFullName());
//    }
//
//    @Test
//    void updatePlayer_shouldThrow_whenRequestIsNull() {
//        assertThrows(IllegalArgumentException.class, () -> playerService.updatePlayer(1L, null));
//    }
//
//    @Test
//    void updatePlayer_shouldThrow_whenNotFound() {
//        when(playerRepository.findById(1L)).thenReturn(Optional.empty());
//
//        PlayerImportModel update = new PlayerImportModel("New", "N", "IND", "Bowler", "Right", "Off");
//        assertThrows(PlayerNotFoundException.class, () -> playerService.updatePlayer(1L, update));
//    }
//
//    // --- deletePlayer ---
//    @Test
//    void deletePlayer_shouldDelete_whenExists() {
//        when(playerRepository.existsById(1L)).thenReturn(true);
//        playerService.deletePlayer(1L);
//        verify(playerRepository).deleteById(1L);
//    }
//
//    @Test
//    void deletePlayer_shouldThrow_whenNotFound() {
//        when(playerRepository.existsById(1L)).thenReturn(false);
//        assertThrows(PlayerNotFoundException.class, () -> playerService.deletePlayer(1L));
//    }
//}
