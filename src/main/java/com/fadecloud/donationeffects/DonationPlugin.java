package com.fadecloud.donationeffects;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class DonationPlugin extends JavaPlugin implements Listener, CommandExecutor {

    private static DonationPlugin instance;
    private boolean isActive;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("donation").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    public static DonationPlugin getInstance() {
        return instance;
    }

    @EventHandler
    public void onEvent(AsyncPlayerChatEvent event) {
        if (isActive) {
            String message = event.getMessage();
            for (String filter : DonationSettings.getFilterMessages()) {
                if (filter.equalsIgnoreCase(message)) {
                    event.setMessage(DonationSettings.getRandomGG());
                    break;
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && sender.hasPermission("donation.admin")) {
            String name = args[0];
            DonationSettings.sendWaveStartMessage(name);

            if (!isActive) {
                isActive = true;
                Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
                    isActive = false;
                    DonationSettings.sendWaveEndMessage();
                }, DonationSettings.getWaveDuration());
            }
        }
        return true;
    }
}
