package com.teamacronymcoders.survivalism.utils.network;

import com.teamacronymcoders.survivalism.common.tiles.TileDryingRack;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateDryingRack implements IMessage, IMessageHandler<MessageUpdateDryingRack, IMessage> {

    private ItemStack stack;
    private BlockPos pos;

    public MessageUpdateDryingRack(){}

    public MessageUpdateDryingRack(TileDryingRack te) {
        this.stack = te.getStack();
        this.pos = te.getPos();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        stack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeItemStack(buf, stack);
    }

    @Override
    public IMessage onMessage(MessageUpdateDryingRack message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
            if (te instanceof TileDryingRack) {
                ((TileDryingRack) te).setStack(message.stack);
            }
        });
        return null;
    }
}
