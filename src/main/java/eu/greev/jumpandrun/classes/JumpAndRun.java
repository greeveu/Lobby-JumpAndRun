package eu.greev.jumpandrun.classes;

import eu.greev.jumpandrun.JumpAndRuns;
import eu.greev.jumpandrun.utils.Maths;
import eu.greev.jumpandrun.utils.Output;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
        boolean success = this.generateEndLocation(startLocation);

        this.task = Bukkit.getScheduler().runTaskTimer(
            JumpAndRuns.getInstance(),
            () -> Output.sendActionBar(player, this.getActionBarText()),
            0, 20
        );

        if (!success) return;

        this.placeStartBlock();
        this.placeEndBlock();

        player.teleport(this.startLocation.clone().add(0.5, 1, 0.5));
    }

    public void nextJump() {
        this.player.playSound(this.player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
        this.removeStartBlock();
        Location lastStartLoc = this.startLocation;
        this.startLocation = this.endLocation;
        this.generateEndLocation(lastStartLoc);
        this.placeStartBlock();
        this.placeEndBlock();
        this.jumpCount++;
        this.updateActionBar();
    }

    public void placeStartBlock() {
        Block block = this.startLocation.getBlock();
        block.setType(Material.STAINED_CLAY);
        block.setData(this.colorCode);
    }

    public void removeStartBlock() {
        if (this.startLocation == null) return;
        Block block = this.startLocation.getBlock();
        block.setType(Material.AIR);
    }

    public void placeEndBlock() {
        Block block = this.endLocation.getBlock();
        block.setType(Material.WOOL);
        block.setData(this.colorCode);
    }

    public void removeEndBlock() {
        if (this.endLocation == null) return;
        Block block = this.endLocation.getBlock();
        block.setType(Material.AIR);
    }

    private List<Vector> getPossibleJumpVectors(boolean yDown, boolean yUp) {
        List<Vector> jumpLocations = new ArrayList<>();

        IntStream.builder().add(-1).add(1).build().forEach(
            xInvert -> IntStream.builder().add(-1).add(1).build().forEach(
                zInvert -> IntStream.rangeClosed(yDown ? -1 : 0, yUp ? 1 : 0).forEach(
                    yAdd -> IntStream.rangeClosed(0, 3).forEach(
                        zAdd -> IntStream.rangeClosed(2, 5 - (yAdd + Math.abs(yAdd)) / 2 - Math.floorDiv(zAdd, 2)).forEach(
                            xAdd -> jumpLocations.add(new Vector(xAdd * xInvert, yAdd, zAdd * zInvert))))))); //xAdd = length, zAdd = offset to the side

        return jumpLocations;
    }

    public boolean generateEndLocation(Location lastStartLoc) {
        List<Vector> jumpVectors = this.getPossibleJumpVectors(this.startLocation.getBlockY() > 200, this.startLocation.getBlockY() < 255);

        int index;
        Vector vec;
        Location loc;
        do {
            if (jumpVectors.isEmpty()) {
                this.player.sendMessage("[lang]lobby.jumpandrun.noendfound[/lang]");
                this.cancel();
                return false;
            }

            index = Maths.randInt(0, jumpVectors.size() - 1);
            vec = jumpVectors.get(index);
            loc = this.startLocation.clone().add(vec);

            jumpVectors.remove(index);
        } while (lastStartLoc.equals(loc) || !loc.getBlock().isEmpty());

        this.endLocation = loc;

        return true;
    }

    public void cancel() {
        this.removeStartBlock();
        this.removeEndBlock();
        this.player.playSound(this.player.getLocation(), Sound.NOTE_BASS, 1, 1);
        if (this.task != null) this.task.cancel();

        JumpAndRuns.getInstance().getJumpAndRunList().remove(this);
    }

    public String getActionBarText() {
//        String text = "§7Du hast bereits §e§l" + jumpCount + " §r§7";
//        text += jumpCount == 1 ? "Sprung geschafft" : "Sprünge geschafft";

        return String.format("[lang]lobby.jumpandrun.jumpcount[args][arg]%d[/arg][/args][/lang]", jumpCount);
    }

    public void updateActionBar() {
        Output.sendActionBar(this.player, this.getActionBarText());
    }
}
