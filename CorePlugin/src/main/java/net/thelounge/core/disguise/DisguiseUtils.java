package net.thelounge.core.disguise;

import java.util.ArrayList;
import java.util.Arrays;

public class DisguiseUtils {
	
	private final ArrayList<String> disguiseSkins = new ArrayList<>(
			Arrays.asList(
					"8497f7b7-f3ce-4272-8de6-b213c9501754",
					"01c29de1-068f-4e80-9d71-34f1f119a678",
					"1fa684a2-fafd-499e-a89d-c96542727e29",
					"722a9e3f-db16-417a-a136-c1d8beb6b023",
					"0e52ebec-84b4-4c8c-b235-0c54428a9fdb",
					"d9fb555b-4fef-42be-968b-0ab124f12d27",
					"1474c0cf-e5fb-447e-b987-551aefb5d975",
					"48435c70-6693-4bcc-86cb-b5d1aba6c693",
					"7538c3e7-08e0-45e5-a0c6-79437053d974",
					"83348230-6282-496e-ae7f-e9e59db84982",
					"c162a00d-1d5a-4903-bedc-2f40a1a438c0",
					"dd873325-45a2-4c22-996c-97510b72475b",
					"c0e63f10-f171-4d89-a137-64b739cfd990",
					"4597b386-59d3-4104-b6b9-377f58a42257",
					"8497f7b7-f3ce-4272-8de6-b213c9501754",
					"6f97f146-d964-4c16-a737-66d4ef64b182",
					"add5045f-b790-4efb-9a68-98b094685a43",
					"64f50d9f-586d-4b7e-a8d4-b0e8cfbe31b5",
					"e9357601-0f8f-4a93-aaef-ee99e30d110d",
					"74d79d17-5e89-4b69-8a0d-fe8a59a093b3",
					"9d700fd1-d65d-4d6e-a051-12e2d72a29bb",
					"9b263313-3737-4237-b6ce-a05b63a86de4",
					"6b1b82dc-e320-4cd5-9345-e1ecec3ac296",
					"63c5a6d5-1035-446e-bddc-46fc9c1d5303",
					"83ac9062-e28a-4d09-9509-09af3266a99f",
					"dbeaa5db-04bd-4eb3-ba63-bced4fb72b18",
					"78736722-c666-47c1-af12-8d6127df0eee",
					"ff6cde5d-d38f-4d6c-972b-ea47b29c334a",
					"b98ddfaa-ac83-47b1-940d-fac7903fc972",
					"c62dc040-e1f2-4505-8a53-21fc91bbf090",
					"635657a6-6c1b-4d19-be03-60a3c01c64bd",
					"405397f4-7095-4f27-bd4a-c862eb722b47",
					"9190bae3-dd9d-4c0f-9c4e-b8b6c49c0bdf",
					"83660cdb-3bce-45ce-a00a-0acc7a2a6d89",
					"097faf8a-999e-4d47-a1a5-0f18abbf3ab9",
					"d56ca246-7715-4856-a0ed-8184de711b1a",
					"02fd71dd-2972-4440-9762-f5dbb199228f",
					"7e27941e-024f-4ab6-9418-77b78f88de5f",
					"b88ba9fa-b1a3-49c0-b8e6-ea30f93c7f5a",
					"e33a73fd-7074-46c3-ada2-9e9fdd086191"
			)
	);

	public ArrayList<String> getDisguiseSkins() {
		return disguiseSkins;
	}

	private final ArrayList<String> blockedChars = new ArrayList<>(
			Arrays.asList(
					"`",
					"!",
					"\"",
					"\\",
					"£",
					"$",
					"%",
					"^",
					"&",
					"*",
					"(",
					")",
					"-",
					"=",
					"+",
					"|",
					",",
					"<",
					">",
					".",
					";",
					":",
					"@",
					"'",
					"#",
					"~",
					"[",
					"]",
					"{",
					"}",
					"?",
					"/",
					"æ",
					"ø",
					"å",
					"ф",
					"£",
					"€",
					"ö",
					"ä",
					"ã",
					"õ",
					"á",
					"é",
					"ф",
					"½"
					)
			);

	public ArrayList<String> getBlockedChars() {
		return blockedChars;
	}

	private final ArrayList<String> blockedWords = new ArrayList<>(
			Arrays.asList(
					"fuck",
					"shit",
					"penis",
					"vagina",
					"dick",
					"fag",
					"faggot",
					"nigger",
					"nigga",
					"niqqa",
					"fucker",
					"nazi",
					"nlgga",
					"kkk",
					"kukluxklan",
					"notch"
			)
	);


	public ArrayList<String> getBlockedWords() {
		return blockedWords;
	}

	private final ArrayList<String> joinableWordNames = new ArrayList<>(
			Arrays.asList(
					"AmazingAmazon",
					"CAPSnicklas",
					"ir0nvein",
					"Alexandr0",
					"running",
					"Blockgame",
					"st4ndinq",
					"mousePAD",
					"AmazingAmazon",
					"jumpingJF",
					"GODDOG",
					"lilGANG",
					"swimminq",
					"fishymc1",
					"gedra1ned",
					"Rodlezz",
					"Burg3r",
					"HorseHealinq",
					"StoneMiner",
					"CreeperDan",
					"P0rtalTrap",
					"Imcamping",
					"snipermen",
					"3nderb0is",
					"Emeraldo",
					"Villaqers",
					"_bookz",
					"P4nda_a",
					"ShearsAreOn",
					"InsaneNico",
					"GLoBaLchat",
					"lostMichi",
					"RoiseMary",
					"Siik0Mode",
					"OnTopOfLife",
					"Enslaved",
					"FuutaiHC",
					"TheTVGuy",
					"IloveNETFLIX",
					"MisterSomething",
					"90msKubek",
					"said511",
					"BeingSean",
					"troublemakers",
					"cinnabun__",
					"NikolasQruz",
					"Xx_King_GamerYT",
					"Skallun",
					"HistoryRemember",
					"ElReyJuan",
					"Hey_Cat",
					"_Boidy_",
					"YaBoiJuicy",
					"iUnidentiFied",
					"NotDarkCraft",
					"GoldenSkeppy",
					"IloveHappy",
					"GeniusPan",
					"Saucepan",
					"10msPing3r"


			)
	);

	public ArrayList<String> getJoinableWordNames() {
		return joinableWordNames;
	}
}
