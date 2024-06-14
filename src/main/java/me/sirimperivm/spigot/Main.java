package me.sirimperivm.spigot;

import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.other.Errors;
import me.sirimperivm.spigot.utils.other.Logger;
import me.sirimperivm.spigot.utils.other.Strings;
import org.apache.commons.lang3.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getVersion;

@SuppressWarnings("all")
public final class Main extends JavaPlugin {

    private Main plugin;
    private Colors colors;
    private Strings strings;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;

    @Override
    public void onEnable() {
        plugin = this;
        colors = new Colors(plugin);
        strings = new Strings(plugin);
        log = new Logger(plugin, "MonstersInAPocket");
        configManager = new ConfigManager(plugin);
        errors = new Errors(plugin);

        log.success("Plugin attivato correttamente!");
    }

    @Override
    public void onDisable() {
        log.success("Plugin disattivato correttamente!");
    }

    public Main getPlugin() {
        return plugin;
    }

    public int getServerVersion() {
        String version = getVersion();
        Validate.notEmpty(version, "Impossibile ottenere la versione principale di Minecraft da una stringa nulla o vuota");

        int index = version.lastIndexOf("MC:");
        if (index != -1) {
            version = version.substring(index + 4, version.length() -1);
        } else if (version.endsWith("SNAPSHOT")) {
            index = version.indexOf('-');
            version = version.substring(0, index);
        }

        int lastDot = version.lastIndexOf('.');
        if (version.indexOf('.') != lastDot) version = version.substring(0, lastDot);

        return Integer.parseInt(version.substring(2));
    }

    public Colors getColors() {
        return colors;
    }

    public Strings getStrings() {
        return strings;
    }

    public Logger getLog() {
        return log;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Errors getErrors() {
        return errors;
    }
}
