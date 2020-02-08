package net.thelounge.core.group.groups;

import net.thelounge.core.group.PermissionGroup;

import java.util.HashMap;

public class GroupDefault implements PermissionGroup {

	@Override
	public int getRankId() {
		return 1;
	}

	@Override
	public HashMap<String, Boolean> permissionsHashMap() {

		HashMap<String, Boolean> permissions = new HashMap<>();

		permissions.put("bukkit.command.me", false);
		permissions.put("bukkit.command.about", false);
		permissions.put("bukkit.command.plugins", false);
		permissions.put("bukkit.command.version", false);
		permissions.put("bukkit.command.kill", false);
		permissions.put("bukkit.command.achievement", false);
		permissions.put("bukkit.command.help", false);
		permissions.put("minecraft.command.kill", false);
		permissions.put("minecraft.command.me", false);
		permissions.put("minecraft.command.version", false);
		permissions.put("bukkit.*", false);
		permissions.put("minecraft.*", false);
		permissions.put("worldedit.calculate", false);
		permissions.put("worldedit.calc", false);

		return permissions;
	}

	@Override
	public String getName() {
		return "default";
	}

	@Override
	public String getPrefix() {
		return "§7";
	}

	@Override
	public int rankPriority() {
		return 1;
	}
}
