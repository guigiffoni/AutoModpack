/*
 * This file is part of the AutoModpack project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023 Skidam and contributors
 *
 * AutoModpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AutoModpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AutoModpack.  If not, see <https://www.gnu.org/licenses/>.
 */

package pl.skidam.automodpack.networking.packet;

//#if FABRICLIKE

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.login.LoginDisconnectS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;
import pl.skidam.automodpack.client.ui.versioned.VersionedText;
import pl.skidam.automodpack.mixin.ServerLoginNetworkHandlerAccessor;

import static pl.skidam.automodpack.GlobalVariables.LOGGER;

public class LinkS2CPacket {

    private static void packet(ServerLoginNetworkHandler handler, PacketByteBuf buf) {
        GameProfile profile = ((ServerLoginNetworkHandlerAccessor) handler).getGameProfile();

        if (buf.readBoolean()) { // disconnect
            LOGGER.warn("{} has not installed modpack", profile.getName());
            Text reason = VersionedText.common.literal("[AutoModpack] Install/Update modpack to join");
            ClientConnection connection = ((ServerLoginNetworkHandlerAccessor) handler).getConnection();
            connection.send(new LoginDisconnectS2CPacket(reason));
            connection.disconnect(reason);
        } else {
            LOGGER.info("{} has installed whole modpack", profile.getName());
        }
    }

    public static void receive(MinecraftServer server, ServerLoginNetworkHandler handler, boolean b, PacketByteBuf buf, ServerLoginNetworking.LoginSynchronizer sync, PacketSender sender) {
        packet(handler, buf);
    }
}

//#endif