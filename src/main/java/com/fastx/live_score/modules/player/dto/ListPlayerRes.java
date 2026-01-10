package com.fastx.live_score.modules.player.dto;

import com.fastx.live_score.modules.player.entity.PlayerEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ListPlayerRes {
    private Long id;
    private String name;
    private String nationality;
    private String role;

    public static ListPlayerRes toShortPlayer(Player player) {
        return new ListPlayerRes(player.getId(), player.getShortName(), player.getNationality(), player.getRole());
    }
    public static ListPlayerRes toShortPlayer(PlayerEntity player) {
        return new ListPlayerRes(player.getId(), player.getShortName(), player.getNationality(), player.getRole());
    }

}
