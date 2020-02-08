package net.thelounge.core.user;

import net.thelounge.core.group.PermissionGroup;
import net.thelounge.core.CorePlugin;
import net.thelounge.core.disguise.SQLReDisguiseObject;
import net.thelounge.core.disguise.api.DisguiseObject;

import java.util.ArrayList;
import java.util.UUID;

public class CoreUser {

	private final CorePlugin corePlugin;
	private final UUID uuid;

	private long userId;
	private String nameColor;
	private String userName;

	private ArrayList<PermissionGroup> permissionGroups;

	private PermissionGroup highest;

	private boolean muted = false;

	private boolean disguised = false;
	private DisguiseObject undisguiseObject = null;
	private SQLReDisguiseObject sqlReDisguiseObject = null;

	private boolean staffNotifications = true, privateMessages = true;

	public CoreUser(CorePlugin corePlugin, UUID uuid) {
		this.corePlugin = corePlugin;
		this.uuid = uuid;

		this.permissionGroups = new ArrayList<>();
		this.highest = corePlugin.getGroupManager().getGroup("default");
		addGroup(corePlugin.getGroupManager().getGroup("default"));
	}

	public ArrayList<PermissionGroup> getPermissionGroups() {
		return permissionGroups;
	}

	public void updatePrefix() {
		this.highest = corePlugin.getGroupManager().getGroup("default");
		for (PermissionGroup permissionGroup : permissionGroups) {
			if(permissionGroup.rankPriority() >= highest.rankPriority()) {
				highest = permissionGroup;
			}
		}
	}

	public void addGroup(PermissionGroup permissionGroup) {
		this.permissionGroups.add(permissionGroup);
		updatePrefix();
	}

	public void removeGroup(PermissionGroup permissionGroup) {
		this.permissionGroups.remove(permissionGroup);
		updatePrefix();
	}

	public boolean isMuted() {
		return muted;
	}
	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setNameColor(String nameColor) {
		this.nameColor = nameColor;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public long getUserId() {
		return userId;
	}

	public String getNameColor() {
		return nameColor;
	}

	public void setPrivateMessages(boolean privateMessages) {
		this.privateMessages = privateMessages;
	}

	public void setStaffNotifications(boolean staffNotifications) {
		this.staffNotifications = staffNotifications;
	}

	public boolean isPrivateMessages() {
		return privateMessages;
	}

	public boolean isStaffNotifications() {
		return staffNotifications;
	}

	public String getPrefix() {
		if(isDisguised()) return "§7";
		if(highest.getPrefix() == null) return "§7";
		return highest.getPrefix() + getNameColor();
	}

	public boolean isDisguised() {
		return disguised;
	}

	public DisguiseObject getUndisguiseObject() {
		return undisguiseObject;
	}

	public void setDisguised(DisguiseObject disguise) {
		this.undisguiseObject = disguise;
		this.disguised = true;
	}

	public void setUnDisguised() {
		this.disguised = false;
	}

	public SQLReDisguiseObject getSqlReDisguiseObject() {
		return sqlReDisguiseObject;
	}
	public void setSqlReDisguiseObject(SQLReDisguiseObject sqlReDisguiseObject) {
		this.sqlReDisguiseObject = sqlReDisguiseObject;
	}
}
