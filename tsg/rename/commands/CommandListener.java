package tsg.rename.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import tsg.rename.main.TSGRename;

public class CommandListener extends Command {

	List<String> commandAliases;

	public CommandListener(String name) {
		super(name);
		commandAliases = new ArrayList<String>();
		fillCommandAliases();
		this.description = "Rename command!";
		this.setAliases(commandAliases);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean execute(CommandSender se, String s, String[] ar) {
		if (!(se instanceof Player)) {
			se.sendMessage(TSGRename.instance.messages.NOT_PLAYER);
			return true;
		}
		Player p = (Player) se;
		if (ar.length == 0) {
			if (!p.hasPermission("tsgrename.help")) {
				p.sendMessage(TSGRename.instance.messages.NO_PERMISSION);
				return true;
			}
			for (String h : TSGRename.instance.messages.HELP) {
				p.sendMessage(h.replaceAll("&", "§").replaceAll("%prefix%", TSGRename.instance.messages.PREFIX));
			}
		} else if (ar.length == 1) {
			if (ar[0].equalsIgnoreCase("help")) {
				if (!p.hasPermission("tsgrename.help")) {
					p.sendMessage(TSGRename.instance.messages.NO_PERMISSION);
					return true;
				}
				for (String h : TSGRename.instance.messages.HELP) {
					p.sendMessage(h.replaceAll("&", "§").replaceAll("%prefix%", TSGRename.instance.messages.PREFIX));
				}
			} else if (ar[0].equalsIgnoreCase("reload")) {
				if (!p.hasPermission("tsgrename.reload")) {
					p.sendMessage(TSGRename.instance.messages.NO_PERMISSION);
					return true;
				}
				TSGRename.instance.reload();
				se.sendMessage(TSGRename.instance.messages.ON_RELOAD);
			} else {
				p.sendMessage(TSGRename.instance.messages.INCORRECT_USAGE);
			}
		} else {
			if (ar[0].equalsIgnoreCase("rename")) {
				if (!p.hasPermission("tsgrename.rename")) {
					p.sendMessage(TSGRename.instance.messages.NO_PERMISSION);
					return true;
				}
				if (TSGRename.instance.utils.getTime(p) > 0) {
					p.sendMessage(TSGRename.instance.messages.WAIT.replaceAll("%time%",
							String.valueOf(TSGRename.instance.utils.getTime(p))));
					return true;
				}
				try {
					StringBuilder build = new StringBuilder();
					for (int i = 1; i < ar.length; i++) {
						build.append(ar[i]);
					}
					String title = build.toString().replaceAll("&", "§");
					if (!TSGRename.instance.utils.validItem(p)) {
						p.sendMessage(TSGRename.instance.messages.ITEM_NOT_VALID.replaceAll("%item%",
								TSGRename.instance.utils.getItemName(p)));
						return true;
					}
					if (TSGRename.instance.utils.validTitle(title) != null) {
						p.sendMessage(TSGRename.instance.messages.TITLE_NOT_VALID.replaceAll("%invalid%",
								TSGRename.instance.utils.validTitle(title)));
						return true;
					}
					ItemMeta m = p.getItemInHand().getItemMeta();
					m.setDisplayName(title);
					p.getItemInHand().setItemMeta(m);
					p.updateInventory();
					p.sendMessage(TSGRename.instance.messages.ON_RENAME.replaceAll("%title%", title));
				} catch (NullPointerException e) {
					p.sendMessage(TSGRename.instance.messages.NOT_ITEM_IN_HAND);
				}
			} else if (ar[0].equalsIgnoreCase("addlore")) {
				if (!p.hasPermission("tsgrename.addlore")) {
					p.sendMessage(TSGRename.instance.messages.NO_PERMISSION);
					return true;
				}
				StringBuilder build = new StringBuilder();
				for (int i = 1; i < ar.length; i++) {
					build.append((i != ar.length) ? ar[i] + " " : ar[i]);
				}
				String newline = build.toString().replaceAll("&", "§");
				try {
					ItemMeta m = p.getItemInHand().getItemMeta();
					List<String> lore = m.getLore();
					lore.add(newline);
					m.setLore(lore);
					p.getItemInHand().setItemMeta(m);
					p.updateInventory();
					p.sendMessage(TSGRename.instance.messages.ON_LORE_ADD.replaceAll("%newline%", newline));
				} catch (NullPointerException e) {
					p.sendMessage(TSGRename.instance.messages.NOT_ITEM_IN_HAND);
				}
			} else if (ar[0].equalsIgnoreCase("setlore")) {
				if (!p.hasPermission("tsgrename.setlore")) {
					p.sendMessage(TSGRename.instance.messages.NO_PERMISSION);
					return true;
				}
				StringBuilder build = new StringBuilder();
				List<String> lore = new ArrayList<String>();
				for (int i = 1; i < ar.length; i++) {
					build.append((i != ar.length) ? ar[i] + " " : ar[i]);
				}
				String[] args = build.toString().split("%n%");
				for (String l : args) {
					lore.add(l.replaceAll("&", "§").replaceAll("%n%", ""));
				}
				try {
					ItemMeta m = p.getItemInHand().getItemMeta();
					m.setLore(lore);
					p.getItemInHand().setItemMeta(m);
					p.updateInventory();
					p.sendMessage(TSGRename.instance.messages.ON_LORE_SET.replaceAll("%lore%",
							build.toString().replaceAll("%n%", " | ").replaceAll("&", "§")));
				} catch (NullPointerException e) {
					p.sendMessage(TSGRename.instance.messages.NOT_ITEM_IN_HAND);
				}
			}
		}
		return true;
	}

	public void fillCommandAliases() {
		if (TSGRename.instance.config.isList("Command.Aliases")) {
			for (String alias : TSGRename.instance.config.getStringList("Command.Aliases")) {
				commandAliases.add(alias);
			}
		} else {
			TSGRename.instance.log.severe("Error commands alias not found");
		}
	}
}