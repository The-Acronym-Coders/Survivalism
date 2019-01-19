package com.teamacronymcoders.survivalism.utils.network;

import com.teamacronymcoders.survivalism.common.blocks.BlockBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateBarrel implements IMessage, IMessageHandler<MessageUpdateBarrel, IMessage> {
    private int x;
    private int y;
    private int z;
    private int state;
    private boolean sealed;
    private int amountI;
    private int amountO;

    public MessageUpdateBarrel(){}

    public MessageUpdateBarrel(TileBarrel barrel) {
        this.x = barrel.getPos().getX();
        this.y = barrel.getPos().getY();
        this.z = barrel.getPos().getZ();
        this.state = barrel.getWorld().getBlockState(barrel.getPos()).getValue(BlockBarrel.BARREL_STATE).ordinal();
        this.sealed = barrel.getWorld().getBlockState(barrel.getPos()).getValue(BlockBarrel.SEALED_STATE);
        this.amountI = barrel.getInputTank().getFluidAmount();
        this.amountO = barrel.getOutputTank().getFluidAmount();
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();

        this.state = buf.readInt();
        this.sealed = buf.readBoolean();

        this.amountI = buf.readInt();
        this.amountO = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);

        buf.writeInt(this.state);
        buf.writeBoolean(this.sealed);

        buf.writeInt(this.amountI);
        buf.writeInt(this.amountO);
    }

    @Override
    public IMessage onMessage(MessageUpdateBarrel message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
        return null;
    }

    private void handle(MessageUpdateBarrel message, MessageContext ctx) {
        if (FMLClientHandler.instance().getClient().world != null) {
            TileEntity te = FMLClientHandler.instance().getClient().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if (te instanceof TileBarrel) {
                ((TileBarrel) te).setState(message.state);
                ((TileBarrel) te).setSealed(message.sealed);
                ((TileBarrel) te).getInputTank().getFluid().amount = message.amountI;
                ((TileBarrel) te).getOutputTank().getFluid().amount = message.amountO;
            }
        }
    }
}
