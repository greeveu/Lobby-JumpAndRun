package eu.greev.jumpandrun.listeners;

import eu.greev.jumpandrun.classes.JumpAndRun;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        JumpAndRun jumpAndRun = JumpAndRun.jumpAndRuns.get(player);
        if (jumpAndRun == null) {
            return;
        }

        jumpAndRun.cancel();
    }
}
