package net.thelounge.core.disguise;

public class SQLReDisguiseObject {

	private String disguiseName;
	private Long skinId;

	public SQLReDisguiseObject(String disguiseName, Long skinId) {
		this.disguiseName = disguiseName;
		this.skinId = skinId;
	}

	public String getDisguiseName() {
		return disguiseName;
	}

	public Long getSkinId() {
		return skinId;
	}
}
