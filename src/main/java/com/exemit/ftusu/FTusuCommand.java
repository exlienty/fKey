package com.exemit.ftusu;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FTusuCommand implements CommandExecutor, TabCompleter {
    private final ExemitFTusu plugin;

    public FTusuCommand(ExemitFTusu plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getMessages().getString("errors.player-only", "&cBu komut sadece oyuncular tarafından kullanılabilir!")));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            boolean isEnabled = plugin.getEnabledPlayers().contains(player.getUniqueId());
            String statusMessage = isEnabled ?
                    plugin.getMessages().getString("commands.ftusu.status.enabled", "&aF tuşu özelliği şu anda aktif.") :
                    plugin.getMessages().getString("commands.ftusu.status.disabled", "&cF tuşu özelliği şu anda devre dışı.");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', statusMessage));
            return true;
        }

        if (args.length == 1) {
            String arg = args[0].toLowerCase();
            if (arg.equals("on")) {
                plugin.getEnabledPlayers().add(player.getUniqueId());
                plugin.saveEnabledPlayersToFile();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getMessages().getString("commands.ftusu.enabled", "&aF tuşu özelliği aktif edildi!")));
                return true;
            } else if (arg.equals("off")) {
                plugin.getEnabledPlayers().remove(player.getUniqueId());
                plugin.saveEnabledPlayersToFile();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getMessages().getString("commands.ftusu.disabled", "&cF tuşu özelliği devre dışı bırakıldı!")));
                return true;
            } else if (arg.equals("toggle")) {
                if (plugin.getEnabledPlayers().contains(player.getUniqueId())) {
                    plugin.getEnabledPlayers().remove(player.getUniqueId());
                    plugin.saveEnabledPlayersToFile();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.getMessages().getString("commands.ftusu.disabled", "&cF tuşu özelliği devre dışı bırakıldı!")));
                } else {
                    plugin.getEnabledPlayers().add(player.getUniqueId());
                    plugin.saveEnabledPlayersToFile();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.getMessages().getString("commands.ftusu.enabled", "&aF tuşu özelliği aktif edildi!")));
                }
                return true;
            }
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                plugin.getMessages().getString("commands.ftusu.usage", "&cKullanım: /ftusu [on/off/toggle]")));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("on");
            completions.add("off");
            completions.add("toggle");
        }

        return completions;
    }
}
