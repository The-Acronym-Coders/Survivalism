package com.teamacronymcoders.survivalism.common.blocks;

import com.teamacronymcoders.base.blocks.properties.PropertySideType;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.defaults.BlockDefault;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.storages.StorageEnumsBarrelStates;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockBarrel extends BlockDefault {

    public static final PropertyEnum<StorageEnumsBarrelStates> BARREL_STATE = PropertyEnum.create("barrel_state", StorageEnumsBarrelStates.class);
    public static final int GUI_ID = 1;
    public static final PropertyBool SEALED_STATE = PropertyBool.create("sealed");
    private static final PropertyEnum<BlockLog.EnumAxis> AXIS = PropertyEnum.create("axis", BlockLog.EnumAxis.class);

    private NBTTagCompound compound = new NBTTagCompound();

    public BlockBarrel() {
        super(Material.WOOD);
        setCreativeTab(Survivalism.TAB);
        setUnlocalizedName(Survivalism.MODID + ".barrel");
        setRegistryName("barrel");
        setSoundType(SoundType.WOOD);
        setLightOpacity(255);
    }

    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBarrel();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    private TileBarrel getTE(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBarrel) {
            return (TileBarrel) te;
        }
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        compound = new NBTTagCompound();
        TileEntity te = getTE(worldIn, pos);
        if (te != null) {
            compound = te.serializeNBT();
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.clear();
        ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1, 0);
        stack.setTagCompound(compound);
        drops.add(stack);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileBarrel barrel = getTE(worldIn, pos);

        if (barrel == null) {
            return false;
        }

        if (worldIn.isRemote) {
            return true;
        }

        playerIn.openGui(Survivalism.INSTANCE, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!stack.hasTagCompound()) {
            worldIn.setBlockState(pos, state.withProperty(BARREL_STATE, StorageEnumsBarrelStates.VALUES[0]), 2);
            worldIn.setBlockState(pos, state.withProperty(SEALED_STATE, false));
        } else {
            NBTTagCompound compound = stack.getTagCompound();
            TileBarrel te = getTE(worldIn, pos);
            if (compound != null && te != null) {
                worldIn.setBlockState(pos, state.withProperty(BARREL_STATE, StorageEnumsBarrelStates.valueOf(compound.getString("barrel_state"))));
                worldIn.setBlockState(pos, state.withProperty(SEALED_STATE, compound.getBoolean("sealed")));
                te.deserializeNBT(compound);
            }

        }

    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getStateFromMeta(meta).withProperty(AXIS, BlockLog.EnumAxis.fromFacingAxis(EnumFacing.getDirectionFromEntityLiving(pos, placer).getAxis()));
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch (state.getValue(AXIS)) {
                    case X:
                        return state.withProperty(AXIS, BlockLog.EnumAxis.Z);
                    case Z:
                        return state.withProperty(AXIS, BlockLog.EnumAxis.X);
                    case Y:
                        return state;
                }

            default:
                return state;
        }
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BARREL_STATE, StorageEnumsBarrelStates.VALUES[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BARREL_STATE).ordinal();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{BARREL_STATE, AXIS, SEALED_STATE}, new IUnlistedProperty[]{PropertySideType.SIDE_TYPE[0], PropertySideType.SIDE_TYPE[1],
                PropertySideType.SIDE_TYPE[2], PropertySideType.SIDE_TYPE[3],
                PropertySideType.SIDE_TYPE[4], PropertySideType.SIDE_TYPE[5]});
    }


}
