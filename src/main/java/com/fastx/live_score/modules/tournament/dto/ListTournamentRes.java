package com.fastx.live_score.modules.tournament.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ListTournamentRes {
    private final Long id;
    private final String name;
    private final String image;
    private final TournamentStatus tournamentStatus;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private ListTournamentRes(Long id, String name, String image, TournamentStatus tournamentStatus, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.tournamentStatus = tournamentStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public static ListTournamentRes from(Tournament tournament) {
        return new ListTournamentRes(tournament.getId(),
                tournament.getName(),
                tournament.getLogoUrl(),
                tournament.getTournamentStatus(),
                tournament.getStartDate().toLocalDate(),
                tournament.getEndDate().toLocalDate());
    }
}
