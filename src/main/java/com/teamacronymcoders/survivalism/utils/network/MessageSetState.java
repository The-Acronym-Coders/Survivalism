package com.teamacronymcoders.survivalism.utils.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSetState implements IMessage, IMessageHandler<MessageSetState, IMessage> {


    public BlockPos pos;
    public IBlockState state;

    public MessageSetState() {
    }

    public MessageSetState(BlockPos pos, IBlockState state) {
        this.pos = pos;
        this.state = state;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        state = Block.getStateById(buf.readInt());

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(Block.getStateId(state));
    }

    @Override
    public IMessage onMessage(MessageSetState message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handle(message, ctx));
        return null;
    }

    private void handle(MessageSetState message, MessageContext ctx) {
        World world = ctx.getServerHandler().player.world;
        //this 9 may cause issues, it is normally 3, this just means that the block will update, and the re-render will be on a different thread
        world.setBlockState(message.pos, message.state, 9);
    }

}
