package club.devcord.gamejam.event;

import club.devcord.gamejam.logic.team.Team;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamEvent extends Event {
    private final Team team;

    public TeamEvent(Team team) {
        this.team = team;
    }

    public Team team() {
        return team;
    }

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
