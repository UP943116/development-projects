package net.thelounge.core.commands.user;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.CoreUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class DiscordLinkCommand implements CommandExecutor {

	private final CorePlugin corePlugin;
	private final Random random;

	public DiscordLinkCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;

		this.random = new Random();
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("discord")) {

			if(!(commandSender instanceof Player)) return true;

			Player player = (Player)commandSender;
			CoreUser coreUser = corePlugin.getUserManager().getUser(player.getUniqueId());

			if(!corePlugin.getSqlManager().isDiscordLinked(coreUser)) {
				String code = corePlugin.getSqlManager().getDiscordCode(coreUser);
				if(code != null) {
					if(code.equalsIgnoreCase("0")) {
						player.sendMessage(corePlugin.getPrefix() + "§aYour Minecraft Account is already linked to Discord.");
						return true;
					}
					player.sendMessage(corePlugin.getPrefix() + "§bIn any channel on discord use§f !link " + code);
					return true;
				}


				code = getRandomCode();
				corePlugin.getSqlManager().insertDiscordCode(coreUser, code);
				player.sendMessage(corePlugin.getPrefix() + "§bIn any channel on discord use§f !link " + code);

				return true;
			} else {
				player.sendMessage(corePlugin.getPrefix() + "§cYour discord is already linked.");
			}

			return true;
		}
		return false;
	}

	private String getRandomCode() {
		int count = 6 + random.nextInt(10);
		final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
}
