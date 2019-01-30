package com.teamacronymcoders.survivalism.client;

import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelBrewing;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelSoaking;
import com.teamacronymcoders.survivalism.client.container.barrel.ContainerBarrelStorage;
import com.teamacronymcoders.survivalism.client.container.vat.ContainerVat;
import com.teamacronymcoders.survivalism.client.gui.GUIBarrel;
import com.teamacronymcoders.survivalism.client.gui.GUICrushingVat;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.common.tiles.TileCrushingVat;
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
            if (barrel.checkBarrelState(StorageEnumsBarrelStates.STORAGE)) {
                return new ContainerBarrelStorage(player.inventory, barrel);
            } else if (barrel.checkBarrelState(StorageEnumsBarrelStates.BREWING)) {
                return new ContainerBarrelBrewing(player.inventory, barrel);
            } else if (barrel.checkBarrelState(StorageEnumsBarrelStates.SOAKING)) {
                return new ContainerBarrelSoaking(player.inventory, barrel);
            }
        }
        if (te instanceof TileCrushingVat) {
            TileCrushingVat vat = (TileCrushingVat) te;
            return new ContainerVat(player.inventory, vat);
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
            if (barrel.checkBarrelState(StorageEnumsBarrelStates.STORAGE)) {
                return new GUIBarrel(barrel, new ContainerBarrelStorage(player.inventory, barrel));
            } else if (barrel.checkBarrelState(StorageEnumsBarrelStates.BREWING)) {
                return new GUIBarrel(barrel, new ContainerBarrelBrewing(player.inventory, barrel));
            } else if (barrel.checkBarrelState(StorageEnumsBarrelStates.SOAKING)) {
                return new GUIBarrel(barrel, new ContainerBarrelSoaking(player.inventory, barrel));
            }
        }
        if (te instanceof TileCrushingVat) {
            TileCrushingVat vat = (TileCrushingVat) te;
            return new GUICrushingVat(vat, new ContainerVat(player.inventory, vat));
        }
        return null;
    }
}
