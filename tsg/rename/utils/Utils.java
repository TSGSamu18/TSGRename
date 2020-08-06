package tsg.rename.utils;

import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import tsg.rename.main.TSGRename;

public class Utils {

	public int cooldown;

	@SuppressWarnings("deprecation")
	public String getItemName(Player p) {
		return (p.getItemInHand().getItemMeta().getDisplayName() == null)
				? p.getItemInHand().getType().toString().replaceAll("_", " ")
				: p.getItemInHand().getItemMeta().getDisplayName();
	}

	public long getTime(Player p) {
		if (cooldown == 0) {
			return 0;
		}
		if (TSGRename.instance.cooldowns.containsKey(p.getName())) {
			long secondsLeft = ((TSGRename.instance.cooldowns.get(p.getName()) / 1000) + cooldown)
					- (System.currentTimeMillis() / 1000);
			if (secondsLeft > 0) {
				return secondsLeft;
			} else {
				TSGRename.instance.cooldowns.put(p.getName(), System.currentTimeMillis());
				return 0;
			}
		} else {
			TSGRename.instance.cooldowns.put(p.getName(), System.currentTimeMillis());
			return 0;
		}
	}

	public void loadListOnEnable() {
		if (cooldown == 0) {
			return;
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(TSGRename.instance.fileManager.saveList);
		for (String s : config.getKeys(false)) {
			TSGRename.instance.cooldowns.put(s, config.getLong(s));
		}
	}

	public void saveListOnDisable() {
		if (cooldown == 0) {
			return;
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(TSGRename.instance.fileManager.saveList);
		for (String s : TSGRename.instance.config.getKeys(false)) {
			config.set(s, null);
		}
		for (String s : TSGRename.instance.cooldowns.keySet()) {
			config.set(s, TSGRename.instance.cooldowns.get(s));
		}
		try {
			config.save(TSGRename.instance.fileManager.saveList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setup() {
		cooldown = TSGRename.instance.config.getInt("Settings.renameCooldown");
	}

	@SuppressWarnings("deprecation")
	public boolean validItem(Player p) {
		if (TSGRename.instance.bannedItem.contains(p.getItemInHand().getType())) {
			return false;
		}
		return true;
	}

	public String validTitle(String msg) {
		for (String c : TSGRename.instance.bannedWord) {
			if (msg.replaceAll(" ", "").toLowerCase().contains(c.toLowerCase())) {
				return c;
			}
		}
		return null;
	}
}
