package net.thelounge.core.commands.user;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.events.CoreUserNameColorChangeEvent;
import net.thelounge.core.user.CoreUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ColorCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public ColorCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (command.getName().equalsIgnoreCase("color")) {

			if(!(commandSender instanceof Player)) return true;

			if (!commandSender.hasPermission("perms.color")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			if (strings.length < 1) {
				commandSender.sendMessage(ChatColor.RED + "/color <color>");
				commandSender.sendMessage(corePlugin.getPrefix() + "§aAvaliable Colors: §cRED, §5DARK_PURPLE, §9BLUE, §dLIGHT_PURPLE, §eYELLOW, §7GRAY, §8DARK_GRAY, §bAQUA, §3DARK_AQUA, §aGREEN, §6GOLD, §2DARK_GREEN.");
				return true;
			}

			if ((!strings[0].equalsIgnoreCase("red")) &&
					(!strings[0].equalsIgnoreCase("dark_purple")) &&
					(!strings[0].equalsIgnoreCase("blue")) &&
					(!strings[0].equalsIgnoreCase("light_purple")) &&
					(!strings[0].equalsIgnoreCase("yellow")) &&
					(!strings[0].equalsIgnoreCase("gray")) &&
					(!strings[0].equalsIgnoreCase("dark_gray")) &&
					(!strings[0].equalsIgnoreCase("aqua")) &&
					(!strings[0].equalsIgnoreCase("dark_aqua")) &&
					(!strings[0].equalsIgnoreCase("green")) &&
					(!strings[0].equalsIgnoreCase("gold")) &&
					(!strings[0].equalsIgnoreCase("dark_green"))) {

				commandSender.sendMessage(ChatColor.RED + "This Color does not exist!");
				commandSender.sendMessage(corePlugin.getPrefix() + "§aAvaliable Colors: §cRED, §5DARK_PURPLE, §9BLUE, §dLIGHT_PURPLE, §eYELLOW, §7GRAY, §8DARK_GRAY, §bAQUA, §3DARK_AQUA, §aGREEN, §6GOLD, §2DARK_GREEN.");
				return true;
			}

			ChatColor color = ChatColor.valueOf(strings[0].toUpperCase());
			Player player = (Player)commandSender;
			CoreUser coreUser = corePlugin.getUserManager().getUser(player.getUniqueId());

			coreUser.setNameColor(color.toString());
			corePlugin.getSqlManager().changeUserColor(coreUser);
			Bukkit.getServer().getPluginManager().callEvent(new CoreUserNameColorChangeEvent(coreUser, color));

			commandSender.sendMessage(corePlugin.getPrefix() + "§bSuccessfully changed name colour to: " + color + commandSender.getName());
			return true;
		}
		return true;

	}
}

