package net.thelounge.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReportUserEvent extends Event {

	private static HandlerList handlerList = new HandlerList();

	private String userName, userServer, userReported, userReportedReason;

	public ReportUserEvent(String userName, String userServer, String userReported, String userReportedReason) {
		this.userName = userName;
		this.userServer = userServer;
		this.userReported = userReported;
		this.userReportedReason = userReportedReason;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserReported() {
		return userReported;
	}

	public String getUserReportedReason() {
		return userReportedReason;
	}

	public String getUserServer() {
		return userServer;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

}
