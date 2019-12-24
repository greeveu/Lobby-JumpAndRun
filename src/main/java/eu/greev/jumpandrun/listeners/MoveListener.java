package eu.greev.jumpandrun.listeners;

import eu.greev.jumpandrun.classes.JumpAndRun;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        for(JumpAndRun jumpAndRun : JumpAndRun.jumpAndRuns) {
            if (jumpAndRun.getPlayer() == player) {
                Block floorBlock = event.getTo().getBlock().getRelative(BlockFace.DOWN);
                if (floorBlock.getLocation() != jumpAndRun.getEndLocation()) {
                    if (floorBlock.getLocation().getBlockY() < jumpAndRun.getStartLocation().getBlockY() - 3) {
                        jumpAndRun.cancel();
                    }
                    return;
                }

                jumpAndRun.removeStartBlock();
                jumpAndRun.setStartLocation(jumpAndRun.getEndLocation());
                jumpAndRun.generateEndLocation();
                jumpAndRun.placeStartBlock();
                jumpAndRun.placeEndBlock();

                return;
            }
        }
    }
}
