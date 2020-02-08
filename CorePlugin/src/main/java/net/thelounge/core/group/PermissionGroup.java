package net.thelounge.core.group;

import java.util.HashMap;

public interface PermissionGroup {

	int getRankId();

	HashMap<String, Boolean> permissionsHashMap();

	String getName();

	String getPrefix();

	int rankPriority();

}
