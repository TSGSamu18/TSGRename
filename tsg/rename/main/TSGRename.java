package tsg.rename.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import tsg.rename.commands.CommandListener;
import tsg.rename.file.FileManager;
import tsg.rename.file.Messages;
import tsg.rename.utils.Utils;

public class TSGRename extends JavaPlugin {

	public static TSGRename instance;
	public YamlConfiguration config;
	public Logger log = this.getLogger();

	public Utils utils;
	public FileManager fileManager;
	public Messages messages;

	public String command;

	public List<String> bannedWord;
	public List<Material> bannedItem;
	public HashMap<String, Long> cooldowns;

	@Override
	public void onDisable() {
		utils.saveListOnDisable();
	}

	@Override
	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();
		config = (YamlConfiguration) this.getConfig();
		utils = new Utils();
		fileManager = new FileManager();
		messages = new Messages();
		bannedItem = new ArrayList<Material>();
		cooldowns = new HashMap<String, Long>();
		this.bannedWord = this.config.getStringList("Settings.bannedRenames");
		for (String s : this.config.getStringList("Settings.bannedItems")) {
			try {
				this.bannedItem.add(Material.valueOf(s));
			} catch (EnumConstantNotPresentException e) {
				this.log.severe("Material " + s + "not found!");
				this.log.severe("Skipping it...");
			}
		}
		fileManager.setup();
		messages.setup();
		utils.setup();
		command = this.config.getString("Command.Main");
		registerCommand(command, new CommandListener(command));
		utils.loadListOnEnable();
	}

	public void registerCommand(String fallback, Command command) {
		try {
			Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
			commandMap.register(fallback, command);
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
			this.log.severe("Error while registering commands:" + e.toString());
		}
	}

	public void reload() {
		this.reloadConfig();
		this.bannedWord = this.config.getStringList("Settings.bannedRenames");
		for (String s : this.config.getStringList("Settings.bannedItems")) {
			try {
				this.bannedItem.add(Material.valueOf(s));
			} catch (EnumConstantNotPresentException e) {
				this.log.severe("Material " + s + "not found!");
				this.log.severe("Skipping it...");
			}
		}
		fileManager.setup();
		messages.setup();
		utils.setup();
	}
}
