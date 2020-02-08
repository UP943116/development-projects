package net.thelounge.core.commands.staff;


import net.thelounge.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MuteChatCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public MuteChatCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("globalmute")) {

			if(!commandSender.hasPermission("perms.staff")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			if(corePlugin.isChatMuted()) {
				Bukkit.broadcastMessage(corePlugin.getPrefix() + "§eGlobal Chat has been§a enabled.");
				corePlugin.setChatMuted(false);
			} else {
				Bukkit.broadcastMessage(corePlugin.getPrefix() + "§eGlobal Chat has been§c disabled.");
				corePlugin.setChatMuted(true);
			}

			return true;
		}

		return false;
	}

}