package com.teamacronymcoders.survivalism.common.blocks.barrels;

import com.teamacronymcoders.base.blocks.BlockTEBase;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BlockBarrelBase<T extends TileBarrelBase> extends BlockTEBase<T> {
    public static final PropertyBool SEALED = PropertyBool.create("sealed");

    BlockBarrelBase(String name) {
        super(Material.WOOD, name);
        setCreativeTab(Survivalism.TAB);
        setSoundType(SoundType.WOOD);
        setLightOpacity(0);
    }

    // Process Methods
    public static TileBarrelBase getTE(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBarrelBase) {
            return (TileBarrelBase) te;
        }
        return null;
    }

    // Information Methods
    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SEALED);
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(SEALED) ? 1 : 0;
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return state.getValue(SEALED);
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return true;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getStateFromMeta(meta);
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return side != null && side.getAxis() != EnumFacing.Axis.Y;
    }

    @Override
    public void getSubBlocks(@Nullable CreativeTabs creativeTab, @Nonnull NonNullList<ItemStack> list) {
        for (IBlockState state : getBlockState().getValidStates()) {
            list.add(new ItemStack(this, 1, getMetaFromState(state)));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta == 0) {
            return getDefaultState().withProperty(SEALED, false);
        } else {
            return getDefaultState().withProperty(SEALED, true);
        }
    }

}
