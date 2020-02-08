package net.thelounge.core.commands.user;


import net.thelounge.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

	private final CorePlugin corePlugin;

	public ReportCommand(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

		if(command.getName().equalsIgnoreCase("report")) {

			if(!(commandSender instanceof Player)) {
				commandSender.sendMessage("§cYou do not have permission to do this. (CONSOLE REALLY)");
				return true;
			}

			if(strings.length < 2) {
				commandSender.sendMessage("§cUsage: /report <user> <reason>");
				commandSender.sendMessage("§cReasons: SPEED, XRAY, KILLAURA, FLYING, CHAT, STALKING, ANTIKB, TEAMING, CAMPING, OTHER");
				return true;
			}

			if(corePlugin.getPlayerCooldownTask().hasReportCooldown(corePlugin.getUserManager().getUser(((Player) commandSender).getUniqueId()))) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§cYou are currently on cooldown for reports.");
				return true;
			}

			Player playerReported = Bukkit.getPlayer(strings[0]);

			if(playerReported == null) {
				commandSender.sendMessage(corePlugin.getPrefix() + "§cError: This player is not online.");
				return true;
			}



			for(ReportReasons reportReasons : ReportReasons.values()) {
				if(reportReasons.name().equalsIgnoreCase(strings[1])) {
					commandSender.sendMessage(corePlugin.getPrefix() + "§aYour report has been submitted, Thank you!");
					corePlugin.getRedisManager().sendReport(commandSender.getName(), playerReported.getName(), reportReasons.name());
					return true;
				}
			}

			if(!commandSender.hasPermission("perms.staff")) {
				corePlugin.getPlayerCooldownTask().addReportCooldown(corePlugin.getUserManager().getUser(((Player) commandSender).getUniqueId()));
			}

			commandSender.sendMessage(corePlugin.getPrefix() + "§aYour report has been submitted, we could not determine the reason, but we still notified our staff.");
			corePlugin.getRedisManager().sendReport(commandSender.getName(), playerReported.getName(), ReportReasons.OTHER.name());

			return true;
		}

		return false;
	}

	enum ReportReasons {

		SPEED,
		XRAY,
		KILLAURA,
		FLYING,
		CHAT,
		STALKING,
		OTHER,
		ANTIKB,
		TEAMING,
		CAMPING

	}

}