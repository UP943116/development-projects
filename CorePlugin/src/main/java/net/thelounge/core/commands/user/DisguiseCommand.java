package net.thelounge.core.commands.user;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.CoreUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DisguiseCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public DisguiseCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("disguise")) {

			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage("§4Error: §cYou must be a player to execute this command.");
				return true;
			}

			if(!commandSender.hasPermission("perms.disguise")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			Player player = (Player) commandSender;
			CoreUser coreUser = corePlugin.getUserManager().getUser(player.getUniqueId());

			if(!corePlugin.getServerName().startsWith("HUB-") && !player.isOp()) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cYou are only able to use this in the HUB/LOBBY Server.");
				return true;
			}

			if(coreUser.isDisguised()) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cYou are already disguised. §fType /undisguise to undisguise.");
				return true;
			}

			if(strings.length == 1 && commandSender.hasPermission("perms.disguise.custom")) {
				if(!corePlugin.getDisguiseManager().canUseDisguiseName(strings[0])) {
					commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cYou cannot use this name!");
					return true;
				}

				if(strings[0].length() > 16) {
					commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cYou cannot use this name!");
					return true;
				}
				corePlugin.getDisguiseManager().disguisePlayer(player, strings[0]);
				return true;
			}

			corePlugin.getDisguiseManager().disguisePlayer(player, corePlugin.getDisguiseManager().getRandomName());

			return true;
		}

		if(command.getName().equalsIgnoreCase("undisguise")) {

			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cYou must be a player to execute this command.");
				return true;
			}

			if(!commandSender.hasPermission("perms.disguise")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			Player player = (Player) commandSender;
			CoreUser coreUser = corePlugin.getUserManager().getUser(player.getUniqueId());

			if(!corePlugin.getServerName().startsWith("HUB-") && !player.isOp()) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cYou are only able to use this in the HUB/LOBBY Server.");
				return true;
			}

			if(!coreUser.isDisguised()) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§4Error: §cYou are not disguised. §fType /disguise to disguise.");
				return true;
			}

			corePlugin.getDisguiseManager().undisguisePlayer(player);
			return true;
		}

		if(command.getName().equalsIgnoreCase("addskin")) {
			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage("§4Error: §cYou must be a player to execute this command.");
				return true;
			}

			if(!commandSender.hasPermission("perms.admin")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			if(strings.length == 1) {
				if(strings[0].length() != 36) {
					commandSender.sendMessage(corePlugin.getPrefix() + "§cWe need the 36 char long uuid.");
					return true;
				}
				UUID uuid = UUID.fromString(strings[0]);

				corePlugin.getDisguiseManager().createSkin(uuid.toString());
				commandSender.sendMessage(corePlugin.getPrefix() + "§aCreating a skin, this will be available on restart!");

				return true;
			} else {
				commandSender.sendMessage(corePlugin.getPrefix() + "§cUsage: /addskin <uuid>");
				return true;
			}


		}

		return false;
	}

}
