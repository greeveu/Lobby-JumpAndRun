package eu.greev.jumpandrun;

import eu.greev.jumpandrun.classes.Maths;
import eu.greev.jumpandrun.listeners.InteractListener;
import eu.greev.jumpandrun.listeners.MoveListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class JumpAndRuns extends JavaPlugin {

    Maths maths = new Maths();
    static JumpAndRuns t;

    @Override
    public void onEnable() {
        t = this;
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new MoveListener(), this);
    }

    public static JumpAndRuns getInstance() {
        return t;
    }

    public Maths getMaths() {
        return maths;
    }
}
