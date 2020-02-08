package net.thelounge.core.listeners;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.events.ReportUserEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ReportUserListener implements Listener {

	private final CorePlugin corePlugin;

	public ReportUserListener(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@EventHandler
	public void onReportUser(ReportUserEvent reportUserEvent) {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.hasPermission("perms.staff")) {
				if(corePlugin.getUserManager().getUser(player.getUniqueId()).isStaffNotifications()) {
					player.sendMessage(
									"§f[§9REPORT§f]" +
									"[§7" + reportUserEvent.getUserServer() + "§f] " +
									"§9" + reportUserEvent.getUserName() + "§7 has reported §c" +
									reportUserEvent.getUserReported() + "§7 for §b" +
									reportUserEvent.getUserReportedReason()
					);
				}
			}
		}
	}
}
