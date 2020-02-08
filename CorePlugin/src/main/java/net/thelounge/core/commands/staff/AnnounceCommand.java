package net.thelounge.core.commands.staff;


import net.thelounge.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AnnounceCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public AnnounceCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("announce")) {

			if(!commandSender.hasPermission("announce.use")) {
				commandSender.sendMessage(corePlugin.getNoPerms());
				return true;
			}

			if(strings.length > 0) {
				String message = strJoin(strings, " ");
				corePlugin.getRedisManager().sendAnnouncement(message);
				return true;
			} else {
				commandSender.sendMessage("§c§lAnnounce §8\u00bb §cUsage: /announce <message>");
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