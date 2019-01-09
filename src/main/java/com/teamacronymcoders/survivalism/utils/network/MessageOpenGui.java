package com.teamacronymcoders.survivalism.utils.network;

import com.teamacronymcoders.survivalism.Survivalism;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageOpenGui implements IMessage, IMessageHandler<MessageOpenGui, IMessage> {


    public BlockPos pos;
    public int guiId;

    public MessageOpenGui() {
    }

    public MessageOpenGui(BlockPos pos, int state) {
        this.pos = pos;
        this.guiId = state;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        guiId = buf.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(guiId);
    }

    @Override
    public IMessage onMessage(MessageOpenGui message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handle(message, ctx));
        return null;
    }

    private void handle(MessageOpenGui message, MessageContext ctx) {
        ctx.getServerHandler().player.openGui(Survivalism.INSTANCE, message.guiId, ctx.getServerHandler().player.world, message.pos.getX(), message.pos.getY(), message.pos.getZ());
    }

}
