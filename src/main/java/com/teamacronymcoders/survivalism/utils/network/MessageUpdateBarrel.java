package com.teamacronymcoders.survivalism.utils.network;

import java.nio.charset.StandardCharsets;

import com.teamacronymcoders.survivalism.client.gui.GUIBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateBarrel implements IMessage, IMessageHandler<MessageUpdateBarrel, IMessage> {

	private FluidStack inStack;
	private FluidStack outStack;

	public MessageUpdateBarrel() {
	}

	public MessageUpdateBarrel(TileBarrel te) {
		this.inStack = te.getInput().getFluid();
		this.outStack = te.getOutput().getFluid();
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
				((GUIBarrel) Minecraft.getMinecraft().currentScreen).setInput(message.inStack);
				((GUIBarrel) Minecraft.getMinecraft().currentScreen).setOutput(message.outStack);
			}
		});
		return null;
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
}
