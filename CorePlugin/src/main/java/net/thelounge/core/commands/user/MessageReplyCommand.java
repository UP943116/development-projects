package net.thelounge.core.commands.user;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.CoreUser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MessageReplyCommand implements CommandExecutor {

	private final CorePlugin corePlugin;
	private HashMap<Player, Player> playerPlayerHashMap;

	public MessageReplyCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;

		this.playerPlayerHashMap = new HashMap<>();
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("message")) {

			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage("§4You must be a player to execute this command.");
				return true;
			}


			if (strings.length < 2) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§cUsage: /message <player> <message>");
				return true;
			}

			Player sendTo = Bukkit.getPlayer(strings[0]);

			if(sendTo == null) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§cThis player is not online.");
				return true;
			}

			CoreUser sendToUser = corePlugin.getUserManager().getUser(sendTo.getUniqueId());

			if(!sendToUser.isPrivateMessages() && !commandSender.hasPermission("perms.staff")) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§cThis player has there private messages disabled.");
				return true;
			}

			Player sentFrom = (Player) commandSender;

			StringBuilder stringBuilder = new StringBuilder();

			for (int i = 1; i < strings.length; i++) {
				stringBuilder.append(strings[i]).append(" ");
			}

			CoreUser coreUser = corePlugin.getUserManager().getUser(((Player) commandSender).getUniqueId());

			if(!coreUser.isPrivateMessages()) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§4[MSG] §cYou have private messages disabled. re-enable them to send messages.");
				return true;
			}

			playerPlayerHashMap.put(sendTo, sentFrom);
			playerPlayerHashMap.put(sentFrom, sendTo);

			sendTo.sendMessage("§7(From "+coreUser.getPrefix() + sentFrom.getName()+ "§7) §b" + stringBuilder.toString());
			sentFrom.sendMessage("§7(To "+sendToUser.getPrefix() + sendTo.getName()+ "§7) §b" + stringBuilder.toString());

			return true;
		}

		if(command.getName().equalsIgnoreCase("reply")) {

			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage("§4You must be a player to execute this command.");
				return true;
			}


			if (strings.length < 1) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§cUsage: /reply <message>");
				return true;
			}
			Player sentFrom = (Player) commandSender;
			Player sendTo = playerPlayerHashMap.get(sentFrom);

			if(sendTo == null) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§cThis player is not online.");
				return true;
			}

			CoreUser sendToUser = corePlugin.getUserManager().getUser(sendTo.getUniqueId());

			if(!sendToUser.isPrivateMessages() && !commandSender.hasPermission("perms.staff")) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§cThis player has there private messages disabled.");
				return true;
			}



			StringBuilder stringBuilder = new StringBuilder();

			for (int i = 0; i < strings.length; i++) {
				stringBuilder.append(strings[i]).append(" ");
			}

			CoreUser coreUser = corePlugin.getUserManager().getUser(((Player) commandSender).getUniqueId());

			if(!coreUser.isPrivateMessages() && !sentFrom.hasPermission("perms.staff")) {
				commandSender.sendMessage("§4[MSG] §cYou have private messages disabled. re-enable them to send messages.");
				return true;
			}

			playerPlayerHashMap.put(sendTo, sentFrom);
			playerPlayerHashMap.put(sentFrom, sendTo);

			sendTo.sendMessage("§7(From "+ coreUser.getPrefix() + sentFrom.getName()+ "§7) §b" + stringBuilder.toString());
			sentFrom.sendMessage("§7(To "+ sendToUser.getPrefix() + sendTo.getName()+ "§7) §b" + stringBuilder.toString());

			return true;
		}
		return false;
	}
}
