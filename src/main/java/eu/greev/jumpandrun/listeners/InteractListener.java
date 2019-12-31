package eu.greev.jumpandrun.listeners;

import eu.greev.jumpandrun.JumpAndRuns;
import eu.greev.jumpandrun.classes.JumpAndRun;
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
        if (block.getType() != Material.GOLD_PLATE) {
            return;
        }
        if (block.getRelative(BlockFace.DOWN).getType() != Material.STAINED_CLAY) {
            return;
        }

        Player player = event.getPlayer();
        JumpAndRun currentRun = JumpAndRun.jumpAndRuns.get(player);
        if (currentRun != null) {
            currentRun.cancel();
        }

        Location startLocation;

        int tryCount = 0;
        do {
            startLocation = block.getLocation();
            startLocation.setX(startLocation.getX() + JumpAndRuns.getInstance().getMaths().randInt(-5, 5));
            startLocation.setY(startLocation.getY() + JumpAndRuns.getInstance().getMaths().randInt(20, 50));
            startLocation.setZ(startLocation.getZ() + JumpAndRuns.getInstance().getMaths().randInt(-5, 5));

            tryCount++;
            if (tryCount >= 50) {
                player.sendMessage(
                    JumpAndRuns.prefix
                    + "§cEs konnte leider keine freie Stelle für dich gefunden werden. Bitte versuche es noch einmal."
                );

                Vector ent = player.getLocation().toVector().subtract(block.getLocation().toVector().add(new Vector(0, 1, 0)));
                ent.setY(1.0D);
                player.setVelocity(ent.multiply(0.5D));

                event.setCancelled(true);
                return;
            }
        } while (startLocation.getBlock().getType() != Material.AIR);

        JumpAndRun jumpAndRun = new JumpAndRun(player, startLocation);

        event.setCancelled(true);
    }
}
