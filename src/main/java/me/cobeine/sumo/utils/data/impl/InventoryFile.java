package me.cobeine.sumo.utils.data.impl;

import me.cobeine.sumo.Core;
import me.cobeine.sumo.utils.data.YamlFile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
//There is a high chance this class will be removed and be replaced with a hashmap< Player,Inventory? > instead
public class InventoryFile extends YamlFile {
    Player player;

    public InventoryFile(Player player) {
        super(player.getUniqueId().toString(), Core.getInstance().getDataFolder().getPath() +"/inventories");
        this.player = player;
    }

    @Override
    public void setDefaults() {
        ItemStack[] armor = player.getInventory().getArmorContents();
         getConfig().set("Armor", armor.length == 0 ? new ItemStack[]{} : armor);
        for (int i = 0; i < 35; i++) {
            ItemStack t = player.getInventory().getItem(i);
            if (t == null || t.getType().equals(Material.AIR))
                return;
            getConfig().set("Slots." + i, t);
            i++;
        }
        save();
        player.getInventory().clear();
    }

    public void load(Player player) {
        player.getInventory().setArmorContents((ItemStack[]) getConfig().get("Armor"));
        for (String slot : getConfig().getConfigurationSection("Slots").getKeys(false)) {
            player.getInventory().setItem(Integer.parseInt(slot), getConfig().getItemStack("Slots." + slot));
        }
        delete();
        this.player = null;
    }
}
