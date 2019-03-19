package com.teamacronymcoders.survivalism.utils.event.crushing;

import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class CrushingEvent extends LivingEvent {
    private TileCrushingVat vat;

    public CrushingEvent(EntityLivingBase entity, TileCrushingVat vat) {
        super(entity);
        this.vat = vat;
    }

    public TileCrushingVat getVat() {
        return vat;
    }

    @Cancelable
    public static class Pre extends CrushingEvent {
        public Pre(EntityLivingBase entity, TileCrushingVat vat) {
            super(entity, vat);
        }
    }

    @Cancelable
    public static class Post extends CrushingEvent {
        private ResourceLocation id;

        public Post(EntityLivingBase entity, TileCrushingVat vat) {
            super(entity, vat);
            this.id = vat.getRecipe().getID();
        }

        public ResourceLocation getId() {
            return id;
        }
    }

}
