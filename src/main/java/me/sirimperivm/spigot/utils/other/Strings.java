package me.sirimperivm.spigot.utils.other;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.ConfigManager;

import java.util.List;

@SuppressWarnings("all")
public class Strings {

    private Main plugin;
    private Logger log;
    private ConfigManager configManager;

    public Strings(Main plugin) {
        this.plugin = plugin;
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
    }

    public String capitalize(String target) {
        return target.substring(0, 1).toUpperCase()+target.substring(1).toLowerCase();
    }

    public String formatNumber(double target) {
        int decimalsCount = configManager.getSettings().getInt("settings.number-formatter.max-decimals-count");
        List<String> associations = configManager.getSettings().getStringList("settings.number-formatter.numbers-associations-list");
        StringBuilder sb = new StringBuilder();
        String toReturn = String.valueOf(target);
        String currencyValue = null;

        for (String associationString : associations) {
            String[] splitter = associationString.split("-");
            if (splitter.length > 1 && splitter.length < 3) {
                double associationValue;
                try {
                    associationValue = Double.parseDouble(splitter[0]);
                } catch (NumberFormatException e) {
                    log.fail("L'associazione " + associationString + " non è valida: il valore di associazione non è numerico, procedo con le altre.");
                    continue;
                }

                if (target >= associationValue) {
                    toReturn = String.valueOf(target/associationValue);
                    currencyValue = splitter[1];
                }
            } else {
                log.fail("L'associazione " + associationString + " non è valida, procedo con le altre.");
                continue;
            }
        }

        if (toReturn.length() >= decimalsCount) {
            for (int i = 0; i < decimalsCount; i++) {
                if (toReturn.charAt(i) == '.') {
                    break;
                }
                sb.append(toReturn.charAt(i));
            }

            toReturn = sb.toString();
        }

        if (toReturn.contains(".0")) {
            toReturn = toReturn.replace(".0", "");
        }

        toReturn = toReturn+currencyValue;

        return toReturn;
    }
}
