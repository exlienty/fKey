package com.exemit.ftusu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ExemitFTusu extends JavaPlugin implements Listener {
    private FileConfiguration config;
    private FileConfiguration messages;
    private Set<UUID> enabledPlayers;
    private static ExemitFTusu instance;

    private File ftusuDataFile;
    private FileConfiguration ftusuDataConfig;

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        saveDefaultConfig();
        config = getConfig();

        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        // F tuşu data dosyasını yükle
        loadFtusuData();

        // Aktif oyuncuları yedekten yükle
        enabledPlayers = new HashSet<>();
        loadEnabledPlayersFromFile();

        getServer().getPluginManager().registerEvents(this, this);

        FTusuCommand ftusuCommand = new FTusuCommand(this);
        getCommand("ftusu").setExecutor(ftusuCommand);
        getCommand("ftusu").setTabCompleter(ftusuCommand);
        getCommand("ftusureload").setExecutor(new ReloadCommand(this));

        setupPlaceholderAPI();
        showStartupMessage();
    }

    @Override
    public void onDisable() {
        saveEnabledPlayersToFile();
    }

    private void setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getScheduler().runTask(this, () -> {
                try {
                    FTusuPlaceholder placeholder = new FTusuPlaceholder(this);
                    if (placeholder.register()) {
                        getLogger().info("PlaceholderAPI entegrasyonu başarıyla yüklendi!");
                    } else {
                        getLogger().warning("PlaceholderAPI entegrasyonu kaydedilemedi!");
                    }
                } catch (Exception e) {
                    getLogger().warning("PlaceholderAPI entegrasyonu yüklenirken bir hata oluştu: " + e.getMessage());
                }
            });
        } else {
            getLogger().warning("PlaceholderAPI bulunamadı! Bu eklenti için gereklidir.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void showStartupMessage() {
        String title = ChatColor.translateAlternateColorCodes('&', config.getString("startup.title", "&6ᴇ&fғ&6ᴛᴜşᴜ"));
        String subtitle = ChatColor.translateAlternateColorCodes('&', config.getString("startup.subtitle", "&7ᴍᴀᴅᴇ ʙʏ ᴇxʟɪᴇɴᴛʏ"));

        String asciiArt = "\n" +
                "  ███████╗███████╗████████╗██╗   ██╗███████╗██╗   ██╗\n" +
                "  ██╔════╝██╔════╝╚══██╔══╝██║   ██║██╔════╝██║   ██║\n" +
                "  █████╗  █████╗     ██║   ██║   ██║███████╗██║   ██║\n" +
                "  ██╔══╝  ██╔══╝     ██║   ██║   ██║╚════██║██║   ██║\n" +
                "  ███████╗██║        ██║   ╚██████╔╝███████║╚██████╔╝\n" +
                "  ╚══════╝╚═╝        ╚═╝    ╚═════╝ ╚══════╝ ╚═════╝ \n";

        getLogger().info(asciiArt);
        getLogger().info("Made by exlienty");
    }

    @EventHandler
    public void onFKeyPress(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (enabledPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            String command = config.getString("command", "spawn");
            Bukkit.dispatchCommand(player, command);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    messages.getString("success.command-executed", "&aKomut başarıyla çalıştırıldı!")));
        }
    }

    public static ExemitFTusu getInstance() {
        return instance;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public Set<UUID> getEnabledPlayers() {
        return enabledPlayers;
    }

    public void reloadConfigs() {
        reloadConfig();
        config = getConfig();

        File messagesFile = new File(getDataFolder(), "messages.yml");
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    private void loadFtusuData() {
        ftusuDataFile = new File(getDataFolder(), "ftusu-data.yml");
        if (!ftusuDataFile.exists()) {
            try {
                ftusuDataFile.createNewFile();
            } catch (IOException e) {
                getLogger().warning("ftusu-data.yml oluşturulamadı!");
            }
        }
        ftusuDataConfig = YamlConfiguration.loadConfiguration(ftusuDataFile);
    }

    private void loadEnabledPlayersFromFile() {
        if (ftusuDataConfig.contains("enabled")) {
            for (String uuidStr : ftusuDataConfig.getStringList("enabled")) {
                try {
                    enabledPlayers.add(UUID.fromString(uuidStr));
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Geçersiz UUID: " + uuidStr);
                }
            }
        }
    }

    void saveEnabledPlayersToFile() {
        Set<String> uuidStrings = new HashSet<>();
        for (UUID uuid : enabledPlayers) {
            uuidStrings.add(uuid.toString());
        }
        ftusuDataConfig.set("enabled", new java.util.ArrayList<>(uuidStrings));
        try {
            ftusuDataConfig.save(ftusuDataFile);
        } catch (IOException e) {
            getLogger().warning("enabledPlayers verileri kaydedilemedi!");
        }
    }
}
