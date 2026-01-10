package com.fastx.live_score.modules.liveMatch.controller;

import com.fastx.live_score.modules.liveMatch.request.AddTipRequest;
import com.fastx.live_score.core.config.APiConfig;
import com.fastx.live_score.core.utils.AppResponse;
import com.fastx.live_score.modules.tips.TipUseCase;
import com.fastx.live_score.modules.tips.Tip;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(APiConfig.API_VERSION_1 + "/match/tip")
@Tag(name = "Tips Api")
@RequiredArgsConstructor
public class TipController {

    private final TipUseCase tipUseCase;

    @PostMapping("/addTip/{id}")
    public AppResponse<String> addTips(@PathVariable(name = "id") Long matchId, @RequestBody @Valid AddTipRequest tip) {
        tipUseCase.addTipToMatch(matchId, tip.getTipData());
        return AppResponse.success(null, "Tips Added successfully");
    }

    @PutMapping("/updateTip/{id}")
    public AppResponse<String> updateTip(@PathVariable(name = "id") Long tipId, @RequestBody @Valid String tipRequest) {
        tipUseCase.updateTip(tipId, tipRequest);
        return AppResponse.success(null, "Tips updated successfully");
    }

    @GetMapping("/getMatchTips/{id}")
    public AppResponse<List<Tip>> getAllTipsOfTheMatch(@PathVariable(name = "id") Long matchId) {
        List<Tip> matchTips = tipUseCase.getMatchTips(matchId);
        return AppResponse.success(matchTips);
    }

    @DeleteMapping("/{id}")
    public AppResponse<String> deleteTips(@PathVariable(name = "id") Long tipId) {
        tipUseCase.deleteTip(tipId);
        return AppResponse.success(null, "Tips deleted successfully");
    }

}
