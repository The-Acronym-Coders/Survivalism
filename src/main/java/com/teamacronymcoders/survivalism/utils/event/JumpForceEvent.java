package com.teamacronymcoders.survivalism.utils.event;

import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Event;

public class JumpForceEvent extends Event {
    public JumpForceEvent() {}

    public static class BaseModification extends JumpForceEvent {
        EntityLivingBase living;
        TileCrushingVat vat;
        double base;

        public BaseModification(EntityLivingBase living, TileCrushingVat vat, double originalBase) {
            this.living = living;
            this.vat = vat;
            this.base = originalBase;
        }

        public void addBaseValue(double modification) {
            vat.addJumpBaseMod(modification);
        }

        public void removeBaseValue(double modification) {
            vat.removeJumpBaseMod(modification);
        }

        public double getBaseValue() {
            return vat.getJumpBase();
        }

        public double getOriginalBase() {
            return base;
        }

    }

    public static class MultiplierModification extends JumpForceEvent {
        EntityLivingBase living;
        TileCrushingVat vat;
        double multiplier;

        public MultiplierModification(EntityLivingBase living, TileCrushingVat vat, double originalMultiplier) {
            this.living = living;
            this.vat = vat;
            this.multiplier = originalMultiplier;
        }

        public void addMultiplierValue(double modification) {
            vat.addMultiplierBaseMod(modification);
        }

        public void removeMultiplierValue(double modification) {
            vat.removeMultiplierBaseMod(modification);
        }

        public double getMultiplierValue() {
            return vat.getMultiplierBase();
        }

        public double getOriginalMultiplier() {
            return multiplier;
        }
    }

}
