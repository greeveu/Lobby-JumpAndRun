package eu.greev.jumpandrun.classes;

import eu.greev.jumpandrun.JumpAndRuns;
import eu.greev.jumpandrun.utils.Maths;
import eu.greev.jumpandrun.utils.Output;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class JumpAndRun {
    @Getter @Setter private Player player;
    @Getter @Setter private int jumpCount = 0;
    @Getter @Setter private Location startLocation;
    @Getter private Location endLocation;

    private final byte colorCode;
    private final BukkitTask task;

    public JumpAndRun(Player player, Location startLocation) {
        this.player = player;
        this.startLocation = startLocation;
        this.colorCode = (byte) Maths.randInt(0, 15);
        generateEndLocation();

        placeStartBlock();
        placeEndBlock();

        player.teleport(this.startLocation.clone().add(0.5, 1, 0.5));

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                Output.sendActionBar(player, getActionBarText());
            }
        }.runTaskTimer(JumpAndRuns.getInstance(), 0, 20);
    }

    public void nextJump() {
        this.player.playSound(this.player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
        removeStartBlock();
        setStartLocation(this.endLocation);
        generateEndLocation();
        placeStartBlock();
        placeEndBlock();
        this.jumpCount++;
        updateActionBar();
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
        int yAdd = 0;
        int offset = Maths.randInt(0, 3);
        int xAdd;
        int zAdd;

        if (this.startLocation.getBlockY() < 256) {
            yAdd = Maths.randInt(0, 1);
        }

        if (yAdd == 1) {
            maxLength--;
        }
        if (offset/2 == 1) {
            maxLength--;
        }

        if (Math.random() < 0.5) {
            xAdd = Maths.randInt(2, maxLength);
            zAdd = offset;
        } else {
            xAdd = offset;
            zAdd = Maths.randInt(2, maxLength);
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

    public void cancel() {
        removeStartBlock();
        removeEndBlock();
        this.player.playSound(this.player.getLocation(), Sound.NOTE_BASS, 1, 1);
        this.task.cancel();

        JumpAndRuns.getInstance().getJumpAndRunList().remove(this);
    }

    public String getActionBarText() {
        String text = "§7Du hast bereits §e§l" + jumpCount + " §r§7";
        text += jumpCount == 1 ? "Sprung geschafft" : "Sprünge geschafft";

        return text;
    }

    public void updateActionBar() {
        Output.sendActionBar(this.player, this.getActionBarText());
    }
}
