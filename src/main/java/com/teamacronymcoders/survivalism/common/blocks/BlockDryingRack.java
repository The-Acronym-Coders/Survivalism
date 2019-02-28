package com.teamacronymcoders.survivalism.common.blocks;

import com.teamacronymcoders.base.blocks.BlockTEBase;
import com.teamacronymcoders.survivalism.client.render.DryingRackTESR;
import com.teamacronymcoders.survivalism.common.tiles.TileDryingRack;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockDryingRack extends BlockTEBase<TileDryingRack> {

    public BlockDryingRack() {
        super(Material.WOOD, "drying_rack");
        setTranslationKey("drying_rack");
    }

    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("survivalism:drying_rack", "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileDryingRack.class, new DryingRackTESR());
    }

    @Nullable
    public static TileDryingRack getTE(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileDryingRack ? (TileDryingRack) te : null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileDryingRack te = getTE(worldIn, pos);
            if (te != null) {
                if (te.getStack().isEmpty()) {
                    if (!playerIn.getHeldItem(hand).isEmpty()) {
                        ItemStack currentStack = playerIn.getHeldItem(hand);
                        if (currentStack.getCount() - 1 <= 0) {
                            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemStack.EMPTY);
                        } else {
                            currentStack.setCount(currentStack.getCount() - 1);
                            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, currentStack);
                        }
                        currentStack.setCount(1);
                        te.setStack(currentStack);
                        playerIn.openContainer.detectAndSendChanges();
                        worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                    }
                } else {
                    ItemStack stack = te.getStack();
                    te.setStack(ItemStack.EMPTY);
                    if (!playerIn.inventory.addItemStackToInventory(stack)) {
                        EntityItem entityItem = new EntityItem(worldIn, pos.getX(), pos.getY()+1, pos.getZ(), stack);
                        worldIn.spawnEntity(entityItem);
                        worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                    } else {
                        playerIn.openContainer.detectAndSendChanges();
                        worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
        return new TileDryingRack();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileDryingRack.class;
    }
}
