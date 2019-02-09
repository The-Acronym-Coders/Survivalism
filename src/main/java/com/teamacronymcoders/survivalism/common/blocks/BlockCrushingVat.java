package com.teamacronymcoders.survivalism.common.blocks;

import com.teamacronymcoders.base.blocks.BlockBase;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.TileCrushingVat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCrushingVat extends BlockBase {

    public static final int GUI_ID = 2;
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockCrushingVat() {
        super(Material.WOOD);
        setCreativeTab(Survivalism.TAB);
        setTranslationKey(Survivalism.MODID + ".crushingvat");
        setRegistryName("crushing_vat");
        setSoundType(SoundType.WOOD);
        setLightOpacity(0);
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileCrushingVat te = getTE(worldIn, pos);
        if (te.getInputItemHandler() != null && te.getOutputItemHandler() != null) {
            for (int i = 0; i < te.getInputItemHandler().getSlots(); i++) {
                ItemStack stack = te.getInputItemHandler().getStackInSlot(i);
                InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
            for (int i = 0; i < te.getOutputItemHandler().getSlots(); i++) {
                ItemStack stack = te.getOutputItemHandler().getStackInSlot(i);
                InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCrushingVat();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    private TileCrushingVat getTE(World world, BlockPos pos) {
        if (world.getTileEntity(pos) != null) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileCrushingVat) {
                return (TileCrushingVat) te;
            }
        }
        return null;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        TileCrushingVat vat = getTE(worldIn, pos);
        if (vat != null) {
            if (entityIn instanceof EntityLivingBase) {
                vat.makeProgress((EntityLivingBase) entityIn);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileCrushingVat vat = getTE(worldIn, pos);

        if (vat == null) {
            return false;
        }

        if (worldIn.isRemote) {
            return true;
        }

        if (playerIn.getHeldItem(hand).getItem() instanceof ItemBucket) {
            ItemStack stack = playerIn.getHeldItem(hand);
            FluidUtil.tryFillContainer(stack, FluidUtil.getFluidHandler(stack), Integer.MAX_VALUE, playerIn, true);
            playerIn.openContainer.detectAndSendChanges();
            return true;
        }

        playerIn.openGui(Survivalism.INSTANCE, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byIndex((meta & 3) + 2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() - 2;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
