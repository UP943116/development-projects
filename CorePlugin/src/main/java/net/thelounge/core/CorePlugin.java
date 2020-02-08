package net.thelounge.core;

import net.thelounge.core.commands.permissions.PermissionsCommand;
import net.thelounge.core.commands.staff.AnnounceUHCCommand;
import net.thelounge.core.commands.staff.ClearChatCommand;
import net.thelounge.core.commands.staff.GlobalRebootCommand;
import net.thelounge.core.commands.staff.MuteChatCommand;
import net.thelounge.core.commands.staff.StaffToggleCommand;
import net.thelounge.core.commands.user.MessageReplyCommand;
import net.thelounge.core.commands.user.ReportCommand;
import net.thelounge.core.group.GroupManager;
import net.thelounge.core.listeners.PlayerJoinListener;
import net.thelounge.core.listeners.PlayerQuitListener;
import net.thelounge.core.listeners.ReportUserListener;
import net.thelounge.core.tasks.PlayerTablistedNameTask;
import net.thelounge.core.commands.permissions.ListGroupsCommand;
import net.thelounge.core.commands.staff.AdminChatCommand;
import net.thelounge.core.commands.staff.AnnounceCommand;
import net.thelounge.core.commands.staff.StaffChatCommand;
import net.thelounge.core.commands.user.ColorCommand;
import net.thelounge.core.commands.user.DiscordLinkCommand;
import net.thelounge.core.commands.user.DisguiseCommand;
import net.thelounge.core.commands.user.PmToggleCommand;
import net.thelounge.core.disguise.DisguiseManager;
import net.thelounge.core.group.PermissionsHandler;
import net.thelounge.core.listeners.CoreUserRankUpdateListener;
import net.thelounge.core.listeners.PlayerChatListener;
import net.thelounge.core.listeners.StaffChatListener;
import net.thelounge.core.redis.RedisManager;
import net.thelounge.core.sql.SQLManager;
import net.thelounge.core.tasks.PlayerCooldownTask;
import net.thelounge.core.user.UserManager;
import net.thelounge.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CorePlugin extends JavaPlugin {

	private String serverName = "NOT_SET";

	@Override
	public void onEnable() {
		log("Plugin is attempting to enable.");

		if(!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
			log("[ERROR] Plugin failed to load as there was no config setup.");
			log("[ERROR] We have created a default config file for you.");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		this.utils = new Utils(this);

		this.groupManager = new GroupManager(this);
		this.userManager = new UserManager(this);
		this.disguiseManager = new DisguiseManager(this);

		this.sqlManager = new SQLManager(this, getConfig());
		this.redisManager = new RedisManager(this);

		this.permissionsHandler = new PermissionsHandler(this);

		this.playerCooldownTask = new PlayerCooldownTask(this);
		this.playerCooldownTask.runTaskTimerAsynchronously(this, 20, 20);

		register(new PlayerJoinListener(this));
		register(new StaffChatListener(this));
		register(new ReportUserListener(this));
		register(new PlayerChatListener(this));
		register(new CoreUserRankUpdateListener(this));
		register(new PlayerQuitListener(this));

		MessageReplyCommand messageReplyCommand = new MessageReplyCommand(this);
		DisguiseCommand disguiseCommand = new DisguiseCommand(this);

		register("staffchat", new StaffChatCommand(this));
		register("announce", new AnnounceCommand(this));
		register("announceuhc", new AnnounceUHCCommand(this));
		register("message", messageReplyCommand);
		register("reply", messageReplyCommand);
		register("disguise", disguiseCommand);
		register("undisguise", disguiseCommand);
		register("addskin", disguiseCommand);
		register("chatclear", new ClearChatCommand(this));
		register("report", new ReportCommand(this));
		register("adminchat", new AdminChatCommand(this));
		register("globalmute", new MuteChatCommand(this));
		register("listgroups",  new ListGroupsCommand(this, groupManager));
		register("permissions", new PermissionsCommand(this));
		register("globalreboot", new GlobalRebootCommand(this));
		register("color", new ColorCommand(this));
		register("discord", new DiscordLinkCommand(this));
		register("pmtoggle", new PmToggleCommand(this));
		register("staffnotifications", new StaffToggleCommand(this));

		this.redisManager.onEnable();

		this.playerTablistedNameTask = new PlayerTablistedNameTask(this);
		this.playerTablistedNameTask.runTaskTimer(this, 20, 20);

	}

	@Override
	public void onDisable() {
		if(sqlManager != null) this.sqlManager.close();
		if(redisManager != null) this.redisManager.onDisable();
	}

	private PlayerTablistedNameTask playerTablistedNameTask;
	private PlayerCooldownTask playerCooldownTask;
	public PlayerCooldownTask getPlayerCooldownTask() {
		return playerCooldownTask;
	}

	public void log(String message) {
		Bukkit.getLogger().info("[CorePlugin] " + message);
	}

	private GroupManager groupManager;
	public GroupManager getGroupManager() {
		return groupManager;
	}

	private RedisManager redisManager;
	public RedisManager getRedisManager() {
		return redisManager;
	}

	private SQLManager sqlManager;
	public SQLManager getSqlManager() {
		return sqlManager;
	}

	private UserManager userManager;
	public UserManager getUserManager() {
		return userManager;
	}

	private DisguiseManager disguiseManager;
	public DisguiseManager getDisguiseManager() {
		return disguiseManager;
	}

	private PermissionsHandler permissionsHandler;
	public PermissionsHandler getPermissionsHandler() {
		return permissionsHandler;
	}

	private Utils utils;
	public Utils getUtils() {
		return utils;
	}

	private void register(Listener listener) {
		Bukkit.getServer().getPluginManager().registerEvents(listener, this);
	}
	private void register(String command, CommandExecutor commandExecutor) {
		Bukkit.getServer().getPluginCommand(command).setExecutor(commandExecutor);
	}

	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	private boolean chatMuted = false;
	public boolean isChatMuted() {
		return chatMuted;
	}
	public void setChatMuted(boolean chatMuted) {
		this.chatMuted = chatMuted;
	}

	public void disableTablist(Plugin plugin) {
		log("Tablist has been disabled by an external plugin: " + plugin.getName());
		this.playerTablistedNameTask.cancel();
	}

	public void enableTablist(Plugin plugin) {
		log("Tablist has been enabled by an external plugin: " + plugin.getName());
		this.playerTablistedNameTask.runTaskTimer(this, 20, 20);
	}

	public String getPrefix() {
		return "§6§lUHCLounge §8\u00bb §e";
	}
	public String getNoPerms() {
		return getPrefix() + "§4Error: §cYou do not have permission to do this.";
	}
}
