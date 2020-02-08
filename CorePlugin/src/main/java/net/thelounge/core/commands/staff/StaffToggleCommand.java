package net.thelounge.core.commands.staff;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.CoreUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffToggleCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public StaffToggleCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;

	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("staffnotifications")) {

			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage("§cYou do not have permission to do this. (CONSOLE REALLY)");
				return true;
			}

			if(!commandSender.hasPermission("perms.staff")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			Player player = (Player)commandSender;
			CoreUser coreUser = corePlugin.getUserManager().getUser(player.getUniqueId());

			if(coreUser.isStaffNotifications()) {
				player.sendMessage(corePlugin.getPrefix() + "§4[STAFF] §eStaff Notifications§7: §cDisabled.");
				coreUser.setStaffNotifications(false);
			} else {
				player.sendMessage(corePlugin.getPrefix() + "§4[STAFF] §eStaff Notifications§7: §aEnabled.");
				coreUser.setStaffNotifications(true);
			}

			return true;
		}
		return false;
	}

}
