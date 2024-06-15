package me.sirimperivm.spigot.extras.dependencies;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.other.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

@SuppressWarnings("all")
public class VaultAPI {

    private Main plugin;
    private Logger log;

    private static Economy econ = null;

    public VaultAPI(Main plugin) {
        this.plugin = plugin;
        log = plugin.getLog();

        if (!setupEconomy()) {
            log.fail("Non è stato possibile implementare la libreria di vault, il plugin si disattiverà.");
            plugin.onDisable();
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }
}
