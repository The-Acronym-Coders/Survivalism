package com.teamacronymcoders.survivalism.utils.network;

import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateBarrel implements IMessage, IMessageHandler<MessageUpdateBarrel, IMessage> {

	public MessageUpdateBarrel() {
	}

	public MessageUpdateBarrel(TileBarrel barrel) {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public IMessage onMessage(MessageUpdateBarrel message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
		return null;
	}

	private void handle(MessageUpdateBarrel message, MessageContext ctx) {

	}
}
