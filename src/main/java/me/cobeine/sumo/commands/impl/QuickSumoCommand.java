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
 * SOFTWARE. YOU ARE NOT ALLOWED TO RE-DUSRIBUTE AND/OR REPUBLISH. YOU ARE NOT ALLOWED TO FORK
 * UNLESS GIVEN CREDIT TO THE ORIGINAL AUTHOR (COBEINE)
 */
package me.cobeine.sumo.commands.impl;


import me.cobeine.sumo.commands.Command;
import me.cobeine.sumo.utils.enums.Chat;
import org.bukkit.entity.Player;

public class QuickSumoCommand extends Command{
    final SubCommands subCommands = new SubCommands();
    public QuickSumoCommand(String name) {
        super(name, new TabComplete());
    }
    @Override
    public void execute(Player player, String subCommand, String[] args) {
        if (subCommand == null || subCommand.equalsIgnoreCase("help")) {
            player.sendMessage(Chat.coloredList("Messages.help_message"));
            return;
        }
        if (!executeSubCommand(subCommands, subCommand,player,args))
            player.sendMessage(Chat.color("Messages.invalid_args"));
    }

    public SubCommands getSubCommands() {
        return subCommands;
    }
}
