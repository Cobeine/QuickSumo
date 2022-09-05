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
package me.cobeine.sumo.commands;

import me.cobeine.sumo.Core;
import me.cobeine.sumo.utils.enums.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Arrays;

public interface ICommand extends CommandExecutor {

    @Override
    default boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player)){
            sender.sendMessage(Chat.color("Messages.player_only"));
            return true;
        }
        Player player = (Player) sender;
        switch (args.length) {
            case 0:
                execute(player, null, new String[]{});
                break;
            case 1:
                execute(player, args[0], new String[]{});
                break;
            default:
                execute(player, args[0], cleanArgs(args));
                break;
        }
        return true;
    }

    void execute(Player player, String subCommand, String[] args);

    default boolean executeSubCommand(Object clazz, String subCommand, Player executor,String[] args) {
        for (final Method method : clazz.getClass().getDeclaredMethods())


            if (method.isAnnotationPresent(ISubCommand.class)) {

                ISubCommand annotation = method.getAnnotation(ISubCommand.class);
                String targetedSub = annotation.value();

                if (targetedSub.equalsIgnoreCase(subCommand))
                    if (executor.hasPermission(Core.getConfigString(annotation.permissionTag())))
                         try {method.invoke(clazz, executor, args);return true;} catch (Exception ignored) {}
                    else {
                        executor.sendMessage(Chat.color("Messages.no_permission"));
                        return true;
                    }


            }
        return false;
    }

    default String[] cleanArgs(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
}
