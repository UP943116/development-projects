package net.thelounge.core.tasks;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.punishments.temp.PunishmentMute;
import net.thelounge.core.user.CoreUser;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CoreUserPunishmentTask extends BukkitRunnable {

	private final CorePlugin corePlugin;

	public CoreUserPunishmentTask(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;

		this.activeMutes = new ConcurrentHashMap<>();
	}

	private ConcurrentHashMap<CoreUser, PunishmentMute> activeMutes;

	@Override
	public void run() {

		for(Map.Entry<CoreUser, PunishmentMute> muteEntry : activeMutes.entrySet()) {
			if((corePlugin.getUtils().currentTime() / 1000) >= muteEntry.getValue().getUnmuteSeconds()) {
				muteEntry.getKey().setMuted(false);
				activeMutes.remove(muteEntry.getKey(), muteEntry.getValue());
			}
		}

	}

	public void putActiveMute(CoreUser coreUser, PunishmentMute punishmentMute) {

	}

	public void removeCachedUser(CoreUser coreUser) {
		activeMutes.remove(coreUser);
	}
}
