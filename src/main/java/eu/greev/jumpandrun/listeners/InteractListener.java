package eu.greev.jumpandrun.listeners;

import eu.greev.jumpandrun.JumpAndRuns;
import eu.greev.jumpandrun.classes.JumpAndRun;
import eu.greev.jumpandrun.utils.Maths;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block.getType() != Material.GOLD_PLATE) {
            return;
        }
        if (block.getRelative(BlockFace.DOWN).getType() != Material.STAINED_CLAY) {
            return;
        }

        Player player = event.getPlayer();
        Location startLocation = block.getLocation();

        JumpAndRuns.getInstance().getJumpAndRunList().stream()
            .filter(j -> j.getPlayer().equals(player))
            .findFirst()
            .ifPresent(JumpAndRun::cancel);

        startLocation.setX(startLocation.getX() + Maths.randInt(-5, 5));
        startLocation.setY(startLocation.getY() + Maths.randInt(20, 50));
        startLocation.setZ(startLocation.getZ() + Maths.randInt(-5, 5));

        JumpAndRun jumpAndRun = new JumpAndRun(player, startLocation);
        JumpAndRuns.getInstance().getJumpAndRunList().add(jumpAndRun);

        event.setCancelled(true);
    }
}
