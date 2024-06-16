package me.sirimperivm.spigot.commands;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.ModuleManager;
import me.sirimperivm.spigot.extras.dependencies.VaultAPI;
import me.sirimperivm.spigot.extras.entities.Gui;
import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.other.Errors;
import me.sirimperivm.spigot.utils.other.Logger;
import me.sirimperivm.spigot.utils.other.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class UserCommand implements CommandExecutor, TabExecutor {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Strings strings;
    private Errors errors;
    private ModuleManager moduleManager;
    private VaultAPI vaultAPI;

    public UserCommand(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        strings = plugin.getStrings();
        errors = plugin.getErrors();
        moduleManager = plugin.getModuleManager();
        vaultAPI = plugin.getVaultAPI();
    }

    private void getUsage(CommandSender s, int page) {
        moduleManager.createHelp(s, "user-command", page);
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.user.main"))) {
            return true;
        } else {
            if (a.length == 0) {
                getUsage(s, 1);
            } else if (a.length == 1) {
                if (a[0].equalsIgnoreCase("shop")) {
                    if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.user.shop.open"))) {
                        return true;
                    } else {
                        if (errors.noConsole(s)) {
                            return true;
                        } else {
                            Player p = (Player) s;
                            Gui shopGui = new Gui(plugin, "shop");
                            shopGui.playerOpenInventory(p);
                        }
                    }
                } else {
                    getUsage(s, 1);
                }
            } else if (a.length == 2) {
                if (a[0].equalsIgnoreCase("help")) {
                    if (moduleManager.containsOnlyNumbers(a[1])) {
                        getUsage(s, Integer.parseInt(a[1]));
                    } else {
                        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "invalid-args.only-numbers-allowed")
                                .replace("{arg}", a[1])
                        );
                    }
                } else {
                    getUsage(s, 1);
                }
            } else {
                getUsage(s, 1);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        if (a.length == 1) {
            List<String> toReturn = new ArrayList<>();
            if (s.hasPermission(configManager.getSettings().getString("permissions.commands.user.main"))) {
                toReturn.add("help");
                if (s instanceof Player && s.hasPermission(configManager.getSettings().getString("permissions.commands.user.shop"))) {
                    toReturn.add("shop");
                }
            }
            return toReturn;
        } else if (a.length == 2) {
            List<String> toReturn = new ArrayList<>();
            if (s.hasPermission(configManager.getSettings().getString("permissions.commands.user.main"))) {
                if (a[0].equalsIgnoreCase("help")) {
                    toReturn.add("[page]");
                }
            }
            return toReturn;
        }
        return new ArrayList<>();
    }
}
