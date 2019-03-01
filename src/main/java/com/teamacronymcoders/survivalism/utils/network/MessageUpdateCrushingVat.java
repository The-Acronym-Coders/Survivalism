package com.teamacronymcoders.survivalism.utils.network;

import com.teamacronymcoders.survivalism.client.gui.GUICrushingVat;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

public class MessageUpdateCrushingVat implements IMessage, IMessageHandler<MessageUpdateCrushingVat, IMessage> {

    private FluidStack stack;

    public MessageUpdateCrushingVat() {
    }

    public MessageUpdateCrushingVat(TileCrushingVat te) {
        this.stack = te.getTank().getFluid();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (stack == null) {
            buf.writeInt(0);
            buf.writeInt(4);
            buf.writeCharSequence("null", StandardCharsets.UTF_8);
        } else {
            buf.writeInt(stack.amount);
            String name = stack.getFluid().getName();
            buf.writeInt(name.length());
            buf.writeCharSequence(name, StandardCharsets.UTF_8);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int amount = buf.readInt();
        String name = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
        stack = name.equals("null") ? null : new FluidStack(FluidRegistry.getFluid(name), amount);
    }

    @Override
    public IMessage onMessage(MessageUpdateCrushingVat message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            if (Minecraft.getMinecraft().currentScreen instanceof GUICrushingVat) {
                ((GUICrushingVat) Minecraft.getMinecraft().currentScreen).setFluid(message.stack);
            }
        });
        return null;
    }
}
