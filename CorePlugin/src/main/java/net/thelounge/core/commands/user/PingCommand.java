package net.thelounge.core.commands.user;

import net.thelounge.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public PingCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("ping")) {

			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage("Console you are a fucking retard.");
				return true;
			}

			CraftPlayer craftPlayer = (CraftPlayer) commandSender;

			commandSender.sendMessage(corePlugin.getPrefix() + "§eYour Ping§7: §6" + craftPlayer.getHandle().ping + "§ems.");
			return true;
		}

		return false;
	}
}
