package eu.greev.jumpandrun.classes;

import eu.greev.jumpandrun.JumpAndRuns;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class JumpAndRun {
    public static HashMap<Player, JumpAndRun> jumpAndRuns = new HashMap<>();

    private Player player;
    private int jumpCount = 0;

    private Location startLocation;
    private Location endLocation;

    private byte colorCode;

    public JumpAndRun(Player player, Location startLocation) {
        this.player = player;
        this.startLocation = startLocation;
        this.colorCode = (byte) JumpAndRuns.getInstance().getMaths().randInt(0, 15);
        generateEndLocation();

        jumpAndRuns.put(player, this);

        placeStartBlock();
        placeEndBlock();

        player.teleport(this.startLocation.clone().add(0.5, 1, 0.5));
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
        Block block = this.endLocation.getBlock();
        block.setType(Material.WOOL);
        block.setData(this.colorCode);
    }

    public void removeEndBlock() {
        Block block = this.endLocation.getBlock();
        block.setType(Material.AIR);
    }

    public void generateEndLocation() {
        int maxLength = 5;
        int yAdd = JumpAndRuns.getInstance().getMaths().randInt(0, 1);
        int offset = JumpAndRuns.getInstance().getMaths().randInt(0, 3);
        int xAdd;
        int zAdd;

        if (yAdd == 1) {
            maxLength--;
        }
        if (offset/2 == 1) {
            maxLength--;
        }

        if (Math.random() < 0.5) {
            xAdd = JumpAndRuns.getInstance().getMaths().randInt(2, maxLength);
            zAdd = offset;
        } else {
            xAdd = offset;
            zAdd = JumpAndRuns.getInstance().getMaths().randInt(2, maxLength);
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

    public int getJumpCount() {
        return jumpCount;
    }

    public void setJumpCount(int jumpCount) {
        this.jumpCount = jumpCount;
    }

    public void cancel() {
        removeStartBlock();
        removeEndBlock();
        jumpAndRuns.remove(this.player);
    }
}
