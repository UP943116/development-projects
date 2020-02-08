package net.thelounge.core.disguise.skins;

import net.thelounge.core.disguise.api.DisguiseObject;

public class PlayerSkin {

	private Long skinId;
	private String skinValue, skinSignature;

	public PlayerSkin(Long skinId, String skinValue, String skinSignature) {
		this.skinId = skinId;
		this.skinValue = skinValue;
		this.skinSignature = skinSignature;
	}

	public Long getSkinId() {
		return skinId;
	}


	public DisguiseObject getDisguiseObject(String name) {
		return new DisguiseObject(name, skinValue, skinSignature);
	}
}
