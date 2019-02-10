package com.teamacronymcoders.survivalism.utils.storages;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;

public enum BarrelState implements IStringSerializable {
	STORAGE(1, 9),
	BREWING(2, 3),
	SOAKING(0, 2);

	int next;
	int invSize;

	BarrelState(int next, int invSize) {
		this.next = next;
		this.invSize = invSize;
	}

	public static final BarrelState[] VALUES = values();

	@Override
	public String getName() {
		return this.toString().toLowerCase(Locale.ROOT);
	}

	public int getInvSize() {
		return invSize;
	}

	public BarrelState next() {
		return VALUES[next];
	}

}
