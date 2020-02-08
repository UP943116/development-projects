package net.thelounge.core.punishments;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.user.TempInfoStoreUser;
import net.thelounge.core.util.TimeStamp;

public class PunishmentCreator {

	private final CorePlugin corePlugin;

	public PunishmentCreator(CorePlugin corePlugin) {
		this.corePlugin = corePlugin;
	}

	private PunishmentType punishmentType;
	private Integer punishmentLength;
	private String punishmentReason;
	private TimeStamp timeStamp;
	private boolean silentPunishment = true;

	private TempInfoStoreUser punishmentCreator, punishedPlayer;

	public PunishmentCreator setPunishmentLength(Integer punishmentLength) {
		this.punishmentLength = punishmentLength;
		return this;
	}

	public PunishmentCreator setPunishedPlayer(TempInfoStoreUser punishedPlayer) {
		this.punishedPlayer = punishedPlayer;
		return this;
	}

	public PunishmentCreator setPunishmentType(PunishmentType punishmentType) {
		this.punishmentType = punishmentType;
		return this;
	}

	public PunishmentCreator setPunishmentReason(String punishmentReason) {
		this.punishmentReason = punishmentReason;
		return this;
	}

	public PunishmentCreator setPunishmentCreator(TempInfoStoreUser punishmentCreator) {
		this.punishmentCreator = punishmentCreator;
		return this;
	}

	public PunishmentCreator setSilentPunishment(boolean silentPunishment) {
		this.silentPunishment = silentPunishment;
		return this;
	}

	public PunishmentType getPunishmentType() {
		return punishmentType;
	}

	public TempInfoStoreUser getPunishmentCreator() {
		return punishmentCreator;
	}

	public TempInfoStoreUser getPunishedPlayer() {
		return punishedPlayer;
	}

	public String getPunishmentReason() {
		return punishmentReason;
	}

	private long punishmentStart, punishmentEnd;

	public long getPunishmentStart() {
		return punishmentStart;
	}

	public long getPunishmentEnd() {
		return punishmentEnd;
	}

	public void create() {

		this.punishmentStart = corePlugin.getUtils().currentTime();
		this.punishmentEnd = punishmentStart + (punishmentLength * timeStamp.getMultiplier() * 1000L);

		corePlugin.getRedisManager().sendPunishment(this);

		switch (punishmentType) {
			case BAN:
			case UNBAN:
				corePlugin.getSqlManager().createBan(this);
				break;

			case MUTE:
			case UNMUTE:
				corePlugin.getSqlManager().createMute(this);
				break;

			case KICK:
				corePlugin.getSqlManager().createKick(this);
				break;
		}

	}
}
