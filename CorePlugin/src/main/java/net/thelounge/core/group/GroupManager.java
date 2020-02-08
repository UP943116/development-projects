package net.thelounge.core.group;

import net.thelounge.core.group.groups.GroupDefault;
import net.thelounge.core.CorePlugin;
import net.thelounge.core.group.groups.Group;

import java.util.HashMap;

public class GroupManager {

	private final CorePlugin corePlugin;

	public GroupManager(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;

		this.permissionGroupHashMap = new HashMap<>();

		this.permissionGroupHashMap.put("default", new GroupDefault());

		corePlugin.log("Permission Groups Registered.");
	}

	private HashMap<String, PermissionGroup> permissionGroupHashMap;
	public HashMap<String, PermissionGroup> getPermissionGroupHashMap() {
		return permissionGroupHashMap;
	}

	public boolean groupExists(String groupName) {
		return permissionGroupHashMap.containsKey(groupName.toLowerCase());
	}

	public PermissionGroup getGroup(String groupName) {
		if(!groupExists(groupName.toLowerCase())) {
			return permissionGroupHashMap.get("default");
		}
		return permissionGroupHashMap.get(groupName.toLowerCase());
	}

	public void createGroup(String name, Group group) {
		this.permissionGroupHashMap.put(name, group);
		corePlugin.log("[PERMS] Rank (" + name + ") has been loaded.");
	}
}
