package com.teamacronymcoders.survivalism.utils.network;

import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrel;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageBarrelButton implements IMessage, IMessageHandler<MessageBarrelButton, IMessage> {

    int id;

    public MessageBarrelButton() {
    }

    public MessageBarrelButton(int id) {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(id);
    }

    @Override
    public IMessage onMessage(MessageBarrelButton msg, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            Container c = ctx.getServerHandler().player.openContainer;
            if (c instanceof ContainerBarrel) {
                TileBarrelBase te = ((ContainerBarrel) c).getTile();
                if (msg.id == 0) {
                    te.updateSeal(!te.isSealed());
                }
            }
        });
        return null;
    }
}
