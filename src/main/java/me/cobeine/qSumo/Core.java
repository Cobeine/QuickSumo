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
package me.cobeine.qSumo;

import lombok.Getter;
import me.cobeine.qSumo.Listeners.QuitListener;
import me.cobeine.qSumo.Managers.GameManager;
import me.cobeine.qSumo.Managers.SumoManager;
import me.cobeine.qSumo.commands.impl.QuickSumoCommand;
import me.cobeine.qSumo.utils.SpigotPlugin;
import me.cobeine.qSumo.utils.metrics.MetricsImpl;

@Getter
public class Core extends SpigotPlugin {
    private static Core instance;
    private GameManager gameManager;
    private MetricsImpl metrics;
    @Override
    protected void init() {
        instance = this;
        gameManager = new SumoManager();
        metrics = new MetricsImpl(this);
    }

    @Override
    protected void registerCommands() {
        registerCommand("QuickSumo",new QuickSumoCommand());
    }

    @Override
    protected void registerListeners() {
        registerListeners(new QuitListener());
    }


    @Override
    protected void shutdown() {

    }
}
