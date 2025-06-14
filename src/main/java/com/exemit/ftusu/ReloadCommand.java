package com.exemit.ftusu;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final ExemitFTusu plugin;

    public ReloadCommand(ExemitFTusu plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("eftusu.reload")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                plugin.getMessages().getString("errors.no-permission", "&cBu komutu kullanmak için yetkiniz yok!")));
            return true;
        }

        try {
            plugin.reloadConfigs();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                plugin.getMessages().getString("commands.reload.success", "&aEklenti başarıyla yeniden yüklendi!")));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                plugin.getMessages().getString("commands.reload.error", "&cEklenti yeniden yüklenirken bir hata oluştu!")));
            e.printStackTrace();
        }

        return true;
    }
} 