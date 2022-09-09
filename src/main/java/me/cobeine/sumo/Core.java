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
 * SOFTWARE. YOU ARE NOT ALLOWED TO RE-DISTRIBUTE AND/OR REPUBLISH. YOU ARE NOT ALLOWED TO FORK
 * UNLESS GIVEN CREDIT TO THE ORIGINAL AUTHOR (COBEINE)
 */
package me.cobeine.sumo;

import lombok.Getter;
import me.cobeine.sumo.listeners.OtherListeners;
import me.cobeine.sumo.listeners.LosingListeners;
import me.cobeine.sumo.managers.GameManager;
import me.cobeine.sumo.managers.InventorySaver;
import me.cobeine.sumo.managers.impl.InventoryManager;
import me.cobeine.sumo.managers.impl.SumoManager;
import me.cobeine.sumo.commands.impl.QuickSumoCommand;
import me.cobeine.sumo.utils.SpigotPlugin;
import me.cobeine.sumo.utils.data.YamlFile;
import me.cobeine.sumo.utils.data.impl.ConfigFile;
import me.cobeine.sumo.utils.data.impl.LocationsFile;
import me.cobeine.sumo.utils.metrics.MetricsImpl;

@Getter
public class Core extends SpigotPlugin {
    private static Core instance;
    private GameManager gameManager;
    private MetricsImpl metrics;
    private YamlFile configFile, locationsFile;
    private QuickSumoCommand quickSumoCommand;
    private InventorySaver inventorySaver;



    @Override
    protected void init() {
        instance = this;
        createDataFolder();
        configFile = new ConfigFile();
        locationsFile = new LocationsFile();
        gameManager = new SumoManager();
        quickSumoCommand = new QuickSumoCommand("QuickSumo");
        inventorySaver = new InventoryManager();
        if (getConfigBoolean("Settings.metrics"))
            metrics = new MetricsImpl(this);
    }


    @Override
    protected void registerCommands() {
        registerCommand(quickSumoCommand);
    }

    @Override
    protected void registerListeners() {
        registerListeners(new LosingListeners(), new OtherListeners());
    }


    @Override //idk, I have trust issues with java's garbage collector.
    protected void shutdown() {
        instance = null;
        configFile = null;
        locationsFile = null;
        gameManager = null;
        metrics = null;
    }
    public static Core getInstance() {
        return instance;
    }

    public void setConfigFile(YamlFile configFile) {
        this.configFile = configFile;
    }
}
