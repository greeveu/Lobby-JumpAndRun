package eu.greev.jumpandrun.listeners;

import eu.greev.jumpandrun.JumpAndRuns;
import eu.greev.jumpandrun.classes.JumpAndRun;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

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
        Block floorBlock = event.getTo().getBlock().getRelative(BlockFace.DOWN);
        if (!floorBlock.getLocation().equals(jumpAndRun.nextBlock())) {
            if (floorBlock.getLocation().getY() < jumpAndRun.currentBlock().getY() - 3) {
                jumpAndRun.cancel();
            }
            return;
        }

        jumpAndRun.nextJump();
    }
}
