package net.thelounge.core.commands.permissions;

import net.thelounge.core.group.PermissionGroup;
import net.thelounge.core.CorePlugin;
import net.thelounge.core.group.GroupManager;
import net.thelounge.core.user.TempInfoStoreUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PermissionsCommand implements CommandExecutor {

	private final CorePlugin corePlugin;
	private GroupManager groupManager;

	public PermissionsCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
		this.groupManager = corePlugin.getGroupManager();
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("permissions")) {

			if(!commandSender.hasPermission("permissions.use")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			if(strings.length < 2) {
				commandSender.sendMessage(corePlugin.getPrefix() + "/permissions <user> add <rank>");
				commandSender.sendMessage(corePlugin.getPrefix() + "/permissions <user> remove <rank>");
				commandSender.sendMessage(corePlugin.getPrefix() + "/permissions <user> clear");
				commandSender.sendMessage(corePlugin.getPrefix() + "/permissions <user> listgroups");
				return true;
			}

			if(strings[1].equalsIgnoreCase("listgroups")) {
				//todo list groups
				return true;
			}

			if(strings[1].equalsIgnoreCase("add") && strings.length == 3) {

				TempInfoStoreUser tempInfoStoreUser = corePlugin.getSqlManager().getUserFromName(strings[0]);

				if(tempInfoStoreUser == null) {
					commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cA Player by this name has never connected to the network!");
					return true;
				}


				if(strings[2].equalsIgnoreCase("default")) {
					commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cYou cannot set a player to the default rank as they already have this.");
					return true;
				}

				if(!groupManager.groupExists(strings[2].toLowerCase())) {
					commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cThis rank cannot be found on the server.");
					return true;
				}

				PermissionGroup permissionGroup = groupManager.getGroup(strings[2].toLowerCase());

				corePlugin.getSqlManager().addGroup(tempInfoStoreUser, permissionGroup);
				commandSender.sendMessage(corePlugin.getPrefix() + "§aPlayers permissions have been updated.");

				return true;
			}

			if(strings[1].equalsIgnoreCase("remove") && strings.length == 3) {

				TempInfoStoreUser tempInfoStoreUser = corePlugin.getSqlManager().getUserFromName(strings[0]);

				if(tempInfoStoreUser == null) {
					commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cA Player by this name has never connected to the network!");
					return true;
				}


				if(strings[2].equalsIgnoreCase("default")) {
					commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cYou cannot remove a player from the default rank as they already have this.");
					return true;
				}

				if(!groupManager.groupExists(strings[2].toLowerCase())) {
					commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cThis rank cannot be found on the server.");
					return true;
				}

				PermissionGroup permissionGroup = groupManager.getGroup(strings[2].toLowerCase());

				corePlugin.getSqlManager().removeGroup(tempInfoStoreUser, permissionGroup);
				commandSender.sendMessage(corePlugin.getPrefix() + "§aPlayers permissions have been updated.");

				return true;
			}


			return true;
		}
		return false;
	}
}
