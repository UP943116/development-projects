package net.thelounge.core.commands.user;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.CoreUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PmToggleCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public PmToggleCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;

	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("pmtoggle")) {

			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage("§cYou do not have permission to do this. (CONSOLE REALLY)");
				return true;
			}

			Player player = (Player)commandSender;
			CoreUser coreUser = corePlugin.getUserManager().getUser(player.getUniqueId());

			if(coreUser.isPrivateMessages()) {
				player.sendMessage(corePlugin.getPrefix() + "§fPrivate Messages§7: §cDisabled.");
				coreUser.setPrivateMessages(false);
			} else {
				player.sendMessage(corePlugin.getPrefix() + "§fPrivate Messages§7: §aEnabled.");
				coreUser.setPrivateMessages(true);
			}

			return true;
		}
		return false;
	}

}
