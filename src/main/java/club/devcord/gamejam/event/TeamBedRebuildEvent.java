package club.devcord.gamejam.event;

import club.devcord.gamejam.logic.team.Team;

public class TeamBedRebuildEvent extends TeamEvent {
    public TeamBedRebuildEvent(Team team) {
        super(team);
    }
}
