package com.teamacronymcoders.survivalism.utils.network;

import com.teamacronymcoders.survivalism.common.tiles.TileCrushingVat;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateCrushingVat implements IMessage, IMessageHandler<MessageUpdateCrushingVat, IMessage> {
    private int x;
    private int y;
    private int z;
    private int amount;

    public MessageUpdateCrushingVat() {}

    public MessageUpdateCrushingVat(TileCrushingVat vat) {
        this.x = vat.getPos().getX();
        this.y = vat.getPos().getY();
        this.z = vat.getPos().getZ();
        this.amount = vat.getTank().getFluidAmount();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.amount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.amount);
    }

    @Override
    public IMessage onMessage(MessageUpdateCrushingVat message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
        return null;
    }

    private void handle(MessageUpdateCrushingVat message, MessageContext ctx) {
        if (FMLClientHandler.instance().getClient().world != null) {
            TileEntity te = FMLClientHandler.instance().getClient().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if (te instanceof TileCrushingVat) {
                TileCrushingVat vat = (TileCrushingVat) te;
                FluidTank tank = vat.getTank();
                if (tank.getFluid() != null) {
                    if (message.amount == 0) {
                        tank.setFluid(null);
                    } else {
                        tank.getFluid().amount = message.amount;
                    }
                }
            }
        }
    }
}
