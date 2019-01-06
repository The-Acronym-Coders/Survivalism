package com.teamacronymcoders.survivalism.utils.network;

import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageBarrelState implements IMessage {
    private BlockPos blockPos;

    public MessageBarrelState() {
        RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        blockPos = ((RayTraceResult) mouseOver).getBlockPos();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos.toLong());
    }

    public static class Handler implements IMessageHandler<MessageBarrelState, IMessage> {

        @Override
        public IMessage onMessage(MessageBarrelState message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(MessageBarrelState messageBarrelState, MessageContext ctx) {
            EntityPlayerMP playerMP = ctx.getServerHandler().player;
            World world = playerMP.getEntityWorld();
            if (world.isBlockLoaded(messageBarrelState.blockPos)) {
                TileEntity te = world.getTileEntity(messageBarrelState.blockPos);
                if (te instanceof TileBarrel) {
                    ((TileBarrel) te).cycleStates(world.getBlockState(messageBarrelState.blockPos));
                }
            }
        }
    }
}
