package me.sirimperivm.spigot;

import me.sirimperivm.spigot.commands.AdminCommand;
import me.sirimperivm.spigot.commands.UserCommand;
import me.sirimperivm.spigot.extras.dependencies.VaultAPI;
import me.sirimperivm.spigot.extras.entities.Gui;
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
    private Logger log;
    private ConfigManager configManager;
    private Strings strings;
    private Gui guis;
    private Errors errors;
    private ModuleManager moduleManager;
    private VaultAPI vaultAPI;

    private void setupDependencies() {
        vaultAPI = new VaultAPI(plugin);
    }

    @Override
    public void onEnable() {
        plugin = this;
        colors = new Colors(plugin);
        log = new Logger(plugin, "MonstersInAPocket");
        configManager = new ConfigManager(plugin);
        strings = new Strings(plugin);
        guis = new Gui(plugin);
        errors = new Errors(plugin);
        moduleManager = new ModuleManager(plugin);
        setupDependencies();

        getCommand("pocketmonstersadmin").setExecutor(new AdminCommand(plugin));
        getCommand("pocketmonstersadmin").setTabCompleter(new AdminCommand(plugin));
        getCommand("pocketmonsters").setExecutor(new UserCommand(plugin));
        getCommand("pocketmonsters").setTabCompleter(new UserCommand(plugin));

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

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Errors getErrors() {
        return errors;
    }

    public Gui getGuis() {
        return guis;
    }

    public Logger getLog() {
        return log;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public Strings getStrings() {
        return strings;
    }

    public VaultAPI getVaultAPI() {
        return vaultAPI;
    }
}
