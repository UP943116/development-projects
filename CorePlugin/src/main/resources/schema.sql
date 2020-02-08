CREATE TABLE IF NOT EXISTS `servers`(
    serverName VARCHAR(40) NOT NULL,
    serverAddress TEXT(40) NOT NULL,
    serverPort INT(20) NOT NULL,
    serverType TEXT(40) NOT NULL,
    serverPrivate BOOLEAN NOT NULL
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS `ips`(
    ipId BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    ipAddress TEXT(40) NOT NULL,
    ipTime BIGINT(20) NOT NULL
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS `user`(
    userId BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    userUUID VARCHAR(36) UNIQUE NOT NULL,
    userName VARCHAR(16) NOT NULL,
    gamePoints BIGINT(20) NOT NULL,
    nameColor VARCHAR(4) NOT NULL DEFAULT '§7',
    INDEX `userdata_ign_index`(`userName`),
    INDEX `userdata_uuid_index`(`userUUID`)
) ENGINE = InnoDB;
CREATE TABLE IF NOT EXISTS `disguise_skins`(
    skinId BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    skinValue TEXT(1000),
    skinSignature TEXT(1000)
)
CREATE TABLE IF NOT EXISTS `user_disguise`(
    userId BIGINT(20) NOT NULL,
    disguiseName VARCHAR(16) NOT NULL,
    skinId BIGINT(20) NOT NULL,
    FOREIGN KEY(`userId`) REFERENCES `user`(`userId`) ON DELETE CASCADE,
    FOREIGN KEY(`skinId`) REFERENCES `disguise_skins`(`skinId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user_ips`(
    userId BIGINT(20) NOT NULL,
    ipId BIGINT(20) NOT NULL,
    ipVerified BOOLEAN,
    FOREIGN KEY(`userId`) REFERENCES `user`(`userId`) ON DELETE CASCADE,
    FOREIGN KEY(`ipId`) REFERENCES `ips`(`ipId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user_options`(
    userId BIGINT(20) NOT NULL,
    staffMessages BOOLEAN,
    privateMessages BOOLEAN,
    FOREIGN KEY(`userId`) REFERENCES `user`(`userId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `permissions_groups`(
    rankId INT(20) AUTO_INCREMENT PRIMARY KEY,
    rankName VARCHAR(100),
    rankPrefix VARCHAR(100),
    rankPriority INT(20)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `punishment_bans`(
    punishmentId BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    punishmentType BIGINT(20),
    punisherId BIGINT(20),
    punishedId BIGINT(20),
    punishmentReason TEXT(100),
    punishmentStart BIGINT(20),
    punishmentEnd BIGINT(20),
    punishmentServer TEXT(50),
    FOREIGN KEY (`punisherId`) REFERENCES `user`(`userId`) ON DELETE CASCADE,
    FOREIGN KEY (`punishedId`) REFERENCES `user`(`userId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `punishment_kick`(
    punishmentId BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    punisherId BIGINT(20),
    punishedId BIGINT(20),
    punishmentReason TEXT(100),
    punishmentTime BIGINT(20),
    punishmentServer TEXT(50),
    FOREIGN KEY (`punisherId`) REFERENCES `user`(`userId`) ON DELETE CASCADE,
    FOREIGN KEY (`punishedId`) REFERENCES `user`(`userId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `punishment_mute`(
    punishmentId BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    punishmentType BIGINT(20),
    punisherId BIGINT(20),
    punishedId BIGINT(20),
    punishmentReason TEXT(100),
    punishmentStart BIGINT(20),
    punishmentEnd BIGINT(20),
    punishmentServer TEXT(50),
    FOREIGN KEY (`punisherId`) REFERENCES `user`(`userId`) ON DELETE CASCADE,
    FOREIGN KEY (`punishedId`) REFERENCES `user`(`userId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `permissions_groups_permissions`(
    rankId INT(20),
    rankPermission TEXT(100),
    FOREIGN KEY(`rankId`) REFERENCES `permissions_groups`(`rankId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `permissions_groups_user`(
    userId BIGINT(20),
    rankId INT(20),
    FOREIGN KEY(`userId`) REFERENCES `user`(`userId`) ON DELETE CASCADE,
    FOREIGN KEY(`rankId`) REFERENCES `permissions_groups`(`rankId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user_link_discord`(
userId BIGINT(20) NOT NULL,
linkCode VARCHAR(20),
discordId VARCHAR(32),
FOREIGN KEY (`userId`) REFERENCES `user`(`userId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user_stats_simulator`(
userId BIGINT(20) NOT NULL,
gameKills INT(20),
gameWins INT(20),
gameDeaths INT(20),
gameELO INT(20),
FOREIGN KEY (`userId`) REFERENCES `user`(`userId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user_stats_ffa`(
userId BIGINT(20) NOT NULL,
gameKills INT(20),
gameDeaths INT(20),
FOREIGN KEY (`userId`) REFERENCES `user`(`userId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user_kits_ffa`(
userId BIGINT(20) NOT NULL,
gameKit TEXT(1000),
FOREIGN KEY (`userId`) REFERENCES `user`(`userId`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user_stats_uhc`(
userId BIGINT(20) NOT NULL,
statKill INT(20),
statWin INT(20),
statDeath INT(20),
statGamesPlayed INT(20),
statSwordSwings INT(20),
statSwordHits INT(20),
statBowShots INT(20),
statBowHits INT(20),
statPlacedBlocks INT(20),
statMinedBlocks INT(20),
statMinedOreCoal INT(20),
statMinedOreIron INT(20),
statMinedOreGold INT(20),
statMinedOreDiamond INT(20),
statMinedOreEmerald INT(20),
statMinedOreLapis INT(20),
statMinedOreRedstone INT(20),
statMinedOreQuartz INT(20),
statMinedMobSpawners INT(20),
FOREIGN KEY (`userId`) REFERENCES `user`(`userId`) ON DELETE CASCADE
) ENGINE = InnoDB;

INSERT
INTO
    `user`
VALUES
(0, "TWISTAR-4694-4ba7-83fe-suchspeedgay", "LoungeCheat", 0, "§7");

INSERT
INTO
    `permissions_groups`(`rankName`, `rankPrefix`, `rankPriority`)
VALUES
()
INSERT
INTO
    `permissions_groups_permissions`(`rankId`, `rankPermission`)
VALUES
(1, "litebans.*"),
(1, "venix.staff"),
(1, "bukkit.command.whitelist"),
(1, "bukkit.command.op"),
(1, "bukkit.command.deop"),
(1, "uhc.disguise"),
(1, "uhc.moderator"),
(1, "uhc.lavachallenge"),
(1, "uhc.host"),
(1, "uhc.latejoin"),
(1, "uhc.bypass.whitelist"),
(1, "uhc.fulljoin"),
(1, "venix.admin"),
(1, "venix.senior"),
(1, "venix.trial"),
(1, "permissions.use"),
(1, "announce.use"),
(1, "perms.color"),

(2, "litebans.*"),
(2, "venix.staff"),
(2, "bukkit.command.whitelist"),
(2, "bukkit.command.op"),
(2, "bukkit.command.deop"),
(2, "uhc.disguise"),
(2, "uhc.moderator"),
(2, "uhc.lavachallenge"),
(2, "uhc.host"),
(2, "uhc.latejoin"),
(2, "uhc.bypass.whitelist"),
(2, "uhc.fulljoin"),
(2, "venix.admin"),
(2, "venix.senior"),
(2, "venix.trial"),
(2, "permissions.use"),
(2, "announce.use"),
(2, "perms.color");
