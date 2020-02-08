package net.thelounge.core.listeners;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.group.PermissionGroup;
import net.thelounge.core.user.CoreUser;
import net.thelounge.core.events.CoreUserRankUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CoreUserRankUpdateListener implements Listener {

	private final CorePlugin corePlugin;

	public CoreUserRankUpdateListener(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@EventHandler
	public void onPermissionUserRankUpdate(CoreUserRankUpdateEvent coreUserRankUpdateEvent) {

		if(coreUserRankUpdateEvent.getGroupAction() == CoreUserRankUpdateEvent.GroupAction.ADD) {

			if(!corePlugin.getGroupManager().groupExists(coreUserRankUpdateEvent.getGroup())) return;

			PermissionGroup permissionGroup = corePlugin.getGroupManager().getGroup(coreUserRankUpdateEvent.getGroup());

			if (Bukkit.getOfflinePlayer(coreUserRankUpdateEvent.getUuid()).isOnline()) {
				Player player = Bukkit.getPlayer(coreUserRankUpdateEvent.getUuid());
				CoreUser permissionUser = corePlugin.getUserManager().getUser(coreUserRankUpdateEvent.getUuid());
				permissionUser.addGroup(permissionGroup);
				corePlugin.getPermissionsHandler().addPermissions(player, permissionUser);
				player.sendMessage(corePlugin.getPrefix() + "§aYour permissions have been updated.");
				return;
			}
		}

		if(coreUserRankUpdateEvent.getGroupAction() == CoreUserRankUpdateEvent.GroupAction.REMOVE) {

			if(!corePlugin.getGroupManager().groupExists(coreUserRankUpdateEvent.getGroup())) return;

			PermissionGroup permissionGroup = corePlugin.getGroupManager().getGroup(coreUserRankUpdateEvent.getGroup());

			if (Bukkit.getOfflinePlayer(coreUserRankUpdateEvent.getUuid()).isOnline()) {
				Player player = Bukkit.getPlayer(coreUserRankUpdateEvent.getUuid());
				CoreUser permissionUser = corePlugin.getUserManager().getUser(coreUserRankUpdateEvent.getUuid());
				permissionUser.removeGroup(permissionGroup);
				corePlugin.getPermissionsHandler().addPermissions(player, permissionUser);
				player.sendMessage(corePlugin.getPrefix() + "§aYour permissions have been updated.");
				return;
			}
		}


	}
}
