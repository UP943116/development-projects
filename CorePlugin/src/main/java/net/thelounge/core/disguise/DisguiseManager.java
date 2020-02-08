package net.thelounge.core.disguise;

import net.minecraft.server.v1_7_R4.DedicatedPlayerList;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumDifficulty;
import net.minecraft.server.v1_7_R4.EnumGamemode;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PacketPlayOutRespawn;
import net.minecraft.server.v1_7_R4.WorldType;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import net.thelounge.core.CorePlugin;
import net.thelounge.core.disguise.api.DisguiseObject;
import net.thelounge.core.disguise.api.GameApisRest;
import net.thelounge.core.disguise.skins.PlayerSkin;
import net.thelounge.core.user.CoreUser;
import net.thelounge.core.disguise.util.Protocol;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DisguiseManager {

	private final CorePlugin corePlugin;
	private final DisguiseUtils disguiseUtils;
	private final GameApisRest gameApisRest;
	private final Protocol protocol;
	private final Random random = new Random();

	private HashMap<Long, PlayerSkin> playerSkinHashMap;

	public DisguiseManager(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;

		this.gameApisRest = new GameApisRest(this.corePlugin);
		this.disguiseUtils = new DisguiseUtils();
		this.protocol = new Protocol();

		playerSkinHashMap = new HashMap<>();

	}

	public HashMap<Long, PlayerSkin> getPlayerSkinHashMap() {
		return playerSkinHashMap;
	}

	public void createSkin(String uuid) {
		DisguiseObject object = gameApisRest.getProfile(uuid).getDisguiseObject();
		corePlugin.getSqlManager().addDisguiseSkin(object.getValue(), object.getSignature());
	}

	public boolean canUseDisguiseName(String disguiseName) {

		boolean canUse = true;

		for(String s : disguiseUtils.getBlockedChars()) {
			if(disguiseName.toLowerCase().contains(s)) {
				return false;
			}
		}

		for(String s : disguiseUtils.getBlockedWords()) {
			if(disguiseName.toLowerCase().contains(s)) {
				return false;
			}
		}

		if(!corePlugin.getSqlManager().disguiseCanUseName(disguiseName)) return false;

		return canUse;
	}

	public String getRandomName() {
		String name = "";

		List<String> names = new ArrayList<>(disguiseUtils.getJoinableWordNames());
		Collections.shuffle(names);
			name = names.get(0);


		if(canUseDisguiseName(name)) {
			return name;
		}
		return getRandomName();
	}

	public void disguisePlayer(Player player, String name) {

		CoreUser coreUser = corePlugin.getUserManager().getUser(player.getUniqueId());

		EntityPlayer entityPlayer = protocol.getEntity(player);
		GameProfile gameProfile = entityPlayer.getProfile();
		int entityId = entityPlayer.getId();

		ArrayList<PlayerSkin> skins = new ArrayList<>(playerSkinHashMap.values());
		Collections.shuffle(skins);

		PlayerSkin playerSkin = skins.get(0);

		DisguiseObject disguiseObject = playerSkin.getDisguiseObject(name);
		disguiseObject.setName(name);

		for (Property property : gameProfile.getProperties().get("textures")) {
			DisguiseObject disguiseObjectOrginal = new DisguiseObject(gameProfile.getName(), property.getValue(), property.getSignature());
			disguiseObjectOrginal.setName(player.getName());
			coreUser.setDisguised(disguiseObjectOrginal);
		}

		gameProfile.getProperties().clear();
		entityPlayer.displayName = name;
		entityPlayer.listName = name;
		setValue(gameProfile, "name", disguiseObject.getName());
		gameProfile.getProperties().put("textures", new Property("textures", disguiseObject.getValue(), disguiseObject.getSignature()));

		Object destroy = new PacketPlayOutEntityDestroy(new int[] { entityId });
		protocol.broadcastWorld((Packet)destroy, player);

		DedicatedPlayerList playerList = getValue(Bukkit.getServer(), "playerList", DedicatedPlayerList.class);
		playerList.playerMap.remove(player.getName());
		playerList.playerMap.put(name, entityPlayer);

		Packet add = PacketPlayOutPlayerInfo.addPlayer(entityPlayer);
		setValue(add, "username", name);
		protocol.broadcastServer(add, player);

		Packet spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
		protocol.broadcastWorld(spawn, player);
		for (int i = 1; i <= 4; i++) {
			Packet equip = new PacketPlayOutEntityEquipment(entityId, i, entityPlayer.getEquipment(i));
			protocol.broadcastWorld(equip, player);
		}

		if(protocol.isClient18(player)) {
			Location location = player.getLocation();

			int dimension = entityPlayer.getWorld().worldData.j();
			EnumDifficulty difficulty = entityPlayer.getWorld().difficulty;
			WorldType type = entityPlayer.getWorld().worldData.getType();
			EnumGamemode gamemode = entityPlayer.playerInteractManager.getGameMode();

			Packet respawn = new PacketPlayOutRespawn(dimension, difficulty, type, gamemode);
			entityPlayer.playerConnection.sendPacket(respawn);

			entityPlayer.triggerHealthUpdate();
			player.updateInventory();
			player.teleport(location);
		}

		corePlugin.getSqlManager().addDisguise(coreUser, name, playerSkin.getSkinId());

		player.sendMessage(corePlugin.getPrefix() + "§aYou are now disguised.");

	}

	public void reDisguisePlayer(Player player, SQLReDisguiseObject reDisguiseObject) {

		CoreUser coreUser = corePlugin.getUserManager().getUser(player.getUniqueId());
		EntityPlayer entityPlayer = protocol.getEntity(player);
		GameProfile gameProfile = entityPlayer.getProfile();
		int entityId = entityPlayer.getId();


		DisguiseObject disguiseObject = playerSkinHashMap.getOrDefault(reDisguiseObject.getSkinId(), playerSkinHashMap.get((long)1)).getDisguiseObject(reDisguiseObject.getDisguiseName());

		for (Property property : gameProfile.getProperties().get("textures")) {
			DisguiseObject disguiseObjectOrginal = new DisguiseObject(gameProfile.getName(), property.getValue(), property.getSignature());
			disguiseObjectOrginal.setName(player.getName());
			coreUser.setDisguised(disguiseObjectOrginal);
		}

		gameProfile.getProperties().clear();
		entityPlayer.displayName = reDisguiseObject.getDisguiseName();
		entityPlayer.listName = reDisguiseObject.getDisguiseName();
		setValue(gameProfile, "name", disguiseObject.getName());
		gameProfile.getProperties().put("textures", new Property("textures", disguiseObject.getValue(), disguiseObject.getSignature()));

		Object destroy = new PacketPlayOutEntityDestroy(new int[] { entityId });
		protocol.broadcastWorld((Packet)destroy, player);

		DedicatedPlayerList playerList = getValue(Bukkit.getServer(), "playerList", DedicatedPlayerList.class);
		playerList.playerMap.remove(player.getName());
		playerList.playerMap.put(reDisguiseObject.getDisguiseName(), entityPlayer);

		Packet add = PacketPlayOutPlayerInfo.addPlayer(entityPlayer);
		setValue(add, "username", reDisguiseObject.getDisguiseName());
		protocol.broadcastServer(add, player);

		Packet spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
		protocol.broadcastWorld(spawn, player);
		for (int i = 1; i <= 4; i++) {
			Packet equip = new PacketPlayOutEntityEquipment(entityId, i, entityPlayer.getEquipment(i));
			protocol.broadcastWorld(equip, player);
		}

		if(protocol.isClient18(player)) {
			Location location = player.getLocation();

			int dimension = entityPlayer.getWorld().worldData.j();
			EnumDifficulty difficulty = entityPlayer.getWorld().difficulty;
			WorldType type = entityPlayer.getWorld().worldData.getType();
			EnumGamemode gamemode = entityPlayer.playerInteractManager.getGameMode();

			Packet respawn = new PacketPlayOutRespawn(dimension, difficulty, type, gamemode);
			entityPlayer.playerConnection.sendPacket(respawn);

			entityPlayer.triggerHealthUpdate();
			player.updateInventory();
			player.teleport(location);
		}

		player.sendMessage(corePlugin.getPrefix() + "§aYou are now re-disguised.");

	}

	public void undisguisePlayer(Player player) {

		String name = player.getName();

		CoreUser coreUser = corePlugin.getUserManager().getUser(player.getUniqueId());

		coreUser.setUnDisguised();
		corePlugin.getSqlManager().removeDisguise(coreUser);

		EntityPlayer entityPlayer = protocol.getEntity(player);
		GameProfile gameProfile = entityPlayer.getProfile();
		int entityId = entityPlayer.getId();


		gameProfile.getProperties().clear();
		entityPlayer.displayName = coreUser.getUndisguiseObject().getName();
		entityPlayer.listName = coreUser.getUndisguiseObject().getName();
		setValue(gameProfile, "name", coreUser.getUndisguiseObject().getName());
		gameProfile.getProperties().put("textures", new Property("textures", coreUser.getUndisguiseObject().getValue(), coreUser.getUndisguiseObject().getSignature()));

		Object destroy = new PacketPlayOutEntityDestroy(new int[] { entityId });
		protocol.broadcastWorld((Packet)destroy, player);

		DedicatedPlayerList playerList = getValue(Bukkit.getServer(), "playerList", DedicatedPlayerList.class);
		playerList.playerMap.remove(name);
		playerList.playerMap.put(coreUser.getUndisguiseObject().getName(), entityPlayer);

		Packet add = PacketPlayOutPlayerInfo.addPlayer(entityPlayer);
		setValue(add, "username", coreUser.getUndisguiseObject().getName());
		protocol.broadcastServer(add, player);

		Packet spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
		protocol.broadcastWorld(spawn, player);
		for (int i = 1; i <= 4; i++) {
			Packet equip = new PacketPlayOutEntityEquipment(entityId, i, entityPlayer.getEquipment(i));
			protocol.broadcastWorld(equip, player);
		}

		if(protocol.isClient18(player)) {
			Location location = player.getLocation();

			int dimension = entityPlayer.getWorld().worldData.j();
			EnumDifficulty difficulty = entityPlayer.getWorld().difficulty;
			WorldType type = entityPlayer.getWorld().worldData.getType();
			EnumGamemode gamemode = entityPlayer.playerInteractManager.getGameMode();

			Packet respawn = new PacketPlayOutRespawn(dimension, difficulty, type, gamemode);
			entityPlayer.playerConnection.sendPacket(respawn);

			entityPlayer.triggerHealthUpdate();
			player.updateInventory();
			player.teleport(location);
		}


		player.sendMessage(corePlugin.getPrefix() + "§aYou are now un-disguised.");

	}

	private void setValue(Object object, String field, Object value) {

		try {
			Field f = object.getClass().getDeclaredField(field);

			if (!f.isAccessible()) {
				f.setAccessible(true);
			}

			f.set(object, value);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getValue(Object object, String field) {
		try {
			Field f = object.getClass().getDeclaredField(field);
			if (!f.isAccessible()) {
				f.setAccessible(true);
			}
			return f.get(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T getValue(Object object, String field, Class<T> clazz) {
		return (T)getValue(object, field);
	}
}
