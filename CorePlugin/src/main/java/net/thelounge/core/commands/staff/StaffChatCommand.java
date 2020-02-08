package net.thelounge.core.commands.staff;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.CoreUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public StaffChatCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("staffchat")) {

			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage("§4You must be a player to execute this command.");
				return true;
			}

			if (!commandSender.hasPermission("perms.staff")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			if (strings.length < 1) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§cUsage: /staffchat <message>");
				return true;
			}

			StringBuilder stringBuilder = new StringBuilder();

			for (int i = 0; i < strings.length; i++) {
				stringBuilder.append(strings[i]).append(" ");
			}

			CoreUser coreUser = corePlugin.getUserManager().getUser(((Player) commandSender).getUniqueId());

			if(!coreUser.isStaffNotifications()) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§4[STAFF] §cYou have staff notifications disabled. re-enable them to send messages.");
				return true;
			}

			corePlugin.getRedisManager().sendStaffChat(coreUser.getUserName(), stringBuilder.toString());

			return true;
		}
		return false;
	}
}
