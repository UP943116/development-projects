package net.thelounge.core.user;

import java.util.UUID;

public class TempInfoStoreUser {

	private Long id;
	private UUID uuid;
	private String name;

	public TempInfoStoreUser(Long id, UUID uuid, String name) {
		this.id = id;
		this.uuid = uuid;
		this.name = name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}
}
