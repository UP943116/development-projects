package net.thelounge.core.redis;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.events.AdminChatEvent;
import net.thelounge.core.events.CoreUserRankUpdateEvent;
import net.thelounge.core.events.ReportUserEvent;
import net.thelounge.core.events.StaffChatEvent;
import net.thelounge.core.punishments.PunishmentCreator;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;

public class RedisManager {

	private String SPLIT = "#~#";

	private JedisPool jedisPool;

	private final CorePlugin corePlugin;

	public RedisManager(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMinIdle(5);
		jedisPoolConfig.setMaxTotal(20);
		jedisPool = new JedisPool(jedisPoolConfig, corePlugin.getConfig().getString("redis-host"), corePlugin.getConfig().getInt("redis-port"), 2000, corePlugin.getConfig().getString("redis-pass"));
	}

	public void onDisable() {
		jedisPool.close();
	}

	public void onEnable() {
		jedisPool.getResource().connect();

		try (Jedis jedis = jedisPool.getResource()) {
			if(jedis.ping().equalsIgnoreCase("PONG")) {
				corePlugin.log("Redis server has been initialized.");
			}
		}

		JedisPubSub jedisPubSub = new JedisPubSub() {
			@Override
			public void onMessage(String channel, String message) {
				String[] arguments = message.split(SPLIT);
				switch (channel.toLowerCase()) {
					case "mc_groups":
						Bukkit.getServer().getPluginManager().callEvent(new CoreUserRankUpdateEvent(CoreUserRankUpdateEvent.GroupAction.valueOf(arguments[0].toUpperCase()), UUID.fromString(arguments[1]), arguments[2]));
						break;

					case "backend_reboot":
						Bukkit.getServer().shutdown();
						break;

					case "mc_chat":
						Bukkit.getServer().getPluginManager().callEvent(new StaffChatEvent(arguments[0], arguments[1], arguments[2]));
						break;

					case "mc_adminchat":
						Bukkit.getServer().getPluginManager().callEvent(new AdminChatEvent(arguments[0], arguments[1], arguments[2]));
						break;

					case "mc_report":
						Bukkit.getServer().getPluginManager().callEvent(new ReportUserEvent(arguments[0], arguments[1], arguments[2], arguments[3]));
						break;

				}
			}
		};

		new Thread(() -> {
			try (Jedis jedis = jedisPool.getResource()){
				jedis.subscribe(jedisPubSub, "mc_groups", "mc_chat", "mc_report", "mc_adminchat", "backend_reboot");
				corePlugin.log("Subscription ended.");
			} catch (Exception exception) {
				corePlugin.log("Subscribing failed." + exception.getMessage());
			}
		}).start();
	}

	public void sendRankUpdate(CoreUserRankUpdateEvent.GroupAction groupAction, UUID uuid, String group) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.publish("mc_groups", groupAction.name().toLowerCase() + SPLIT + uuid.toString() + SPLIT + group);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void sendStaffChat(String userName, String message) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.publish("mc_chat", userName + SPLIT + corePlugin.getServerName() + SPLIT + message);
		}
	}

	public void sendAdminChat(String userName, String message) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.publish("mc_adminchat", userName + SPLIT + corePlugin.getServerName() + SPLIT + message);
		}
	}

	public void sendReport(String userName, String userReported, String userReportedReason) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.publish("mc_report", userName + SPLIT + corePlugin.getServerName() + SPLIT + userReported + SPLIT + userReportedReason);
		}
	}

	public void sendAnnouncement(String announcement) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.publish("mc_announcement", "global" + SPLIT + announcement);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void sendReboot() {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.publish("backend_reboot", "global" + SPLIT + "reboot");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void sendPunishment(PunishmentCreator punishmentCreator) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.publish("mc_punishment", punishmentCreator.getPunishmentCreator().getName() + SPLIT + punishmentCreator.getPunishedPlayer().getName() + SPLIT + punishmentCreator.getPunishmentReason() + SPLIT + corePlugin.getServerName());
		}
	}
}
