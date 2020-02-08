package net.thelounge.core.group.groups;

import net.thelounge.core.group.PermissionGroup;
import org.bukkit.ChatColor;

import java.util.HashMap;

public class Group implements PermissionGroup {

	private int rankId, rankPriority;
	private HashMap<String, Boolean> stringBooleanHashMap;
	private String rankName, rankPrefix;

	public Group(int rankId, HashMap<String, Boolean> stringBooleanHashMap, String rankName, String rankPrefix, int rankPriority) {
		this.rankId = rankId;
		this.stringBooleanHashMap = stringBooleanHashMap;
		this.rankName = rankName;
		this.rankPrefix = rankPrefix.replace("(star)", "✦");

		this.rankPriority = rankPriority;
	}

	@Override
	public int getRankId() {
		return rankId;
	}

	@Override
	public HashMap<String, Boolean> permissionsHashMap() {
		return stringBooleanHashMap;
	}

	@Override
	public String getName() {
		return rankName;
	}

	@Override
	public String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', rankPrefix);
	}

	@Override
	public int rankPriority() {
		return rankPriority;
	}
}
