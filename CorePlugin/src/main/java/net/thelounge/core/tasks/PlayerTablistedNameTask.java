package net.thelounge.core.tasks;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.CoreUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerTablistedNameTask extends BukkitRunnable {

	private final CorePlugin corePlugin;

	public PlayerTablistedNameTask(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	@Override
	public void run() {

		for(Player all : Bukkit.getServer().getOnlinePlayers()) {
			if(all.getScoreboard() != null) {
				Scoreboard scoreboard = all.getScoreboard();

				colorTab(scoreboard, all);
				for(Player all1 : Bukkit.getServer().getOnlinePlayers()) {
					colorTab(scoreboard, all1);
				}
			}
		}
	}

	private void colorTab(Scoreboard scoreboard, Player player) {
		CoreUser coreUser = corePlugin.getUserManager().getUser(player.getUniqueId());

		if(coreUser.isDisguised()) {
			if(scoreboard.getTeam("^I_user") == null) {
				scoreboard.registerNewTeam("^I_user").setPrefix("§7");
			}
			if(!scoreboard.getTeam("^I_user").hasPlayer(player)) {
				scoreboard.getTeam("^I_user").addPlayer(player);
			}
			return;
		}


		if(coreUser.getPermissionGroups().contains(corePlugin.getGroupManager().getGroup("owner"))) {
			String rank = "^AAAA_owner";
			if(scoreboard.getTeam(rank) == null) {
				scoreboard.registerNewTeam(rank).setPrefix("§4§l");
			}
			if(!scoreboard.getTeam(rank).hasPlayer(player)) {
				scoreboard.getTeam(rank).addPlayer(player);
			}
			return;
		}

		if(coreUser.getPermissionGroups().contains(corePlugin.getGroupManager().getGroup("developer"))) {
			String rank = "^AAA_developer";
			if(scoreboard.getTeam(rank) == null) {
				scoreboard.registerNewTeam(rank).setPrefix("§4");
			}
			if(!scoreboard.getTeam(rank).hasPlayer(player)) {
				scoreboard.getTeam(rank).addPlayer(player);
			}
			return;
		}

		if(coreUser.getPermissionGroups().contains(corePlugin.getGroupManager().getGroup("admin"))) {
			String rank = "^A_admin";
			if(scoreboard.getTeam(rank) == null) {
				scoreboard.registerNewTeam(rank).setPrefix("§c");
			}
			if(!scoreboard.getTeam(rank).hasPlayer(player)) {
				scoreboard.getTeam(rank).addPlayer(player);
			}
			return;
		}

		if(coreUser.getPermissionGroups().contains(corePlugin.getGroupManager().getGroup("senior-mod"))) {
			String rank = "^BB_senior";
			if(scoreboard.getTeam(rank) == null) {
				scoreboard.registerNewTeam(rank).setPrefix("§9");
			}
			if(!scoreboard.getTeam(rank).hasPlayer(player)) {
				scoreboard.getTeam(rank).addPlayer(player);
			}
			return;
		}

		if(coreUser.getPermissionGroups().contains(corePlugin.getGroupManager().getGroup("modplus"))) {
			String rank = "^B_modplus";
			if(scoreboard.getTeam(rank) == null) {
				scoreboard.registerNewTeam(rank).setPrefix("§b");
			}
			if(!scoreboard.getTeam(rank).hasPlayer(player)) {
				scoreboard.getTeam(rank).addPlayer(player);
			}
			return;
		}

		if(coreUser.getPermissionGroups().contains(corePlugin.getGroupManager().getGroup("mod"))) {
			String rank = "^C_mod";
			if(scoreboard.getTeam(rank) == null) {
				scoreboard.registerNewTeam(rank).setPrefix("§3");
			}
			if(!scoreboard.getTeam(rank).hasPlayer(player)) {
				scoreboard.getTeam(rank).addPlayer(player);
			}
			return;
		}

		if(coreUser.getPermissionGroups().contains(corePlugin.getGroupManager().getGroup("trainee"))) {
			String rank = "^D_trial";
			if(scoreboard.getTeam(rank) == null) {
				scoreboard.registerNewTeam(rank).setPrefix("§e");
			}
			if(!scoreboard.getTeam(rank).hasPlayer(player)) {
				scoreboard.getTeam(rank).addPlayer(player);
			}
			return;
		}

		if(coreUser.getPermissionGroups().contains(corePlugin.getGroupManager().getGroup("partner"))) {
			String rank = "^E_partner";
			if(scoreboard.getTeam(rank) == null) {
				scoreboard.registerNewTeam(rank).setPrefix("§d");
			}
			if(!scoreboard.getTeam(rank).hasPlayer(player)) {
				scoreboard.getTeam(rank).addPlayer(player);
			}
			return;
		}

		if(coreUser.getPermissionGroups().contains(corePlugin.getGroupManager().getGroup("famous"))) {
			String rank = "^F_famous";
			if(scoreboard.getTeam(rank) == null) {
				scoreboard.registerNewTeam(rank).setPrefix("§b");
			}
			if(!scoreboard.getTeam(rank).hasPlayer(player)) {
				scoreboard.getTeam(rank).addPlayer(player);
			}
			return;
		}

		if(coreUser.getPermissionGroups().contains(corePlugin.getGroupManager().getGroup("media"))) {
			String rank = "^G_media";
			if(scoreboard.getTeam(rank) == null) {
				scoreboard.registerNewTeam(rank).setPrefix("§d");
			}
			if(!scoreboard.getTeam(rank).hasPlayer(player)) {
				scoreboard.getTeam(rank).addPlayer(player);
			}
			return;
		}

		if(coreUser.getPermissionGroups().contains(corePlugin.getGroupManager().getGroup("donator"))) {
			String rank = "^H_donator";
			if(scoreboard.getTeam(rank) == null) {
				scoreboard.registerNewTeam(rank).setPrefix("§6");
			}
			if(!scoreboard.getTeam(rank).hasPlayer(player)) {
				scoreboard.getTeam(rank).addPlayer(player);
			}
			return;
		}

		if(scoreboard.getTeam("^I_user") == null) {
			scoreboard.registerNewTeam("^I_user").setPrefix("§7");
		}
		if(!scoreboard.getTeam("^I_user").hasPlayer(player)) {
			scoreboard.getTeam("^I_user").addPlayer(player);
		}
	}
}
