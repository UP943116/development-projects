package net.thelounge.core.events;

import net.thelounge.core.user.CoreUser;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CoreUserNameColorChangeEvent extends Event {

	private static HandlerList handlerList = new HandlerList();

	public CoreUserNameColorChangeEvent(CoreUser coreUser, ChatColor chatColor) {
		this.coreUser = coreUser;
		this.chatColor = chatColor;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	private CoreUser coreUser;
	private ChatColor chatColor;

	public ChatColor getChatColor() {
		return chatColor;
	}

	public CoreUser getCoreUser() {
		return coreUser;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
