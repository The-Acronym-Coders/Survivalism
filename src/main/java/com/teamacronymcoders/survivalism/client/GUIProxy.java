//package com.teamacronymcoders.survivalism.client;
//
//import com.teamacronymcoders.survivalism.client.container.vat.ContainerVat;
//import com.teamacronymcoders.survivalism.client.gui.GUIBarrel;
//import com.teamacronymcoders.survivalism.client.gui.GUICrushingVat;
//import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
//import com.teamacronymcoders.survivalism.common.tiles.TileCrushingVat;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.common.network.IGuiHandler;
//
//import javax.annotation.Nullable;
//
//public class GUIProxy implements IGuiHandler {
//    @Nullable
//    @Override
//    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
//        BlockPos pos = new BlockPos(x, y, z);
//        TileEntity te = world.getTileEntity(pos);
//        if (te instanceof TileBarrel) {
//            return ((TileBarrel) te).getContainer(player.inventory);
//        }
//        if (te instanceof TileCrushingVat) {
//            return new ContainerVat(player.inventory, (TileCrushingVat) te);
//        }
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
//        BlockPos pos = new BlockPos(x, y, z);
//        TileEntity te = world.getTileEntity(pos);
//        if (te instanceof TileBarrel) {
//            TileBarrel barrel = (TileBarrel) te;
//            return new GUIBarrel(barrel, barrel.getContainer(player.inventory));
//        }
//        if (te instanceof TileCrushingVat) {
//            TileCrushingVat vat = (TileCrushingVat) te;
//            return new GUICrushingVat(vat, new ContainerVat(player.inventory, vat));
//        }
//        return null;
//    }
//}
