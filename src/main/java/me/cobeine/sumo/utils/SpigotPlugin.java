/**
 * MIT License
 *
 * Copyright (c) 2022 Cobeine
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. YOU ARE NOT ALLOWED TO RE-DISTRIBUTE AND/OR REPUBLISH.
 */
package me.cobeine.sumo.utils;

import me.cobeine.sumo.Core;
import me.cobeine.sumo.commands.Command;
import me.cobeine.sumo.utils.data.impl.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class SpigotPlugin extends JavaPlugin {
    public static final ItemStack leave_item = leaveItem();
    public void onEnable() {
        log(
                "Starting " + getDescription().getName() +
                        " Version v" + getDescription().getVersion() +
                        " By " + getDescription().getAuthors().get(0));
        init();
        registerCommands();
        registerListeners();
    }

    public void onDisable() {
        shutdown();
    }

    protected abstract void init();

    protected abstract void registerCommands();

    public void registerCommand(Command clazz) {
        Bukkit.getPluginCommand(clazz.getName()).setExecutor(clazz);
        Bukkit.getPluginCommand(clazz.getName()).setTabCompleter(clazz.getTabCompleter());
    }

    protected abstract void registerListeners();

    public void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener,this);
        }
    }

    protected abstract void shutdown();

    static ItemStack leaveItem() {
        ItemStack stack = new ItemStack(Material.BARRIER);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cLeave"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        stack.setItemMeta(meta);
        return stack;
    }

    public static String getConfigString(String key) {
        return ConfigFile.getInstance().getConfig().getString(key);
    }

    public static boolean getConfigBoolean(String key) {
        return ConfigFile.getInstance().getConfig().getBoolean(key);
    }

    public static int getConfigInt(String key) {
        return ConfigFile.getInstance().getConfig().getInt(key);
    }
    public static void log(String info) {
        Bukkit.getServer().getLogger().info(info);
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createDataFolder() {
        Core.getInstance().getDataFolder().mkdirs();
        new File(Core.getInstance().getDataFolder().getPath() + "/inventories").mkdirs();
    }

}
