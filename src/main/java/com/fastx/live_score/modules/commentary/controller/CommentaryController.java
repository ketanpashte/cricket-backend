package com.fastx.live_score.modules.commentary.controller;

import com.fastx.live_score.core.config.APiConfig;
import com.fastx.live_score.core.utils.AppResponse;
import com.fastx.live_score.modules.commentary.entity.CommentaryEntity;
import com.fastx.live_score.modules.commentary.entity.OverSummaryEntity;
import com.fastx.live_score.modules.commentary.service.CommentaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(APiConfig.API_VERSION_1 + "/matches/commentary")
@RequiredArgsConstructor
@Tag(name = "Match Commentary", description = "API for match commentary")
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/{matchId}")
    public AppResponse<List<CommentaryEntity>> getCommentary(@PathVariable String matchId) {
        return AppResponse.success(commentaryService.getCommentary(matchId));
    }

    @GetMapping("/{matchId}/summaries")
    public AppResponse<List<OverSummaryEntity>> getOverSummaries(@PathVariable String matchId) {
        return AppResponse.success(commentaryService.getOverSummaries(matchId));
    }

    @PutMapping("/{id}")
    public AppResponse<CommentaryEntity> updateCommentary(@PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String text = request.get("commentaryText");
        return AppResponse.success(commentaryService.updateCommentary(id, text));
    }
}
