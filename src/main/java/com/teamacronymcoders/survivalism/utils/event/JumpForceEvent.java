package com.teamacronymcoders.survivalism.utils.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class JumpForceEvent extends Event {
    public JumpForceEvent() {}

    public static class BaseModification extends JumpForceEvent {
        private double originalValue;
        private double modifiedValue;

        public BaseModification(double value) {
            this.originalValue = value;
            this.modifiedValue = value;
        }

        public double getModifiedValue() {
            return modifiedValue;
        }

        public void addToModifier(double modifier) {
            modifiedValue += modifier;
        }

        public void removeFromModifier(double modifier) {
            if ((modifiedValue -= modifier) >= 0) {
                modifiedValue -= modifier;
            } else {
                modifiedValue = 0d;
            }
        }

        public void setModifiedValue(double value) {
            this.modifiedValue = value;
        }

        public double getOriginalValue() {
            return originalValue;
        }
    }

    public static class FinalModification extends JumpForceEvent {
        private double originalValue;
        private double modifiedValue;

        public FinalModification(double value) {
            this.originalValue = value;
            this.modifiedValue = value;
        }

        public double getModifiedValue() {
            return modifiedValue;
        }

        public void addToModifier(double modifier) {
            modifiedValue += modifier;
        }

        public void removeFromModifier(double modifier) {
            if ((modifiedValue -= modifier) >= 0) {
                modifiedValue -= modifier;
            } else {
                modifiedValue = 0d;
            }
        }

        public void setModifiedValue(double value) {
            this.modifiedValue = value;
        }

        public double getOriginalValue() {
            return originalValue;
        }
    }

}
