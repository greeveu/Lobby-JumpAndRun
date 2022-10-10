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
import org.bukkit.util.Vector;

public class InteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block.getType() != Material.GOLD_PLATE || block.getRelative(BlockFace.DOWN).getType() != Material.STAINED_CLAY) {
            return;
        }

        Player player = event.getPlayer();
        Location plateLocation = block.getLocation();

        JumpAndRuns.getInstance().getJumpAndRunList().stream()
            .filter(j -> j.getPlayer().equals(player))
            .findFirst()
            .ifPresent(JumpAndRun::cancel);

        Location startLocation = block.getLocation();
        int count = 0;
        do {
            if (count > 50) {
                player.sendMessage("[lang]lobby.jumpandrun.nostartfound[/lang]");

                Vector vec = player.getLocation().toVector().subtract(plateLocation.toVector());
                vec.setY(1.0D).normalize();
                player.setVelocity(vec);

                return;
            }

            startLocation.setX(plateLocation.getX() + Maths.randInt(-5, 5));
            startLocation.setY(plateLocation.getY() + Maths.randInt(20, 50));
            startLocation.setZ(plateLocation.getZ() + Maths.randInt(-5, 5));
            count++;
        } while (!startLocation.getBlock().isEmpty() || !startLocation.getBlock().getRelative(BlockFace.UP).isEmpty());

        JumpAndRun jumpAndRun = new JumpAndRun(player, startLocation);
        JumpAndRuns.getInstance().getJumpAndRunList().add(jumpAndRun);

        event.setCancelled(true);
    }
}
