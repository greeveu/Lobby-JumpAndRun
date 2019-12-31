package eu.greev.jumpandrun;

import eu.greev.jumpandrun.classes.JumpAndRun;
import eu.greev.jumpandrun.classes.Maths;
import eu.greev.jumpandrun.listeners.InteractListener;
import eu.greev.jumpandrun.listeners.MoveListener;
import eu.greev.jumpandrun.listeners.QuitListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class JumpAndRuns extends JavaPlugin {

    Maths maths = new Maths();
    static JumpAndRuns t;
    public static String prefix = "§a<§6Greev.eu§a>§7 ";

    public static ArrayList<Player> cooldownList = new ArrayList<>();

    @Override
    public void onEnable() {
        t = this;
        registerListeners();
    }

    @Override
    public void onDisable() {
        for (JumpAndRun jumpAndRun : JumpAndRun.jumpAndRuns.values()) {
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

    public Maths getMaths() {
        return maths;
    }
}
