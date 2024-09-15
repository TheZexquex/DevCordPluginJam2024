package club.devcord.gamejam.event;

import club.devcord.gamejam.logic.team.Team;

public class TeamBedDestroyEvent extends TeamEvent {
    public TeamBedDestroyEvent(Team team) {
        super(team);
    }
}
