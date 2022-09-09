/**
 * MIT License
 * <p>
 * Copyright (c) 2022 Cobeine
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. YOU ARE NOT ALLOWED TO RE-DISTRIBUTE AND/OR REPUBLISH. YOU ARE NOT ALLOWED TO FORK
 * UNLESS GIVEN CREDIT TO THE ORIGINAL AUTHOR (COBEINE)
 */
package me.cobeine.sumo.utils.data.impl;

import me.cobeine.sumo.Core;
import me.cobeine.sumo.utils.Interfaces.AsyncTask;
import me.cobeine.sumo.utils.data.YamlFile;
import me.cobeine.sumo.utils.enums.LocationType;
import org.bukkit.Location;

public class LocationsFile extends YamlFile {
    public static LocationsFile instance;
    public LocationsFile() {
        super("locations", Core.getInstance().getDataFolder().getPath());
        instance = this;
    }

    @Override
    public void setDefaults() {
        setDefaultConfig();
    }
    public void setLocation(LocationType type, Location location) {
        AsyncTask.run(()->{
            getConfig().set("Locations." + type.toString(), location);
            save();
        });
    }

    public Location getLocation(LocationType type) {
        return (Location)getConfig().get("Locations." + type.toString());
    }

    public static LocationsFile getInstance() {
        return instance;
    }
}
