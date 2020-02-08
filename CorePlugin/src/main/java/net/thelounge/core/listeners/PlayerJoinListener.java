package net.thelounge.core.listeners;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.CoreUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerJoinListener implements Listener {

	private final CorePlugin corePlugin;

	public PlayerJoinListener(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerJoin(AsyncPlayerPreLoginEvent playerPreLoginEvent) {
		Long ipId = corePlugin.getSqlManager().logIp(playerPreLoginEvent.getAddress().getHostAddress());

		CoreUser coreUser = corePlugin.getUserManager().getUser(playerPreLoginEvent.getUniqueId());
		coreUser.setUserName(playerPreLoginEvent.getName());

		corePlugin.getSqlManager().loadUser(coreUser);
		corePlugin.getSqlManager().saveIp(coreUser, ipId);
		corePlugin.getSqlManager().loadUserExtra(coreUser);
		corePlugin.getSqlManager().loadUserGroups(coreUser);
		corePlugin.getSqlManager().loadDisguise(coreUser);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent playerLoginEvent) {
		CoreUser coreUser = corePlugin.getUserManager().getUser(playerLoginEvent.getPlayer().getUniqueId());
		corePlugin.getPermissionsHandler().addPermissions(playerLoginEvent.getPlayer(), coreUser);

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
		playerJoinEvent.setJoinMessage(null);

		CoreUser coreUser = corePlugin.getUserManager().getUser(playerJoinEvent.getPlayer().getUniqueId());

		coreUser.updatePrefix();

		if(coreUser.getSqlReDisguiseObject() != null) {
			corePlugin.getDisguiseManager().reDisguisePlayer(playerJoinEvent.getPlayer(), coreUser.getSqlReDisguiseObject());
		}
	}
}
