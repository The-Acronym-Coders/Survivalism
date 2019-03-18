package com.teamacronymcoders.survivalism.utils.event;

import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
import net.minecraftforge.fml.common.eventhandler.Event;

public class JumpForceEvent extends Event {
    public JumpForceEvent() {}

    public static class BaseModification extends JumpForceEvent {
        TileCrushingVat vat;

        public BaseModification(TileCrushingVat vat) {
            this.vat = vat;
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

    }

    public static class MultiplierModification extends JumpForceEvent {
        TileCrushingVat vat;

        public MultiplierModification(TileCrushingVat vat) {
            this.vat = vat;
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
    }

}
