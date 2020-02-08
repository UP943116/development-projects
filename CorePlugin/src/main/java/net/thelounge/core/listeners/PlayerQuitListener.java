package net.thelounge.core.listeners;


import net.thelounge.core.CorePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

	private final CorePlugin corePlugin;

	public PlayerQuitListener(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}


	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
		playerQuitEvent.setQuitMessage(null);
		playerQuitEvent.getPlayer().setOp(false);

		corePlugin.getUserManager().removeUser(playerQuitEvent.getPlayer().getUniqueId());

	}
}
