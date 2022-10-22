package eu.greev.jumpandrun.listeners;

import eu.greev.jumpandrun.JumpAndRuns;
import eu.greev.jumpandrun.classes.JumpAndRun;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Optional<JumpAndRun> optionalJumpAndRun = JumpAndRuns.getInstance().getJumpAndRunList().stream()
                .filter(j -> j.getPlayer().equals(player))
                .findFirst();

        if (!optionalJumpAndRun.isPresent()) {
            return;
        }

        JumpAndRun jumpAndRun = optionalJumpAndRun.get();

        Set<Location> possibleBlocks = new HashSet<>();
        Location to = event.getTo();
        Location floorBlock = to.getBlock().getRelative(BlockFace.DOWN).getLocation();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location location = new Location(to.getWorld(), to.getX() + (x * 0.3), floorBlock.getY(), to.getZ() + (z * 0.3)).getBlock().getLocation();
                possibleBlocks.add(location);
            }
        }

        if (possibleBlocks.contains(jumpAndRun.nextBlock())) {
            jumpAndRun.nextJump();
            return;
        }

        if (floorBlock.getY() < jumpAndRun.currentBlock().getY() - 3) {
            jumpAndRun.cancel();
        }
    }
}
