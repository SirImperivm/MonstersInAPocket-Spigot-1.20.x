package me.sirimperivm.spigot;

import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.other.Logger;
import me.sirimperivm.spigot.utils.other.Strings;
import org.bukkit.command.CommandSender;

import java.util.List;

@SuppressWarnings("all")
public class ModuleManager {

    private Main plugin;
    private Colors colors;
    private Strings strings;
    private Logger log;
    private ConfigManager configManager;

    public ModuleManager(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        strings = plugin.getStrings();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
    }

    public void createHelp(CommandSender s, String helpTarget, int page) {
        int visualizedPage = page;
        page--;

        List<String> totalLines = configManager.getMessages().getStringList("helps-creator." + helpTarget + ".lines");
        int commandsPerPage = configManager.getMessages().getInt("helps-creator." + helpTarget + ".max-lines-per-page");
        int startIndex = page*commandsPerPage;
        int totalCommands = totalLines.size();
        int endIndex = Math.min((page+1) * commandsPerPage, totalCommands);

        if (visualizedPage <= 0 || visualizedPage > (int) Math.floor((double) totalCommands/commandsPerPage)+1) {
            s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"page-not-found")
                    .replace("{page}", String.valueOf(visualizedPage))
            );
            return;
        }

        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".header"));
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".title"));
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".spacer"));

        for (int i=startIndex; i<endIndex; i++) {
            String line = totalLines.get(i);
            if (line != null) {
                String[] parts = line.split("-");
                if (parts.length == 2) {
                    String commandName = parts[0].trim();
                    String commandDescription = parts[1].trim();
                    s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".line-format")
                            .replace("{command-name}", colors.translateString(commandName))
                            .replace("{command-description}", colors.translateString(commandDescription))
                    );
                }
            }
        }

        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".spacer"));
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".page-format")
                .replace("{currentpage}", String.valueOf(visualizedPage))
        );
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".footer"));
    }

    public boolean containsOnlyNumbers(String target) {
        try {
            Integer.parseInt(target);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
