package net.thelounge.core.commands.staff;


import net.thelounge.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AnnounceUHCCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public AnnounceUHCCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("announceuhc")) {

			if(!commandSender.hasPermission("perms.host")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			if(corePlugin.getServerName().toUpperCase().contains("UHC-")) {
				corePlugin.getRedisManager().sendAnnouncement("&eThe Whitelist for §6"+ corePlugin.getServerName() + "§e is now off!");
				return true;
			} else {
				commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cYou are not on a UHC Server.");
			}
			return true;
		}

		return false;
	}

}
