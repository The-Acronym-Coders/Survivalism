package com.teamacronymcoders.survivalism.utils.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHandlerBarrelState implements IMessageHandler<MessageBarrelState, IMessage> {

    @Override
    public IMessage onMessage(MessageBarrelState message, MessageContext ctx) {

        return null;
    }

}
