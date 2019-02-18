package com.teamacronymcoders.survivalism.common.blocks;

import com.teamacronymcoders.base.blocks.BlockTEBase;
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
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class BlockCrushingVat extends BlockTEBase<TileCrushingVat> {

    public static final int GUI_ID = 4;
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockCrushingVat() {
        super(Material.WOOD, "crushing_vat");
        setCreativeTab(Survivalism.TAB);
        setTranslationKey("crushing_vat");
        setSoundType(SoundType.WOOD);
        setLightOpacity(0);
    }

    @Nullable
    public static TileCrushingVat getTE(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileCrushingVat ? (TileCrushingVat) te : null;
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
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileCrushingVat te = getTE(world, pos);
        if (te != null) {

            ItemStack stack = te.getInputInv().getStackInSlot(0);
            if (!stack.isEmpty()) {
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
            stack = te.getOutputInv().getStackInSlot(0);
            if (!stack.isEmpty()) {
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            }

        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCrushingVat();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        TileCrushingVat vat = getTE(world, pos);
        if (vat != null && entity instanceof EntityLivingBase) {
            vat.onJump((EntityLivingBase) entity);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileCrushingVat vat = getTE(world, pos);

        if (vat == null) {
            return false;
        }
        if (world.isRemote) {
            return true;
        }
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() == Items.BUCKET && vat.getTank().getFluidAmount() >= 1000) {
            ItemStack full = FluidUtil.getFilledBucket(vat.getTank().getFluid());
            if (!full.isEmpty()) {
                stack.shrink(1);
                vat.getTank().drain(1000, true);
                player.addItemStackToInventory(full);
                return true;
            }
        } else {
            if (FluidUtil.tryFillContainer(stack, FluidUtil.getFluidHandler(stack), Integer.MAX_VALUE, player, true).success) {
                return true;
            }
        }

        player.openGui(Survivalism.INSTANCE, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byIndex((meta & 3) + 2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() - 2;
    }

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

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 1, 0.625, 1);
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileCrushingVat.class;
    }
}
