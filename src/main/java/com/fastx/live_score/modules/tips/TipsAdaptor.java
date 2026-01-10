package com.fastx.live_score.modules.tips;

import com.fastx.live_score.core.exception.AppException;
import com.fastx.live_score.modules.match.db.MatchEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipsAdaptor implements TipsRepository {

    private final TipEntityRepository tipEntityRepository;

    private final MatchEntityRepository matchEntityRepository;

    @Override
    public void saveTips(List<Tip> tips) {
        List<TipEntity> list = tips.stream().map(tip -> {
            TipEntity tipEntity = new TipEntity();
            tipEntity.setTipData(tip.tipData());
            tipEntity.setMatchEntity(matchEntityRepository.findById(tip.matchId()).orElseThrow(() -> new AppException("No match available for this match id")));
            return tipEntity;
        }).toList();
        tipEntityRepository.saveAll(list);

    }

    @Override
    public List<Tip> getTipsPerMatch(Long matchId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<TipEntity> tipsPerMatch = tipEntityRepository.getTipsPerMatch(matchId, sort);
        return tipsPerMatch.stream().map(TipsAdaptor::getTipFromEntity).toList();
    }

    @Override
    public Tip getTip(Long tipId) {

        TipEntity tipEntity = tipEntityRepository.findById(tipId).orElseThrow(() ->
                new AppException("Tip not found"));

        return getTipFromEntity(tipEntity);
    }

    private static Tip getTipFromEntity(TipEntity tipEntity) {
        return new Tip(
                tipEntity.getId(),
                tipEntity.getTipData(),
                tipEntity.getMatchEntity().getId()
        );
    }

    @Override
    public void deleteTip(Long tipId) {
        tipEntityRepository.deleteById(tipId);
    }
}
