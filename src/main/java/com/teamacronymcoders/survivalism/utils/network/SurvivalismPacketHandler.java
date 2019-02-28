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
        Survivalism.INSTANCE.getPacketHandler().registerPacket(MessageBarrelButton.class, MessageBarrelButton.class, Side.SERVER);
        Survivalism.INSTANCE.getPacketHandler().registerPacket(MessageUpdateCrushingVat.class, MessageUpdateCrushingVat.class, Side.CLIENT);
        Survivalism.INSTANCE.getPacketHandler().registerPacket(MessageUpdateBarrel.class, MessageUpdateBarrel.class, Side.CLIENT);
        Survivalism.INSTANCE.getPacketHandler().registerPacket(MessageUpdateDryingRack.class, MessageUpdateDryingRack.class, Side.CLIENT);
    }
}
