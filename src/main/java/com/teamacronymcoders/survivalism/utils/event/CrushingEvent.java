package com.teamacronymcoders.survivalism.utils.event;

import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
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
        private ItemStack inputItem;
        private ItemStack outputItem;
        private FluidStack outputFluid;

        public Post(EntityLivingBase entity, TileCrushingVat vat) {
            super(entity, vat);
            this.inputItem = vat.getInputInv().getStackInSlot(0);
            this.outputItem = vat.getOutputInv().getStackInSlot(0);
            this.outputFluid = vat.getTank().getFluid();
        }

        public ItemStack getInputItem() {
            return inputItem;
        }

        public ItemStack getOutputItem() {
            return outputItem;
        }

        public FluidStack getOutputFluid() {
            return outputFluid;
        }
    }

}
