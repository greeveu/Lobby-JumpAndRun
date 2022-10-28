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

import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class JumpAndRun {
    private static final int FUTURE_JUMPS = 4;
    @Getter @Setter private Player player;
    @Getter @Setter private int jumpCount = 0;
    @Getter LinkedList<Location> jumps = new LinkedList<>();
    @Getter Location initialLocation;

    private final byte colorCode;
    private BukkitTask task;

    public JumpAndRun(Player player, Location startLocation) {
        this.player = player;
        this.jumps.add(startLocation);
        this.initialLocation = startLocation;
        this.colorCode = (byte) Maths.randInt(0, 15);
        boolean success = this.generateFutureLocations();

        if (!success) return;

        this.task = Bukkit.getScheduler().runTaskTimer(
                JumpAndRuns.getInstance(),
                this::updateActionBar,
                0, 20
        );

        this.placeBlocks();

        player.teleport(startLocation.clone().add(0.5, 1, 0.5));
    }

    public void nextJump() {
        this.removeOldestJump();
        this.player.playSound(this.player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
        this.generateFutureLocations();
        this.placeBlocks();
        this.jumpCount++;
        this.updateActionBar();
    }

    private void removeOldestJump() {
        Location location = this.jumps.removeFirst();
        Block block = location.getBlock();
        block.setType(Material.AIR);
    }

    public void placeBlocks() {
        for (int i = 0; i < jumps.size(); i++) {
            Block block = jumps.get(i).getBlock();
            if (i == 0) {
                block.setType(Material.STAINED_CLAY);
                block.setData(this.colorCode);
            } else if (i == 1) {
                block.setType(Material.WOOL);
                block.setData(this.colorCode);
            } else {
                this.player.sendBlockChange(jumps.get(i), Material.STAINED_GLASS, this.colorCode);
            }
        }
    }

    private LinkedList<Vector> getPossibleJumpVectors(boolean mayGoDown, boolean tooHight) {
        LinkedList<Vector> jumpLocations = new LinkedList<>();

        IntStream.builder().add(-1).add(1).build().forEach(
                xInvert -> IntStream.builder().add(-1).add(1).build().forEach(
                        zInvert -> IntStream.rangeClosed(mayGoDown ? -1 : 0, tooHight ? 0 : 1).forEach(
                                yAdd -> IntStream.rangeClosed(0, 3).forEach(
                                        zAdd -> IntStream.rangeClosed(2, 5 - (yAdd + Math.abs(yAdd)) / 2 - Math.floorDiv(zAdd, 2)).forEach(
                                                xAdd -> jumpLocations.add(new Vector(xAdd * xInvert, yAdd, zAdd * zInvert)))))));

        return jumpLocations;
    }

    public boolean generateFutureLocations() {
        Location last = this.getJumps().getLast();
        LinkedList<Vector> possibleJumpVectors = getPossibleJumpVectors(last.getBlockY() - initialLocation.getBlockY() > 25, last.getBlockY() > 250);

        if (this.getJumps().size() >= 2) {
            Location secondToLast = this.getJumps().get(this.getJumps().size() - 2);
            possibleJumpVectors.removeIf(possibleNewVector -> secondToLast.distance(last.clone().add(possibleNewVector)) <= 3.75);

            //            possibleJumpVectors.removeIf(vector1 -> vector.angle(vector1) < 90);
        }

        Collections.shuffle(possibleJumpVectors);

        Vector vec;
        Location loc;
        do {
            if (possibleJumpVectors.isEmpty()) {
                this.player.sendMessage("[lang]lobby.jumpandrun.noendfound[/lang]");
                this.cancel();
                return false;
            }

            vec = possibleJumpVectors.poll();
            loc = last.clone().add(vec);
        } while (!(loc.getBlock().isEmpty() && loc.clone().add(0,1,0).getBlock().isEmpty() && loc.clone().add(0,2,0).getBlock().isEmpty()));

        this.jumps.add(loc);

        if (FUTURE_JUMPS > this.getJumps().size()) {
            return generateFutureLocations();
        }

        return true;
    }

    public void cancel() {
        jumps.forEach(location -> {
            location.getBlock().setType(Material.AIR);
            this.player.sendBlockChange(location, Material.AIR, (byte) 0);
        });

        this.player.playSound(this.player.getLocation(), Sound.NOTE_BASS, 1, 1);
        if (this.task != null) {
            this.task.cancel();
        }

        JumpAndRuns.getInstance().getJumpAndRunList().remove(this);
    }

    public void updateActionBar() {
        Output.sendActionBar(this.player, String.format("[lang]lobby.jumpandrun.jumpcount[args][arg]%d[/arg][/args][/lang]", jumpCount));
    }

    public Location currentBlock() {
        return jumps.getFirst();
    }

    public Location nextBlock() {
        return jumps.get(1); //Next block
    }
}
