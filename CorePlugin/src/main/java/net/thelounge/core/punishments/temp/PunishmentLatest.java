package net.thelounge.core.punishments.temp;

import net.thelounge.core.CorePlugin;
import net.thelounge.core.punishments.PunishmentType;
import net.thelounge.core.user.CoreUser;
import net.thelounge.core.user.TempInfoStoreUser;

public class PunishmentLatest {

	private final CorePlugin corePlugin;

	private PunishmentType punishmentType;
	private CoreUser punishmentOwner;
	private Long startTime, endTime;
	private String reason;
	private TempInfoStoreUser punishedBy;

	public PunishmentLatest(CorePlugin corePlugin, PunishmentType punishmentType, CoreUser punishmentOwner, Long startTime, Long endTime, String reason, TempInfoStoreUser punishedBy) {
		this.corePlugin = corePlugin;
		this.punishmentType = punishmentType;
		this.punishmentOwner = punishmentOwner;
		this.startTime = startTime;
		this.endTime = endTime;
		this.reason = reason;
		this.punishedBy = punishedBy;
	}
}
