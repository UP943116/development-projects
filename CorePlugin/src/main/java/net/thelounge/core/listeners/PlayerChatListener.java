package net.thelounge.core.listeners;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.CoreUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

	private final CorePlugin corePlugin;

	public PlayerChatListener(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}


	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent playerChatEvent) {
		CoreUser coreUser = corePlugin.getUserManager().getUser(playerChatEvent.getPlayer().getUniqueId());

		if(corePlugin.getPlayerCooldownTask().hasChatCooldown(coreUser)) {
			playerChatEvent.getPlayer().sendMessage(corePlugin.getPrefix() + "§cYou are currently on chat-cooldown.");
			playerChatEvent.setCancelled(true);
			return;
		}

		if(!playerChatEvent.getPlayer().hasPermission("perms.staff")) {
			corePlugin.getPlayerCooldownTask().addChatCooldown(coreUser);
		}

		playerChatEvent.setMessage(playerChatEvent.getMessage().replaceAll("%", "%%"));

		playerChatEvent.setMessage(playerChatEvent.getMessage().replaceAll("<3", "§c❤§f"));
		if(!coreUser.isDisguised()) {
			playerChatEvent.setFormat(coreUser.getPrefix() + playerChatEvent.getPlayer().getName() + "§7: §f" +  playerChatEvent.getMessage());
		} else {
			playerChatEvent.setFormat("§7" + playerChatEvent.getPlayer().getName() + "§7: §f" +  playerChatEvent.getMessage());
		}


		if(corePlugin.isChatMuted() && !playerChatEvent.getPlayer().hasPermission("perms.staff")) {
			playerChatEvent.setCancelled(true);
			playerChatEvent.getPlayer().sendMessage(corePlugin.getPrefix() + "§cGlobal chat is disabled.");
			return;
		}
	}
}
