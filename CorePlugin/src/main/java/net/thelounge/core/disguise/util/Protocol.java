package net.thelounge.core.disguise.util;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Protocol {

	public EntityPlayer getEntity(Player player) {
		return ((CraftPlayer)player).getHandle();
	}

	public boolean isClient18(Player player) {
		return getEntity(player).playerConnection.networkManager.getVersion() > 5;
	}

	public void sendPacket(Player player, Packet packet) {
		getEntity(player).playerConnection.sendPacket(packet);
	}

	public void broadcastWorld(Packet packet, Player player) {
		for (Player online : player.getWorld().getPlayers()) {
			if ((online != null) && (online.getEntityId() != player.getEntityId()) && (online.canSee(player))) {
				sendPacket(online, packet);
			}
		}
	}

	public void broadcastServer(Packet packet, Player player) {
		for (Player all : Bukkit.getServer().getOnlinePlayers()) {
			if(all.canSee(player)) {
				sendPacket(all, packet);
			}
		}
	}
}
