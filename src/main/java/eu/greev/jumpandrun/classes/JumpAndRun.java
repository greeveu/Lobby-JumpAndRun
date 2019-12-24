package eu.greev.jumpandrun.classes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JumpAndRun {
    public static List<JumpAndRun> jumpAndRuns = new ArrayList<>();

    private Player player;

    private Location startLocation;
    private Location endLocation;

    public JumpAndRun(Player player, Location startLocation) {
        this.player = player;
        this.startLocation = startLocation;
        generateEndLocation();

        jumpAndRuns.add(this);

        placeStartBlock();
        placeEndBlock();
    }

    public void placeStartBlock() {
    }

    public void placeEndBlock() {
    }

    public void generateEndLocation() {
        this.endLocation = this.startLocation;
        this.endLocation.setX(this.endLocation.getBlockY() + 5);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
