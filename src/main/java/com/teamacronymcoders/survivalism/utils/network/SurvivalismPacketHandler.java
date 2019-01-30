package com.teamacronymcoders.survivalism.utils.network;

import com.teamacronymcoders.survivalism.Survivalism;
import net.minecraftforge.fml.relauncher.Side;

public class SurvivalismPacketHandler {

    private static int packetID = 0;

    public SurvivalismPacketHandler() {
    }

    public static int nextID() {
        return packetID++;
    }


    public static void registerMessages() {
        Survivalism.INSTANCE.getPacketHandler().registerPacket(MessageSetState.class, MessageSetState.class, Side.SERVER);
        Survivalism.INSTANCE.getPacketHandler().registerPacket(MessageOpenGui.class, MessageOpenGui.class, Side.SERVER);
        Survivalism.INSTANCE.getPacketHandler().registerPacket(MessageUpdateBarrel.class, MessageUpdateBarrel.class, Side.CLIENT);
        Survivalism.INSTANCE.getPacketHandler().registerPacket(MessageUpdateCrushingVat.class, MessageUpdateCrushingVat.class, Side.CLIENT);
    }
}
