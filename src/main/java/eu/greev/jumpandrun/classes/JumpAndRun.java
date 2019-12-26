package eu.greev.jumpandrun.classes;

import eu.greev.jumpandrun.JumpAndRuns;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class JumpAndRun {
    public static HashMap<Player, JumpAndRun> jumpAndRuns = new HashMap<>();

    private Player player;
    private int jumpCount = 0;

    private Location startLocation;
    private Location endLocation;

    private byte colorCode;

    public JumpAndRun(Player player, Location startLocation) {
        this.player = player;
        this.startLocation = startLocation;
        this.colorCode = (byte) JumpAndRuns.getInstance().getMaths().randInt(0, 15);
        generateEndLocation();

        jumpAndRuns.put(player, this);

        placeStartBlock();
        placeEndBlock();

        player.teleport(this.startLocation.clone().add(0.5, 1, 0.5));

        new BukkitRunnable() {
            @Override
            public void run() {
                sendActionBar(player, "TEXT");
            }
        }.runTaskTimer(JumpAndRuns.getInstance(), 0, 20);
    }

    public void nextJump() {
        this.player.playSound(this.player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
        removeStartBlock();
        setStartLocation(this.endLocation);
        generateEndLocation();
        placeStartBlock();
        placeEndBlock();
        this.jumpCount++;
    }

    public void placeStartBlock() {
        Block block = this.startLocation.getBlock();
        block.setType(Material.STAINED_CLAY);
        block.setData(this.colorCode);
    }

    public void removeStartBlock() {
        System.out.println(startLocation);
        Block block = this.startLocation.getBlock();
        block.setType(Material.AIR);
    }

    public void placeEndBlock() {
        Block block = this.endLocation.getBlock();
        block.setType(Material.WOOL);
        block.setData(this.colorCode);
    }

    public void removeEndBlock() {
        System.out.println(endLocation);
        Block block = this.endLocation.getBlock();
        block.setType(Material.AIR);
    }

    public void generateEndLocation() {
        int maxLength = 5;
        int yAdd = JumpAndRuns.getInstance().getMaths().randInt(0, 1);
        int offset = JumpAndRuns.getInstance().getMaths().randInt(0, 3);
        int xAdd;
        int zAdd;

        if (yAdd == 1) {
            maxLength--;
        }
        if (offset/2 == 1) {
            maxLength--;
        }

        if (Math.random() < 0.5) {
            xAdd = JumpAndRuns.getInstance().getMaths().randInt(2, maxLength);
            zAdd = offset;
        } else {
            xAdd = offset;
            zAdd = JumpAndRuns.getInstance().getMaths().randInt(2, maxLength);
        }
        if (Math.random() < 0.5) {
            xAdd *= -1;
        }
        if (Math.random() < 0.5) {
            zAdd *= -1;
        }

        this.endLocation = new Location(
            this.startLocation.getWorld(),
            this.startLocation.getX() + xAdd,
            this.startLocation.getY() + yAdd,
            this.startLocation.getZ() + zAdd
        );
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Location getStartLocation() {
        return this.startLocation;
    }

    public void setStartLocation(Location location) {
        this.startLocation = location;
    }

    public Location getEndLocation() {
        return this.endLocation;
    }

    public int getJumpCount() {
        return jumpCount;
    }

    public void setJumpCount(int jumpCount) {
        this.jumpCount = jumpCount;
    }

    public void cancel() {
        removeStartBlock();
        removeEndBlock();
        this.player.playSound(this.player.getLocation(), Sound.NOTE_BASS, 1, 1);
        jumpAndRuns.remove(this.player);
    }

    public void sendActionBar(Player p, String msg) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
    }

    public void sendTitle(Player p, String text, String sbtext) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{text:\"§6" + text + "\"}");
        IChatBaseComponent chatSubtitle = IChatBaseComponent.ChatSerializer.a("{text:\"" + sbtext + "\"}");
        PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, 10, 60, 10);
        PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle packetPlayOutSubitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetPlayOutTimes);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetPlayOutTitle);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetPlayOutSubitle);
    }
}
