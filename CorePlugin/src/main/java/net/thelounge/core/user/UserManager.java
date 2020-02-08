package net.thelounge.core.user;

import net.thelounge.core.CorePlugin;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {

	private final CorePlugin corePlugin;

	private HashMap<UUID, CoreUser> coreUserHashMap;

	public UserManager(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;

		this.coreUserHashMap = new HashMap<>();
	}

	public CoreUser getUser(UUID uuid) {
		return coreUserHashMap.computeIfAbsent(uuid, user -> new CoreUser(corePlugin, uuid));
	}

	public boolean userExists(UUID uuid) {
		return coreUserHashMap.containsKey(uuid);
	}

	public void removeUser(UUID uuid) {
		if(userExists(uuid)) {
			CoreUser coreUser = getUser(uuid);

			corePlugin.getSqlManager().saveUser(coreUser);

			coreUserHashMap.remove(uuid);
		}
	}
}
