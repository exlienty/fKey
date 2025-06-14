package com.exemit.ftusu;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FTusuPlaceholder extends PlaceholderExpansion {
    private final ExemitFTusu plugin;

    public FTusuPlaceholder(ExemitFTusu plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "ftusu";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Exemit";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        // Configden değerleri al
        String statusOpened = plugin.getConfig().getString("ftusu.status_opened", "Açılmış ✓");
        String statusClosed = plugin.getConfig().getString("ftusu.status_closed", "Kapatılmış ×");

        // Parametreye göre sonucu döndür
        if (params.equalsIgnoreCase("status")) {
            return plugin.getEnabledPlayers().contains(player.getUniqueId()) ? statusOpened : statusClosed;
        }

        return null;
    }
}
