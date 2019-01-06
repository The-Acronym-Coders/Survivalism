package com.teamacronymcoders.survivalism.utils.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class SurvivalismPacketHandler {
    public static SimpleNetworkWrapper INSTANCE = null;
    private static int packetID = 0;

    public SurvivalismPacketHandler() {
    }

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
