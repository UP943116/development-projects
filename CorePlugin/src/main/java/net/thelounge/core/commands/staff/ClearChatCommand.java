package net.thelounge.core.commands.staff;


import net.thelounge.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public ClearChatCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("chatclear")) {

			if(!commandSender.hasPermission("perms.staff")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			for(Player all : Bukkit.getServer().getOnlinePlayers()) {
				if(!all.hasPermission("perms.staff")) {
					for(int i = 0; i < 58; i++) {
						all.sendMessage("");
					}
					all.sendMessage("§eChat has been cleared by a §6Staff Member§e!");
				} else {
					all.sendMessage("§eChat has been cleared by a §6Staff Member§7! §7(Your chat has not been cleared as you are staff!)");
				}
			}

			return true;
		}

		return false;
	}

}