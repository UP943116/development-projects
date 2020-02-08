package net.thelounge.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class CoreUserRankUpdateEvent extends Event {

	private static HandlerList handlerList = new HandlerList();

	private final GroupAction groupAction;
	private final UUID uuid;
	private final String group;

	public CoreUserRankUpdateEvent(GroupAction groupAction, UUID uuid, String group) {
		this.groupAction = groupAction;
		this.uuid = uuid;
		this.group = group;
	}

	public UUID getUuid() {
		return uuid;
	}

	public GroupAction getGroupAction() {
		return groupAction;
	}

	public String getGroup() {
		return group;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	public enum GroupAction {
		ADD,
		REMOVE;
	}
}
