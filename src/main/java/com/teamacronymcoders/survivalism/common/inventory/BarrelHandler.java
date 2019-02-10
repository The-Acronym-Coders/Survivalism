package com.teamacronymcoders.survivalism.common.inventory;

import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;

public class BarrelHandler extends UpdatingItemStackHandler {

	TileBarrel te;

	public BarrelHandler(int size, TileBarrel inv) {
		super(size, inv);
		te = inv;
	}

	@Override
	public int getSlots() {
		return te.getState().getInvSize();
	}

}
