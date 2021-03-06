package wtf.choco.veinminer.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import wtf.choco.veinminer.VeinMiner;
import wtf.choco.veinminer.pattern.VeinMiningPattern;
import wtf.choco.veinminer.tool.ToolCategory;

public class VeinMinerCmdTabCompleter implements TabCompleter {

	private final VeinMiner plugin;

	public VeinMinerCmdTabCompleter(VeinMiner plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		List<String> values = new ArrayList<>();
		if (args.length == 1) {
			values.add("version");
			if (sender.hasPermission("veinminer.reload")) {
				values.add("reload");
			}
			if (sender.hasPermission("veinminer.toggle")) {
				values.add("toggle");
			}
			if (hasBlocklistPerms(sender)) {
				values.add("blocklist");
			}
			if (sender.hasPermission("veinminer.pattern")) {
				values.add("pattern");
			}
		}

		else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("toggle") || args[0].equalsIgnoreCase("blocklist")) {
				for (ToolCategory category : ToolCategory.values()) {
					values.add(category.name().toLowerCase());
				}
			}

			else if (args[0].equalsIgnoreCase("pattern")) {
				values = plugin.getPatternRegistry().getPatterns().stream()
						.map(VeinMiningPattern::getKey).map(NamespacedKey::toString)
						.collect(Collectors.toList());
			}
		}

		else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("blocklist")) {
				if (sender.hasPermission("veinminer.blocklist.add")) {
					values.add("add");
				}
				if (sender.hasPermission("veinminer.blocklist.remove")) {
					values.add("remove");
				}
				if (sender.hasPermission("veinminer.blocklist.list.*")) {
					values.add("list");
				}
			}
		}
		else {
			return null;
		}

		return StringUtil.copyPartialMatches(args[args.length - 1], values, new ArrayList<>());
	}

	private boolean hasBlocklistPerms(CommandSender sender) {
		return sender.hasPermission("veinminer.blocklist.add")
			|| sender.hasPermission("veinminer.blocklist.remove")
			|| sender.hasPermission("veinminer.blocklist.list.*");
	}
}