package net.thelounge.core.sql;

import net.minecraft.util.org.apache.commons.io.IOUtils;
import net.thelounge.core.CorePlugin;
import net.thelounge.core.disguise.SQLReDisguiseObject;
import net.thelounge.core.disguise.skins.PlayerSkin;
import net.thelounge.core.events.CoreUserRankUpdateEvent;
import net.thelounge.core.group.PermissionGroup;
import net.thelounge.core.group.groups.Group;
import net.thelounge.core.punishments.PunishmentCreator;
import net.thelounge.core.punishments.PunishmentType;
import net.thelounge.core.punishments.temp.PunishmentLatest;
import net.thelounge.core.user.CoreUser;
import net.thelounge.core.user.TempInfoStoreUser;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.UUID;

public class SQLManager {

	private final CorePlugin corePlugin;
	private final ConnectionPoolManager connectionPoolManager;

	public SQLManager(CorePlugin corePlugin, FileConfiguration fileConfiguration) {
		this.corePlugin = corePlugin;
		this.connectionPoolManager = new ConnectionPoolManager(fileConfiguration, corePlugin);

		if (connectionPoolManager.hasFailed()) {
			Bukkit.getPluginManager().disablePlugin(this.corePlugin);
			return;
		}

		setUpServer();
		loadGroups();
		loadSkins();
	}

	public void close() {
		connectionPoolManager.closePool();
	}

	private void setUpServer() {
		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `servers` WHERE `serverPort` = ? LIMIT 1;");
			preparedStatement.setInt(1, Bukkit.getServer().getPort());
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					corePlugin.setServerName(resultSet.getString("serverName"));
				}
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void loadUser(CoreUser coreUser) {

		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `user` WHERE `userUUID` = ? LIMIT 1;");
			preparedStatement.setString(1, coreUser.getUuid().toString());

			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					coreUser.setNameColor(resultSet.getString("nameColor"));
					coreUser.setUserId(resultSet.getLong("userID"));
				}
			} else {
				preparedStatement = connection.prepareStatement("INSERT INTO `user` (userUUID, userName, gamePoints, nameColor) VALUES (?, ?, ?, ?)");
				preparedStatement.setString(1, coreUser.getUuid().toString());
				preparedStatement.setString(2, coreUser.getUserName());
				preparedStatement.setInt(3, 0);
				preparedStatement.setString(4, "§7");
				preparedStatement.executeUpdate();
				resultSet.close();
				preparedStatement.close();
				connection.close();
				loadUser(coreUser);
				return;
			}

			resultSet.close();
			preparedStatement.close();

			connection.close();

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public void loadUserExtra(CoreUser coreUser) {

		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `user_options` WHERE `userId` = ? LIMIT 1;");
			preparedStatement.setLong(1, coreUser.getUserId());

			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					coreUser.setPrivateMessages(resultSet.getBoolean("privateMessages"));
					coreUser.setStaffNotifications(resultSet.getBoolean("staffMessages"));
				}
			} else {
				preparedStatement = connection.prepareStatement("INSERT INTO `user_options` (userId, staffMessages, privateMessages) VALUES (?, ?, ?)");
				preparedStatement.setLong(1, coreUser.getUserId());
				preparedStatement.setBoolean(2, true);
				preparedStatement.setBoolean(3, true);
				preparedStatement.executeUpdate();
				resultSet.close();
				preparedStatement.close();
				connection.close();
				return;
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public void loadUserGroups(CoreUser coreUser) {

		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT `permissions_groups`.`rankName` AS `rankName` FROM `permissions_groups_user` INNER JOIN `permissions_groups` ON `permissions_groups`.`rankId` = `permissions_groups_user`.`rankId` WHERE `permissions_groups_user`.`userId` = ?;");
			preparedStatement.setLong(1, coreUser.getUserId());

			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					coreUser.getPermissionGroups().add(corePlugin.getGroupManager().getGroup(resultSet.getString("rankName")));
				}
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public Long logIp(String ipAddress) {
		Long ipId = (long) -1;

		try(Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `ips` WHERE `ipAddress` = ? LIMIT 1;");
			preparedStatement.setString(1, corePlugin.getUtils().hashText(ipAddress, "MD5"));
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					ipId = resultSet.getLong("ipId");
				}
			} else {
				preparedStatement = connection.prepareStatement("INSERT INTO `ips` (ipAddress, ipTime) VALUES (?, ?)");
				preparedStatement.setString(1, corePlugin.getUtils().hashText(ipAddress, "MD5"));
				preparedStatement.setLong(2, timeMillis());
				preparedStatement.executeUpdate();
				resultSet.close();
				preparedStatement.close();
				connection.close();
				return logIp(ipAddress);
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ipId;
	}

	public void saveIp(CoreUser coreUser, Long ipId) {

		try(Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `user_ips` WHERE `ipId` = ? AND `userId` = ? LIMIT 1;");
			preparedStatement.setLong(1, ipId);
			preparedStatement.setLong(2, coreUser.getUserId());
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
			} else {
				preparedStatement = connection.prepareStatement("INSERT INTO `user_ips` VALUES (?, ?, ?)");
				preparedStatement.setLong(1, coreUser.getUserId());
				preparedStatement.setLong(2, ipId);
				preparedStatement.setBoolean(3, false);
				preparedStatement.executeUpdate();
				resultSet.close();
				preparedStatement.close();
				connection.close();
				return;
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Long timeMillis() {
		TimeZone timeZone = TimeZone.getTimeZone("Universal");
		Calendar calendar = Calendar.getInstance(timeZone);
		return calendar.getTimeInMillis();
	}

	private void loadGroups() {
		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `permissions_groups`;");
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM `permissions_groups_permissions` WHERE `rankId` = ?");
					preparedStatement1.setInt(1, resultSet.getInt("rankId"));
					preparedStatement1.executeQuery();

					HashMap<String, Boolean> stringBooleanHashMap = new HashMap<>();

					ResultSet resultSet1 = preparedStatement1.getResultSet();
					if(resultSet1.isBeforeFirst()) {
						while (resultSet1.next()) {
							stringBooleanHashMap.put(resultSet1.getString("rankPermission"), true);
						}
					}
					corePlugin.getGroupManager().createGroup(resultSet.getString("rankName"), new Group(
							resultSet.getInt("rankId"),
							stringBooleanHashMap,
							resultSet.getString("rankName"),
							resultSet.getString("rankPrefix"),
							resultSet.getInt("rankPriority")
							));

					resultSet1.close();
					preparedStatement1.close();

				}
			}

			resultSet.close();
			preparedStatement.close();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public TempInfoStoreUser getUserFromName(String username) {

		TempInfoStoreUser tempInfoStoreUser = null;

		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `user` WHERE `userName` = ? LIMIT 1;");
			preparedStatement.setString(1, username);
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					tempInfoStoreUser = new TempInfoStoreUser(resultSet.getLong("userId"), UUID.fromString(resultSet.getString("userUUID")), resultSet.getString("userName"));
				}
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tempInfoStoreUser;
	}

	public TempInfoStoreUser getUserFromUUID(UUID uuid) {

		TempInfoStoreUser tempInfoStoreUser = null;

		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `user` WHERE `userUUID` = ? LIMIT 1;");
			preparedStatement.setString(1, uuid.toString());
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					tempInfoStoreUser = new TempInfoStoreUser(resultSet.getLong("userId"), UUID.fromString(resultSet.getString("userUUID")), resultSet.getString("userName"));
				}
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tempInfoStoreUser;
	}

	private TempInfoStoreUser getUserFromID(long id) {

		TempInfoStoreUser tempInfoStoreUser = null;

		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `user` WHERE `userId` = ? LIMIT 1;");
			preparedStatement.setLong(1, id);
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					tempInfoStoreUser = new TempInfoStoreUser(resultSet.getLong("userId"), UUID.fromString(resultSet.getString("userUUID")), resultSet.getString("userName"));
				}
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tempInfoStoreUser;
	}

	public boolean hasGroup(TempInfoStoreUser tempInfoStoreUser, PermissionGroup permissionGroup) {
		boolean result = false;
		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `permissions_groups_user` WHERE `userId` = ? AND `rankId` = ?");
			preparedStatement.setLong(1, tempInfoStoreUser.getId());
			preparedStatement.setInt(2, permissionGroup.getRankId());
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					result = true;
				}
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		return result;
	}

	public void addGroup(TempInfoStoreUser tempInfoStoreUser, PermissionGroup permissionGroup) {
		if(hasGroup(tempInfoStoreUser, permissionGroup)) {
			return;
		}

		corePlugin.getRedisManager().sendRankUpdate(CoreUserRankUpdateEvent.GroupAction.ADD, tempInfoStoreUser.getUuid(), permissionGroup.getName());
		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT IGNORE INTO `permissions_groups_user` VALUES (?, ?);");
			preparedStatement.setLong(1, tempInfoStoreUser.getId());
			preparedStatement.setInt(2, permissionGroup.getRankId());
			preparedStatement.executeUpdate();

			preparedStatement.close();
			connection.close();

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public void removeGroup(TempInfoStoreUser tempInfoStoreUser, PermissionGroup permissionGroup) {
		if(!hasGroup(tempInfoStoreUser, permissionGroup)) {
			return;
		}

		corePlugin.getRedisManager().sendRankUpdate(CoreUserRankUpdateEvent.GroupAction.REMOVE, tempInfoStoreUser.getUuid(), permissionGroup.getName());
		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `permissions_groups_user` WHERE `userId` = ? AND `rankId` = ?");
			preparedStatement.setLong(1, tempInfoStoreUser.getId());
			preparedStatement.setInt(2, permissionGroup.getRankId());
			preparedStatement.executeUpdate();

			preparedStatement.close();
			connection.close();

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public void saveUser(CoreUser coreUser) {
		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `user` SET `userName` =  ? WHERE `userId` = ?");
			preparedStatement.setString(1, coreUser.getUserName());
			preparedStatement.setLong(2, coreUser.getUserId());
			preparedStatement.executeUpdate();

			preparedStatement = connection.prepareStatement("UPDATE `user_options` SET `privateMessages` = ?, `staffMessages` = ? WHERE `userId` = ?");
			preparedStatement.setBoolean(1, coreUser.isPrivateMessages());
			preparedStatement.setBoolean(2, coreUser.isStaffNotifications());
			preparedStatement.setLong(3, coreUser.getUserId());
			preparedStatement.executeUpdate();

			preparedStatement.close();
			connection.close();

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public void changeUserColor(CoreUser coreUser) {
		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `user` SET `nameColor` =  ? WHERE `userId` = ?");
			preparedStatement.setString(1, coreUser.getNameColor());
			preparedStatement.setLong(2, coreUser.getUserId());
			preparedStatement.executeUpdate();

			preparedStatement.close();
			connection.close();

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public void transfer() {
		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `user_permissions`;");
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					TempInfoStoreUser user = getUserFromUUID(UUID.fromString(resultSet.getString("uuid")));

					if(user != null) {
						if(corePlugin.getGroupManager().groupExists(resultSet.getString("rank"))) {
							addGroup(user, corePlugin.getGroupManager().getGroup(resultSet.getString("rank")));
							corePlugin.log("TRANSFER: " + user.getName() + " DONE RANK " + resultSet.getString("rank"));
						}

						PreparedStatement statement = connection.prepareStatement("DELETE FROM `user_permissions` WHERE `uuid` = ? AND `rank` = ?");
						statement.setString(1, user.getUuid().toString());
						statement.setString(2, resultSet.getString("rank"));
						statement.executeUpdate();
						statement.close();
					} else {
						String name = getName(resultSet.getString("uuid"));

						if(!name.equalsIgnoreCase("error")) {
							PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO `user` (userUUID, userName, gamePoints, nameColor) VALUES (?, ?, ?, ?)");
							preparedStatement2.setString(1, resultSet.getString("uuid"));
							preparedStatement2.setString(2, name);
							preparedStatement2.setInt(3, 0);
							preparedStatement2.setString(4, "§7");
							preparedStatement2.executeUpdate();
							preparedStatement2.close();
						}
					}
				}
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();

		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}

	public String getName(String uuid) {
		String url = "https://api.mojang.com/user/profiles/"+uuid.replace("-", "")+"/names";
		try {
			@SuppressWarnings("deprecation")
			String nameJson = IOUtils.toString(new URL(url));
			JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
			String playerSlot = nameValue.get(nameValue.size()-1).toString();
			JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
			return nameObject.get("name").toString();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return "error";
	}

	public boolean disguiseCanUseName(String name) {

		boolean canUse = false;

		try (Connection connection = connectionPoolManager.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `user_disguise` WHERE `disguiseName` = ?");
			preparedStatement.setString(1, name);
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					canUse = true;
				}
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(getUserFromName(name) == null) canUse = true;

		return canUse;
	}

	public void removeDisguise(CoreUser coreUser) {

		try (Connection connection = connectionPoolManager.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `user_disguise` WHERE `userId` = ?");
			preparedStatement.setLong(1, coreUser.getUserId());
			preparedStatement.executeUpdate();

			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addDisguise(CoreUser coreUser, String name, Long skinId) {

		try (Connection connection = connectionPoolManager.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `user_disguise` VALUES (?, ?, ?)");
			preparedStatement.setLong(1, coreUser.getUserId());
			preparedStatement.setString(2, name);
			preparedStatement.setLong(3, skinId);
			preparedStatement.executeUpdate();

			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void loadDisguise(CoreUser coreUser) {

		try (Connection connection = connectionPoolManager.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `user_disguise` WHERE `userId` = ?");
			preparedStatement.setLong(1, coreUser.getUserId());
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					coreUser.setSqlReDisguiseObject(new SQLReDisguiseObject(resultSet.getString("disguiseName"), resultSet.getLong("skinId")));
				}
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	public void loadSkins() {
		try (Connection connection = connectionPoolManager.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `disguise_skins`;");
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					corePlugin.getDisguiseManager().getPlayerSkinHashMap().put(resultSet.getLong("skinId"), new PlayerSkin(resultSet.getLong("skinId"), resultSet.getString("skinValue"), resultSet.getString("skinSignature")));
				}
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addDisguiseSkin(String value, String signature) {

		try (Connection connection = connectionPoolManager.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `disguise_skins` (skinValue, skinSignature) VALUES (?, ?)");
			preparedStatement.setString(1, value);
			preparedStatement.setString(2, signature);
			preparedStatement.executeUpdate();

			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean isDiscordLinked(CoreUser coreUser) {
		boolean linked = false;
		try (Connection connection = connectionPoolManager.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `user_link_discord` WHERE `userId` = ?");
			preparedStatement.setLong(1, coreUser.getUserId());
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()) {
					if(resultSet.getInt("discordId") != 0) {
						linked = true;
					}
				}
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return linked;
	}

	public void insertDiscordCode(CoreUser coreUser, String code) {

		try (Connection connection = connectionPoolManager.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `user_link_discord` VALUES (?, ?, ?)");
			preparedStatement.setLong(1, coreUser.getUserId());
			preparedStatement.setString(2, code);
			preparedStatement.setString(3, "0");
			preparedStatement.executeUpdate();

			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public String getDiscordCode(CoreUser coreUser) {
		String code = null;
		try (Connection connection = connectionPoolManager.getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `user_link_discord` WHERE `userId` = ?");
			preparedStatement.setLong(1, coreUser.getUserId());
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();

			if(resultSet.isBeforeFirst()) {
				while (resultSet.next()){
					code = resultSet.getString("linkCode");
				}
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return code;

	}

	public void createBan(PunishmentCreator punishmentCreator) {
		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `punishment_bans` (punishmentType, punisherId, punishedId, punishmentReasson, punishmentStart, punishmentEnd, punishmentServer) VALUES (?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setString(1, punishmentCreator.getPunishmentType().name());
			preparedStatement.setLong(2, punishmentCreator.getPunishmentCreator().getId());
			preparedStatement.setLong(3, punishmentCreator.getPunishedPlayer().getId());
			preparedStatement.setString(4, punishmentCreator.getPunishmentReason());
			preparedStatement.setLong(5, punishmentCreator.getPunishmentStart());
			preparedStatement.setLong(6, punishmentCreator.getPunishmentEnd());
			preparedStatement.setString(7, corePlugin.getServerName());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createKick(PunishmentCreator punishmentCreator) {
		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `punishment_kick` (punishmentType, punisherId, punishedId, punishmentReasson, punishmentStart, punishmentServer) VALUES (?, ?, ?, ?, ?, ?)");
			preparedStatement.setString(1, punishmentCreator.getPunishmentType().name());
			preparedStatement.setLong(2, punishmentCreator.getPunishmentCreator().getId());
			preparedStatement.setLong(3, punishmentCreator.getPunishedPlayer().getId());
			preparedStatement.setString(4, punishmentCreator.getPunishmentReason());
			preparedStatement.setLong(5, punishmentCreator.getPunishmentStart());
			preparedStatement.setString(6, corePlugin.getServerName());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createMute(PunishmentCreator punishmentCreator) {
		try (Connection connection = connectionPoolManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `punishment_mutes` (punishmentType, punisherId, punishedId, punishmentReasson, punishmentStart, punishmentEnd, punishmentServer) VALUES (?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setString(1, punishmentCreator.getPunishmentType().name());
			preparedStatement.setLong(2, punishmentCreator.getPunishmentCreator().getId());
			preparedStatement.setLong(3, punishmentCreator.getPunishedPlayer().getId());
			preparedStatement.setString(4, punishmentCreator.getPunishmentReason());
			preparedStatement.setLong(5, punishmentCreator.getPunishmentStart());
			preparedStatement.setLong(6, punishmentCreator.getPunishmentEnd());
			preparedStatement.setString(7, corePlugin.getServerName());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public PunishmentLatest getLatestPunishment(PunishmentType punishmentType, CoreUser coreUser) {
		PunishmentLatest punishmentLatest = null;

		switch (punishmentType) {

			case MUTE:
				try (Connection connection = connectionPoolManager.getConnection()) {
					PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `punishment_mutes` WHERE `punishedId` = ? LIMIT 1;");
					preparedStatement.setLong(1, coreUser.getUserId());
					preparedStatement.executeQuery();

					ResultSet resultSet = preparedStatement.getResultSet();

					if(resultSet.isBeforeFirst()) {
						while (resultSet.next()) {
							punishmentLatest = new PunishmentLatest(
									corePlugin,
									getPunishmentType(resultSet.getString("punishmentType")),
									coreUser,
									resultSet.getLong("punishmentStart"),
									resultSet.getLong("punishmentEnd"),
									resultSet.getString("punishmentReason"),
									getUserFromID(resultSet.getLong("punisherId"))
									);
						}
					}
					resultSet.close();
					preparedStatement.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case BAN:
				try (Connection connection = connectionPoolManager.getConnection()) {
					PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `punishment_bans` WHERE `punishedId` = ? LIMIT 1;");
					preparedStatement.setLong(1, coreUser.getUserId());
					preparedStatement.executeQuery();

					ResultSet resultSet = preparedStatement.getResultSet();

					if(resultSet.isBeforeFirst()) {
						while (resultSet.next()) {
							punishmentLatest = new PunishmentLatest(
									corePlugin,
									getPunishmentType(resultSet.getString("punishmentType")),
									coreUser,
									resultSet.getLong("punishmentStart"),
									resultSet.getLong("punishmentEnd"),
									resultSet.getString("punishmentReason"),
									getUserFromID(resultSet.getLong("punisherId"))
							);
						}
					}
					resultSet.close();
					preparedStatement.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
		}

		return punishmentLatest;
	}



	private PunishmentType getPunishmentType(String punishmentType) {
		for(PunishmentType punishmentTypes : PunishmentType.values()) {
			if(punishmentTypes.name().equalsIgnoreCase(punishmentType)) {
				return punishmentTypes;
			}
		}
		return PunishmentType.UNKNOWN;
	}
}