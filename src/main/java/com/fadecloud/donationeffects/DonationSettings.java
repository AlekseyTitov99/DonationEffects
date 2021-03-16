package com.fadecloud.donationeffects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DonationSettings {

    private static final int waveDuration;
    private static final List<String> ggMessage;
    private static final List<String> waveStartMessages;
    private static final List<String> waveEndMessages;
    private static final List<String> filterMessages;

    static {
        FileConfiguration config = DonationPlugin.getInstance().getConfig();
        filterMessages = config.getStringList("Filter");
        waveDuration = config.getInt("Duration") * 20;
        ggMessage = config.getStringList("GG").stream().map(DonationSettings::color).collect(Collectors.toList());
        waveStartMessages = config.getStringList("WaveStart").stream().map(DonationSettings::color).collect(Collectors.toList());
        waveEndMessages = config.getStringList("WaveEnded").stream().map(DonationSettings::color).collect(Collectors.toList());
    }

    public static String getRandomGG() {
        return ggMessage.get(ThreadLocalRandom.current().nextInt(ggMessage.size()));
    }

    public static void sendWaveStartMessage(String name) {
        List<String> messages = new ArrayList<>(waveStartMessages).stream()
                .map(line -> line.replace("%player%", name))
                .collect(Collectors.toList());

        Bukkit.getOnlinePlayers().forEach(p -> messages.forEach(line -> p.sendMessage(line)));
    }

    public static void sendWaveEndMessage() {
        Bukkit.getOnlinePlayers().forEach(p -> waveEndMessages.forEach(line -> p.sendMessage(line)));
    }

    public static List<String> getFilterMessages() {
        return filterMessages;
    }

    public static int getWaveDuration() {
        return waveDuration;
    }

    private static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
