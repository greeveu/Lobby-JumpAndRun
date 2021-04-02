package eu.greev.jumpandrun;

import eu.greev.jumpandrun.classes.JumpAndRun;
import eu.greev.jumpandrun.listeners.InteractListener;
import eu.greev.jumpandrun.listeners.MoveListener;
import eu.greev.jumpandrun.listeners.QuitListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class JumpAndRuns extends JavaPlugin {
    //TODO: add lombok

    List<JumpAndRun> jumpAndRunList = new ArrayList<>();
    static JumpAndRuns t;

    @Override
    public void onEnable() {
        t = this;
        registerListeners();
    }

    @Override
    public void onDisable() {
        for (JumpAndRun jumpAndRun : this.jumpAndRunList) {
            jumpAndRun.cancel();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new MoveListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
    }

    public static JumpAndRuns getInstance() {
        return t;
    }

    public List<JumpAndRun> getJumpAndRunList() {
        return this.jumpAndRunList;
    }
}
