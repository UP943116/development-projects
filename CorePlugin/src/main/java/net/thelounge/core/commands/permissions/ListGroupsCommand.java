package net.thelounge.core.commands.permissions;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.group.GroupManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListGroupsCommand implements CommandExecutor {

	private final CorePlugin corePlugin;
	private GroupManager groupManager;

	public ListGroupsCommand(CorePlugin corePlugin, GroupManager groupManager) {
		this.corePlugin = corePlugin;
		this.groupManager = groupManager;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("listgroups")) {

			if (!commandSender.hasPermission("permissions.use")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			StringBuilder builder = new StringBuilder();

			for(String groups : groupManager.getPermissionGroupHashMap().keySet()) {
				if(!groups.equalsIgnoreCase("default")) {
					builder.append(groups).append(", ");
				}
			}
			commandSender.sendMessage(corePlugin.getPrefix() + "§aGroup List:");
			commandSender.sendMessage(corePlugin.getPrefix() + "§7" + builder.toString());
			return true;
		}



		return false;
	}
}
