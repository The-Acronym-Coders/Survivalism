package com.teamacronymcoders.survivalism.utils.network;

import mezz.jei.network.PacketHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class SurvivalismPacketHandler {
    private static int packetID = 0;
    public static SimpleNetworkWrapper INSTANCE = null;

    public SurvivalismPacketHandler() {}

    public static int nextID() {
        return packetID++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
    }

    public static void registerMessages() {
        INSTANCE.registerMessage(MessageBarrelState.Handler.class, MessageBarrelState.class, nextID(), Side.SERVER);
    }
}
