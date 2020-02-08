package net.thelounge.core.listeners;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.events.AdminChatEvent;
import net.thelounge.core.events.StaffChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StaffChatListener implements Listener {

	private final CorePlugin corePlugin;

	public StaffChatListener(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@EventHandler
	public void onStaffChat(StaffChatEvent staffChatEvent) {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.hasPermission("perms.staff")) {
				if(corePlugin.getUserManager().getUser(player.getUniqueId()).isStaffNotifications()) {
					player.sendMessage(
							"§4[STAFF]§f[§7MC§f]" +
									"[§7" + staffChatEvent.getStaffServer() + "§f] " +
									"§9" + staffChatEvent.getStaffUsername() + "§7: §f" +
									staffChatEvent.getStaffMessage()
					);
				}
			}
		}
	}

	@EventHandler
	public void onAdminChat(AdminChatEvent adminChatEvent) {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.hasPermission("perms.admin")) {
				if(corePlugin.getUserManager().getUser(player.getUniqueId()).isStaffNotifications()) {
					player.sendMessage(
							"§4[STAFF]§f[§cAC§f]" +
									"[§7" + adminChatEvent.getStaffServer() + "§f] " +
									"§c" + adminChatEvent.getStaffUsername() + "§7: §f" +
									adminChatEvent.getStaffMessage()
					);
				}
			}
		}
	}
}
