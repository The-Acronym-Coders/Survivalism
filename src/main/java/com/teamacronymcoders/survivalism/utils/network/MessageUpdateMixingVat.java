package com.teamacronymcoders.survivalism.utils.network;

import com.teamacronymcoders.survivalism.client.gui.GUIMixingVat;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileMixingVat;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

public class MessageUpdateMixingVat implements IMessage, IMessageHandler<MessageUpdateMixingVat, IMessage> {
    private FluidStack main;
    private FluidStack secondary;
    private FluidStack output;

    public MessageUpdateMixingVat(){}

    public MessageUpdateMixingVat(TileMixingVat te) {
        this.main = te.getMain().getFluid();
        this.secondary = te.getSecondary().getFluid();
        this.output = te.getOutput().getFluid();
    }

    private static FluidStack readFluid(ByteBuf buf) {
        int amount = buf.readInt();
        String name = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
        return name.equals("null") ? null : new FluidStack(FluidRegistry.getFluid(name), amount);
    }

    private static void writeFluid(ByteBuf buf, FluidStack stack) {
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
        main = readFluid(buf);
        secondary = readFluid(buf);
        output = readFluid(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeFluid(buf, main);
        writeFluid(buf, secondary);
        writeFluid(buf, output);
    }

    @Override
    public IMessage onMessage(MessageUpdateMixingVat message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
           if (Minecraft.getMinecraft().currentScreen instanceof GUIMixingVat) {
               ((GUIMixingVat) Minecraft.getMinecraft().currentScreen).setMain(message.main);
               ((GUIMixingVat) Minecraft.getMinecraft().currentScreen).setSecondary(message.secondary);
               ((GUIMixingVat) Minecraft.getMinecraft().currentScreen).setOutput(message.output);
           }
        });
        return null;
    }
}
