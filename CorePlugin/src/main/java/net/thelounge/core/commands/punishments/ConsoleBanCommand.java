package net.thelounge.core.commands.punishments;


import net.thelounge.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConsoleBanCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public ConsoleBanCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("consoleban")) {

			if(commandSender instanceof Player) return true;

			if(strings.length > 0) {

				return true;
			} else {
				commandSender.sendMessage(corePlugin.getPrefix() + "§cUsage: /consoleban <username>");
				return true;
			}
		}

		return false;
	}

	private String strJoin(String[] aArr, String sSep) {
		StringBuilder sbStr = new StringBuilder();
		for (int i = 0, il = aArr.length; i < il; i++) {
			if (i > 0)
				sbStr.append(sSep);
			sbStr.append(aArr[i]);
		}
		return sbStr.toString();
	}
}