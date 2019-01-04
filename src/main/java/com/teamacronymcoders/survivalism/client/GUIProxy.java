package com.teamacronymcoders.survivalism.client;

import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelBrewing;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelSoaking;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelStorage;
import com.teamacronymcoders.survivalism.client.gui.GUIBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.storages.StorageEnumsBarrelStates;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GUIProxy implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBarrel) {
            TileBarrel barrel = (TileBarrel) te;
            if (barrel.checkState(StorageEnumsBarrelStates.STORAGE)) {
                return new ContainerBarrelStorage(player.inventory, barrel);
            } else if (barrel.checkState(StorageEnumsBarrelStates.BREWING)) {
                return new ContainerBarrelBrewing(player.inventory, barrel);
            } else if (barrel.checkState(StorageEnumsBarrelStates.SOAKING)) {
                return new ContainerBarrelSoaking(player.inventory, barrel);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBarrel) {
            TileBarrel barrel = (TileBarrel) te;
            if (barrel.checkState(StorageEnumsBarrelStates.STORAGE)) {
                return new GUIBarrel(barrel, new ContainerBarrelStorage(player.inventory, barrel));
            } else if (barrel.checkState(StorageEnumsBarrelStates.BREWING)) {
                return new GUIBarrel(barrel, new ContainerBarrelBrewing(player.inventory, barrel));
            } else if (barrel.checkState(StorageEnumsBarrelStates.SOAKING)) {
                return new GUIBarrel(barrel, new ContainerBarrelSoaking(player.inventory, barrel));
            }
        }
        return null;
    }
}
