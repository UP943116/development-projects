package net.thelounge.core.punishments.temp;

public class PunishmentMute {

	private String muteReason;
	private int unmuteSeconds;
	private String mutedBy;

	public PunishmentMute(String muteReason, int unmuteSeconds, String mutedBy) {
		this.muteReason = muteReason;
		this.unmuteSeconds = unmuteSeconds;
		this.mutedBy = mutedBy;
	}

	public int getUnmuteSeconds() {
		return unmuteSeconds;
	}
}
