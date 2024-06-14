package me.sirimperivm.spigot;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public final class Main extends JavaPlugin {

    private Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Main getPlugin() {
        return plugin;
    }
}
