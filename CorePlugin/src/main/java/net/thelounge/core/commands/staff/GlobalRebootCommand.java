package net.thelounge.core.commands.staff;


import net.thelounge.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlobalRebootCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public GlobalRebootCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("globalreboot")) {

			if(commandSender instanceof Player) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cThis command can only be executed by console.");
				return true;
			}

			corePlugin.getRedisManager().sendReboot();

			return true;
		}

		return false;
	}

}