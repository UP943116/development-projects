package net.thelounge.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AdminChatEvent extends Event {

	private static HandlerList handlerList = new HandlerList();

	private String staffUsername, staffServer, staffMessage;

	public AdminChatEvent(String staffUsername, String staffServer, String staffMessage) {
		this.staffUsername = staffUsername;
		this.staffServer = staffServer;
		this.staffMessage = staffMessage;
	}

	public String getStaffServer() {
		return staffServer;
	}

	public String getStaffMessage() {
		return staffMessage;
	}

	public String getStaffUsername() {
		return staffUsername;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

}
