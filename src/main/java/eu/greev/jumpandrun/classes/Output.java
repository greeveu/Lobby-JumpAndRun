package eu.greev.jumpandrun.classes;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class Output {
    public static void sendActionBar(Player p, String msg) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
    }

    public static void sendTitle(Player p, String text, String sbtext) {
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
