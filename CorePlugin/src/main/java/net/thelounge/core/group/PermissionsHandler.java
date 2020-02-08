package net.thelounge.core.group;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.CoreUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Map;

public class PermissionsHandler {

	private final CorePlugin corePlugin;

	public PermissionsHandler(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	public void addPermissions(Player player, CoreUser coreUser) {
		removePermissions(coreUser);

		PermissionAttachment permissionAttachment = player.addAttachment(corePlugin);

		for(PermissionGroup permissionGroup : coreUser.getPermissionGroups()) {
			for(Map.Entry<String, Boolean> stringBooleanHashMap : permissionGroup.permissionsHashMap().entrySet()) {
				permissionAttachment.setPermission(stringBooleanHashMap.getKey(), stringBooleanHashMap.getValue());
			}
			permissionAttachment.setPermission("perms." + permissionGroup.getName(), true);
			permissionAttachment.setPermission("venix." + permissionGroup.getName(), true);
		}

	}

	public void removePermissions(CoreUser coreUser) {
		Player player = Bukkit.getPlayer(coreUser.getUuid());

		if(player == null) return;

			for (PermissionAttachmentInfo permissionInfo : player.getEffectivePermissions()) {
				PermissionAttachment attachment = permissionInfo.getAttachment();
				if (attachment != null) {
					Map<String, Boolean> flags = attachment.getPermissions();
					for (String permissions : flags.keySet()) {
						attachment.unsetPermission(permissions);
					}
				}
			player.getEffectivePermissions().clear();
		}

	}
}
