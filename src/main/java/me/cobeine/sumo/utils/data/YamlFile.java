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
package me.cobeine.sumo.utils.data;

import com.google.common.io.ByteStreams;
import me.cobeine.sumo.Core;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
@SuppressWarnings("all")
public abstract class YamlFile {
    public final String path;
    public final String name;
    private final File file;
    private YamlConfiguration cfg;

    public YamlFile(String name, String path) {
        this.path = path;
        this.name = name;
        this.file = new File(path, name + ".yml");
    }

    public void createNew() {
        try {
            if (file.createNewFile()) {
                Bukkit.getLogger().info("File '" + name + ".yml' has been created");
                cfg = YamlConfiguration.loadConfiguration(file);
                setDefaults();
            }

        } catch (Exception e) {

            e.printStackTrace();
            Bukkit.getLogger().severe("Failed to create file '" + name + ".yml' please check the error above");

        }

        if (file.exists())
            cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void setDefaultConfig() {
        try {
            InputStream stream = Core.getInstance().getResource(name + ".yml");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteStreams.copy(stream, fileOutputStream);
            stream.close();
        } catch (Exception ignored) {
        }
    }
    public abstract void setDefaults();

    public boolean exists() {
        return file.exists();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void delete() {
        file.delete();
        cfg = null;
    }

    public String getName() {
        return name;
    }

    public void save() {
        try {
            cfg.save(file);

        } catch (Exception e) {

            e.printStackTrace();
            Bukkit.getLogger().info("Failed to save file '" + name + ".yml' please check the error above");

        }
    }


    public YamlConfiguration getConfig() {
        return cfg;
    }
}