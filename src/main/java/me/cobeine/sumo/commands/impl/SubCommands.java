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
package me.cobeine.sumo.commands.impl;

import me.cobeine.sumo.Core;
import me.cobeine.sumo.commands.ISubCommand;
import me.cobeine.sumo.utils.data.impl.LocationsFile;
import me.cobeine.sumo.utils.Chat;
import me.cobeine.sumo.utils.enums.GameState;
import me.cobeine.sumo.utils.enums.LocationType;
import org.bukkit.entity.Player;


public final class SubCommands {

    @ISubCommand(value = "start", permissionKey = "sumo_start")
    public void start(Player player, String[] args) {
        if (!Core.getInstance().getGameManager().canStart()) {
            player.sendMessage(Chat.color("Messages.start_failed"));
            return;
        }
        if (!Core.getInstance().getGameManager().getGameState().equals(GameState.IDLE)) {
            player.sendMessage(Chat.color("Messages.tournament_already_started"));
            return;
        }
        if (Core.getInstance().getGameManager().getMinPlayers() < 2) {
            player.sendMessage(Chat.color("Messages.invalid_min_players"));
            return;
        }
        Core.getInstance().getGameManager().begin(player);
    }
    @ISubCommand(value = "setLocation", permissionKey = "sumo_setup")
    public void setLocation(Player player, String[] args) {
        if (!Core.getInstance().getGameManager().getGameState().equals(GameState.IDLE)) {
            player.sendMessage(Chat.color("Messages.modify_location_failed"));
            return;
        }
        LocationType type = LocationType.get(args[0].toUpperCase());
        if (type == null) {
            player.sendMessage(Chat.color("Messages.invalid_location"));
            return;
        }
        LocationsFile.getInstance().setLocation(type, player.getLocation());
        player.sendMessage(Chat.color("Messages.location_set").replace("<loc>", type.toString().toLowerCase()));
    }

    @ISubCommand(value = "join", permissionKey = "")
    public void join(Player player, String[] args) {
        switch (Core.getInstance().getGameManager().getGameState()){
            case IDLE:
                player.sendMessage(Chat.color("Messages.no_tournament"));
                break;
            case FIGHTING:
                player.sendMessage(Chat.color("Messages.tournament_already_started"));
                break;
            default:
                Core.getInstance().getGameManager().join(player);
                break;
        }

    }
    @ISubCommand(value = "reload", permissionKey = "sumo_setup")
    public void reload(Player player, String[] args) {

    }


}
