package eu.greev.jumpandrun.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JumpAndRun {
    public static List<JumpAndRun> jumpAndRuns = new ArrayList<>();

    private Player player;

    private Location startLocation;
    private Location endLocation;

    private byte colorCode;

    public JumpAndRun(Player player, Location startLocation) {
        this.player = player;
        this.startLocation = startLocation;
        this.colorCode = (byte) Maths.randInt(0, 15);
        generateEndLocation();

        jumpAndRuns.add(this);

        placeStartBlock();
        placeEndBlock();

        player.teleport(this.startLocation.add(0.5, 1, 0.5));
    }

    public void placeStartBlock() {
        Block block = this.startLocation.getBlock();
        block.setType(Material.STAINED_CLAY);
        block.setData(this.colorCode);
    }

    public void removeStartBlock() {
        Block block = this.startLocation.getBlock();
        block.setType(Material.AIR);
    }

    public void placeEndBlock() {
        Block block = this.startLocation.getBlock();
        block.setType(Material.WOOL);
        block.setData(this.colorCode);
    }

    public void removeEndBlock() {
        Block block = this.endLocation.getBlock();
        block.setType(Material.AIR);
    }

    public void generateEndLocation() {
        int maxLength = 5;
        int yAdd = Maths.randInt(0, 1);
        int offset = Maths.randInt(0, 3);
        int xAdd;
        int zAdd;

        if (yAdd == 1) {
            maxLength--;
        }
        if (offset/2 == 1) {
            maxLength--;
        }

        if (Math.random() < 0.5) {
            xAdd = maxLength;
            zAdd = offset;
        } else {
            xAdd = offset;
            zAdd = maxLength;
        }
        if (Math.random() < 0.5) {
            xAdd *= -1;
        }
        if (Math.random() < 0.5) {
            zAdd *= -1;
        }

        this.endLocation = new Location(
            this.startLocation.getWorld(),
            this.startLocation.getX() + xAdd,
            this.startLocation.getY() + yAdd,
            this.startLocation.getZ() + zAdd
        );
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Location getStartLocation() {
        return this.startLocation;
    }

    public void setStartLocation(Location location) {
        this.startLocation = location;
    }

    public Location getEndLocation() {
        return this.endLocation;
    }

    public void cancel() {
        removeStartBlock();
        removeEndBlock();
        jumpAndRuns.remove(this);
    }
}
