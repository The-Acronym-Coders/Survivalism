package com.teamacronymcoders.survivalism.utils.event.mixing;

import com.teamacronymcoders.survivalism.common.tiles.vats.TileMixingVat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class MixingEvent extends PlayerEvent {
    private TileMixingVat vat;

    public MixingEvent(EntityPlayer entity, TileMixingVat vat) {
        super(entity);
        this.vat = vat;
    }

    public TileMixingVat getVat() {
        return vat;
    }

    @Cancelable
    public static class Pre extends MixingEvent {
        public Pre(EntityPlayer entity, TileMixingVat vat) {
            super(entity, vat);
        }
    }

    @Cancelable
    public static class Post extends MixingEvent {
        private ResourceLocation id;

        public Post(EntityPlayer entity, TileMixingVat vat) {
            super(entity, vat);
            this.id = vat.getRecipe().getId();
        }

        public ResourceLocation getId() {
            return id;
        }
    }
}
