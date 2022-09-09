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
package me.cobeine.sumo.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
//Class created by ConnorLinfoot, pretty old but it works
public final class Actionbar {
    public static void send(Player player, String message) {
        String nmsver;
        boolean useOldMethods = false;
        nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
        if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.startsWith("v1_7_"))
            useOldMethods = true;
        if (!player.isOnline())
            return;
        try {
            Object packet;
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
            Class<?> packetClass = Class.forName("net.minecraft.server." + nmsver + ".Packet");
            if (useOldMethods) {
                Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
                Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                Method m3 = chatSerializerClass.getDeclaredMethod("a", String.class);
                Object cbc = iChatBaseComponentClass.cast(m3.invoke(chatSerializerClass, "{\"text\": \"" + message + "\"}"));
                packet = packetPlayOutChatClass.getConstructor(new Class[] { iChatBaseComponentClass, byte.class }).newInstance(cbc, (byte) 2);
            } else {
                Class<?> chatComponentTextClass = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
                Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                try {
                    Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsver + ".ChatMessageType");
                    Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                    Object chatMessageType = null;
                    for (Object obj : chatMessageTypes) {
                        if (obj.toString().equals("GAME_INFO"))
                            chatMessageType = obj;
                    }
                    Object chatCompontentText = chatComponentTextClass.getConstructor(new Class[] { String.class }).newInstance(message);
                    packet = packetPlayOutChatClass.getConstructor(new Class[] { iChatBaseComponentClass, chatMessageTypeClass }).newInstance(chatCompontentText, chatMessageType);
                } catch (ClassNotFoundException cnfe) {
                    Object chatCompontentText = chatComponentTextClass.getConstructor(new Class[] { String.class }).newInstance(message);
                    packet = packetPlayOutChatClass.getConstructor(new Class[] { iChatBaseComponentClass, byte.class }).newInstance(chatCompontentText, (byte) 2);
                }
            }
            Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
            Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
            Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
            Object playerConnection = playerConnectionField.get(craftPlayerHandle);
            Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", packetClass);
            sendPacketMethod.invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
