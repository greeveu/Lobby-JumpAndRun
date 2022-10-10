package eu.greev.jumpandrun;

import eu.greev.jumpandrun.classes.JumpAndRun;
import eu.greev.jumpandrun.listeners.InteractListener;
import eu.greev.jumpandrun.listeners.MoveListener;
import eu.greev.jumpandrun.listeners.QuitListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class JumpAndRuns extends JavaPlugin {
    public static final String prefix = "[lang]prefix[/lang]";

    @Getter private final List<JumpAndRun> jumpAndRunList = new ArrayList<>();
    @Getter private static JumpAndRuns instance;

    @Override
    public void onEnable() {
        instance = this;
        registerListeners();
    }

    @Override
    public void onDisable() {
        this.jumpAndRunList.forEach(JumpAndRun::cancel);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new MoveListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
    }
}
