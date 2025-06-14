package com.exemit.ftusu.commands;

import com.exemit.ftusu.ExemitFTusu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FTusuCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cBu komutu sadece oyuncular kullanabilir!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("ftusu.toggle")) {
            player.sendMessage("§cBu komutu kullanmak için yetkiniz yok!");
            return true;
        }

        if (args.length == 0) {
            boolean currentState = ExemitFTusu.getInstance().getConfig().getBoolean("players." + player.getUniqueId(), true);
            ExemitFTusu.getInstance().getConfig().set("players." + player.getUniqueId(), !currentState);
            ExemitFTusu.getInstance().saveConfig();
            player.sendMessage("§aF tuşu özelliği " + (!currentState ? "§aaktif" : "§cdeaktif") + " §aedildi!");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                ExemitFTusu.getInstance().getConfig().set("players." + player.getUniqueId(), true);
                ExemitFTusu.getInstance().saveConfig();
                player.sendMessage("§aF tuşu özelliği §aaktif §aedildi!");
                return true;
            } else if (args[0].equalsIgnoreCase("off")) {
                ExemitFTusu.getInstance().getConfig().set("players." + player.getUniqueId(), false);
                ExemitFTusu.getInstance().saveConfig();
                player.sendMessage("§aF tuşu özelliği §cdeaktif §aedildi!");
                return true;
            }
        }

        player.sendMessage("§cKullanım: /ftusu [on/off]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.add("on");
            completions.add("off");
            return completions;
        }
        
        return completions;
    }
} 