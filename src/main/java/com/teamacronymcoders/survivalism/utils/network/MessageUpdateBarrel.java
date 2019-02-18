package com.teamacronymcoders.survivalism.utils.network;

import com.teamacronymcoders.survivalism.client.gui.barrels.GUIBarrel;
import com.teamacronymcoders.survivalism.client.gui.barrels.GUIBarrelBrewing;
import com.teamacronymcoders.survivalism.client.gui.barrels.GUIBarrelSoaking;
import com.teamacronymcoders.survivalism.client.gui.barrels.GUIBarrelStorage;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelSoaking;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

public class MessageUpdateBarrel implements IMessage, IMessageHandler<MessageUpdateBarrel, IMessage> {

    private FluidStack inStack;
    private FluidStack outStack;

    public MessageUpdateBarrel() {
    }

    public MessageUpdateBarrel(TileBarrelBase te) {
        if (te instanceof TileBarrelBrewing) {
            TileBarrelBrewing brewing = (TileBarrelBrewing) te;
            this.inStack = brewing.getInput().getFluid();
            this.outStack = brewing.getOutput().getFluid();
        } else if (te instanceof TileBarrelSoaking) {
            TileBarrelSoaking soaking = (TileBarrelSoaking) te;
            this.inStack = soaking.getInput().getFluid();
        } else {
            TileBarrelStorage storage = (TileBarrelStorage) te;
            this.inStack = storage.getInput().getFluid();
        }
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
    public void toBytes(ByteBuf buf) {
        writeFluid(buf, inStack);
        writeFluid(buf, outStack);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        inStack = readFluid(buf);
        outStack = readFluid(buf);
    }

    @Override
    public IMessage onMessage(MessageUpdateBarrel message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            if (Minecraft.getMinecraft().currentScreen instanceof GUIBarrel) {
                if (Minecraft.getMinecraft().currentScreen instanceof GUIBarrelBrewing) {
                    ((GUIBarrelBrewing) Minecraft.getMinecraft().currentScreen).setInput(message.inStack);
                    ((GUIBarrelBrewing) Minecraft.getMinecraft().currentScreen).setOutput(message.outStack);
                } else if (Minecraft.getMinecraft().currentScreen instanceof GUIBarrelSoaking) {
                    ((GUIBarrelSoaking) Minecraft.getMinecraft().currentScreen).setInput(message.inStack);
                } else {
                    ((GUIBarrelStorage) Minecraft.getMinecraft().currentScreen).setInput(message.inStack);
                }
            }
        });
        return null;
    }
}
