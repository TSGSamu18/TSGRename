package tsg.rename.file;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import tsg.rename.main.TSGRename;

public class Messages {

	YamlConfiguration messages;

	public String PREFIX;

	public String NOT_PLAYER;
	public String NOT_ITEM_IN_HAND;
	public String NO_PERMISSION;
	public String TITLE_NOT_VALID;
	public String ITEM_NOT_VALID;
	public String INCORRECT_USAGE;
	public String WAIT;
	public String ON_RELOAD;
	public String ON_LORE_SET;
	public String ON_LORE_ADD;
	public String ON_RENAME;
	public List<String> HELP;

	public void setup() {
		messages = TSGRename.instance.config;
		PREFIX = messages.getString("Messages.prefix").replaceAll("&", "§");
		NOT_PLAYER = traslator(messages.getString("Messages.not-player"));
		ON_RELOAD = traslator(messages.getString("Messages.on-reload"));
		NOT_ITEM_IN_HAND = traslator(messages.getString("Messages.not-item-in-hand"));
		NO_PERMISSION = traslator(messages.getString("Messages.no-permission"));
		INCORRECT_USAGE = traslator(messages.getString("Messages.incorrect-usage"));
		TITLE_NOT_VALID = traslator(messages.getString("Messages.title-not-valid"));
		ITEM_NOT_VALID = traslator(messages.getString("Messages.item-not-valid"));
		WAIT = traslator(messages.getString("Messages.wait"));
		ON_RENAME = traslator(messages.getString("Messages.on-rename"));
		ON_LORE_ADD = traslator(messages.getString("Messages.on-lore-add"));
		ON_LORE_SET = traslator(messages.getString("Messages.on-lore-set"));
		HELP = messages.getStringList("Messages.help");
	}

	public String traslator(String msg) {
		return msg.replaceAll("&", "§").replaceAll("%prefix%", PREFIX);
	}
}
