package com.fastx.live_score.util;

import org.example.Innings;
import org.example.Match;
import org.example.MatchControl;
import org.example.events.BallCompletedEvent;
import org.example.events.OverCompletedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchUtil {
    public static List<BallCompletedEvent> getLastNBallEvents(MatchControl matchControl, int numberOfBalls) {
        List<BallCompletedEvent> all = matchControl.eventStream(BallCompletedEvent.class).toList();
        int total = all.size();
        if (total == 0) return Collections.emptyList();

        List<BallCompletedEvent> result;
        if (numberOfBalls == 0) {
            result = new ArrayList<>(all);
        } else {
            result = new ArrayList<>(all.subList(Math.max(0, total - numberOfBalls), total));
        }

        Collections.reverse(result);
        return result;
    }



}

