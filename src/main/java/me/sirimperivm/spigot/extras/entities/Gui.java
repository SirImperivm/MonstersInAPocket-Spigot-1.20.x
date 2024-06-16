package me.sirimperivm.spigot.extras.entities;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.other.Logger;
import me.sirimperivm.spigot.utils.other.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("all")
public class Gui {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Strings strings;

    private String inventoryName;
    private String inventoryTitle;
    private int inventoryRows;
    private int inventorySize;

    public Gui(Main plugin, String inventoryName) {
        this.plugin = plugin;
        this.inventoryName = inventoryName;
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        strings = plugin.getStrings();

        inventoryTitle = configManager.getTranslatedString(configManager.getGuis(), inventoryName + ".title");
        inventoryRows = configManager.getGuis().getInt(inventoryName + ".rows");
        inventoryRows = inventoryRows > 0 ? inventoryRows : 1;
        inventoryRows = inventoryRows < 7 ? inventoryRows : 6;
        inventorySize = inventoryRows*9;
    }

    private Inventory constructInventory() {
        Inventory inventory = Bukkit.createInventory(null, inventorySize, inventoryTitle);

        boolean fillerEnabled = configManager.getGuis().getBoolean(inventoryName + ".filler.enabled");

        ItemStack filler = new ItemStack(Material.getMaterial(configManager.getGuis().getString(inventoryName + ".filler.material")));
        ItemMeta fillerMeta = filler.getItemMeta();
        String fillerDisplay = configManager.getGuis().getString(inventoryName + ".filler.display");
        if (!fillerDisplay.equals("null")) {
            fillerMeta.setDisplayName(colors.translateString(fillerDisplay));
        }
        fillerMeta.setLore(configManager.getTranslatedList(configManager.getGuis(), inventoryName + ".filler.lore"));
        if (configManager.getGuis().getBoolean(inventoryName + ".filler.glowing")) {
            fillerMeta.addEnchant(Enchantment.LURE, 0, true);
            fillerMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        fillerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        fillerMeta.setCustomModelData(configManager.getGuis().getInt(inventoryName + ".filler.model"));
        filler.setItemMeta(fillerMeta);

        for (String itemKey : configManager.getGuis().getConfigurationSection(inventoryName + ".items").getKeys(false)) {
            String path = inventoryName + ".items." + itemKey;

            List<Integer> itemSlots = configManager.getGuis().getIntegerList(path + ".slots");
            ItemStack itemStack = new ItemStack(Material.getMaterial(configManager.getGuis().getString(path + ".material")), configManager.getGuis().getInt(path + ".displayed-amount"));
            ItemMeta itemMeta = itemStack.getItemMeta();
            String displayName = configManager.getGuis().getString(path + ".display");
            if (!displayName.equals("null")) {
                itemMeta.setDisplayName(colors.translateString(displayName));
            }
            double modifier = 0.0;

            String clickAction = configManager.getGuis().getString(path + ".settings.click-action");
            double itemPrice = 0;
            String formattedPrice = null;
            if (clickAction != null) {
                if (clickAction.equalsIgnoreCase("BUY_POCKET") || clickAction.equalsIgnoreCase("CLOSE_MENU")) {
                    if (clickAction.equalsIgnoreCase("BUY_POCKET")) {
                        itemPrice = configManager.getGuis().getDouble(path + ".settings.price");
                        int boughtQuantity = configManager.getGuis().getInt(path + ".settings.quantity");
                        String modifiedQuantity = "1." + boughtQuantity;
                        modifier = Double.parseDouble(modifiedQuantity);

                        formattedPrice = strings.formatNumber(itemPrice);
                        HashMap<String, String> replacements = new HashMap<>();
                        replacements.put("{price}", formattedPrice);
                        replacements.put("{click-action}", clickAction);
                        replacements.put("{modifier}", modifiedQuantity);

                        itemMeta.setLore(configManager.getTranslatedList(configManager.getGuis(), path + ".lore", replacements));
                    } else if (clickAction.equalsIgnoreCase("CLOSE_MENU")) {
                        modifier = 5.0;
                        String modifiedQuantity = String.valueOf(modifier);

                        HashMap<String, String> replacements = new HashMap<>();
                        replacements.put("{price}", "N/A");
                        replacements.put("{click-action}", clickAction);
                        replacements.put("{modifier}", modifiedQuantity);

                        itemMeta.setLore(configManager.getTranslatedList(configManager.getGuis(), path + ".lore", replacements));
                    } else {
                        log.fail("l'impostazione " + clickAction + " dell'oggetto nella gui " + inventoryName + " alla sezione " + path + " non è stato riconosciuto. Continuo con gli altri oggetti.");
                        continue;
                    }
                }
            } else {
                log.fail("Impossibile modificare l'oggetto della gui: " + inventoryName + " all'indirizzo: " + path + "; non è stata impostata la sezione settings.click-action! Continuo con il resto degli oggetti.");
                continue;
            }

            if (configManager.getGuis().getBoolean(path + ".glowing")) {
                itemMeta.addEnchant(Enchantment.LURE, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            Attribute attribute = Attribute.ZOMBIE_SPAWN_REINFORCEMENTS;
            itemMeta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attribute.name(), modifier, AttributeModifier.Operation.MULTIPLY_SCALAR_1));

            itemMeta.setCustomModelData(configManager.getGuis().getInt(path + ".model"));;
            itemStack.setItemMeta(itemMeta);

            for (Integer slot : itemSlots) {
                inventory.setItem(slot, itemStack);
            }
        }

        if (fillerEnabled) {
            for (int slot=0; slot<inventorySize; slot++) {
                if (inventory.getItem(slot) == null || inventory.getItem(slot).getType() == Material.AIR) {
                    inventory.setItem(slot, filler);
                }
            }
        }

        return inventory;
    }

    public void playerOpenInventory(Player target) {
        target.openInventory(constructInventory());
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public int getInventoryRows() {
        return inventoryRows;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public String getInventoryTitle() {
        return inventoryTitle;
    }
}
