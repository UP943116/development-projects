package net.thelounge.core.disguise.api;

import net.thelounge.core.CorePlugin;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameApisRest {

	private final CorePlugin corePlugin;

	public GameApisRest(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}
	private final Gson gson = new Gson();

	public Profile getProfile(String uuidOrName) {
		String ip = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuidOrName.replaceAll("-", "") + "?unsigned=false";
		String json = readJson(ip);
		return gson.fromJson(json, Profile.class);
	}

	private String readJson(String ip) {
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(ip);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
			http.setConnectTimeout(3000);
			http.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
			String l;
			while ((l = reader.readLine()) != null) {
				sb.append(l);
			}
			reader.close();

		} catch (Exception e) {
			corePlugin.log("[SEVERE] " +e.getMessage());
		}
		return sb.toString();
	}
}
