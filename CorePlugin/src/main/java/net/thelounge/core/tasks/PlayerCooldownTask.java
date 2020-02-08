package net.thelounge.core.tasks;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.CoreUser;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCooldownTask extends BukkitRunnable {

	private final CorePlugin corePlugin;

	public PlayerCooldownTask(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;

		this.chatCooldown = new ConcurrentHashMap<>();
		this.reportCooldown = new ConcurrentHashMap<>();
	}

	@Override
	public void run() {

		if(chatCooldown.size() > 0) {
			for (Map.Entry<CoreUser, Integer> chats : chatCooldown.entrySet()) {
				if (chats.getValue() > 0) {
					chats.setValue(chats.getValue() - 1);
				} else {
					chatCooldown.remove(chats.getKey());
				}
			}
		}

		if(reportCooldown.size() > 0) {
			for (Map.Entry<CoreUser, Integer> reports : reportCooldown.entrySet()) {
				if (reports.getValue() > 0) {
					reports.setValue(reports.getValue() - 1);
				} else {
					reportCooldown.remove(reports.getKey());
				}
			}
		}

	}

	private ConcurrentHashMap<CoreUser, Integer> chatCooldown;
	private ConcurrentHashMap<CoreUser, Integer> reportCooldown;

	public void addChatCooldown(CoreUser coreUser) {
		chatCooldown.put(coreUser, 3);
	}

	public void addReportCooldown(CoreUser coreUser) {
		reportCooldown.put(coreUser, 20);
	}

	public boolean hasReportCooldown(CoreUser coreUser) {
		return reportCooldown.containsKey(coreUser);
	}

	public boolean hasChatCooldown(CoreUser coreUser) {
		return chatCooldown.containsKey(coreUser);
	}
}
