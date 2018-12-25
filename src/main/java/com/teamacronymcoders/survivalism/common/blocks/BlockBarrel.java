package com.teamacronymcoders.survivalism.common.blocks;

import com.teamacronymcoders.base.blocks.properties.PropertySideType;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.defaults.BlockDefault;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.storages.EnumsBarrelStates;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;
import java.util.Objects;

public class BlockBarrel extends BlockDefault {

    public static final PropertyEnum<EnumsBarrelStates> BARREL_STATE = PropertyEnum.create("barrel_state", EnumsBarrelStates.class);
    private static final int GUI_ID = 1;
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockBarrel() {
        super(Material.WOOD);
        setCreativeTab(Survivalism.TAB);
        setUnlocalizedName(Survivalism.MODID + ".barrel");
        setRegistryName(Survivalism.MODID, "barrel");
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileBarrel barrel = getTE(worldIn, pos);
        ItemStack stack = playerIn.getHeldItem(hand);

        if (worldIn.isRemote) {
            return true;
        }
        if (barrel == null) {
            return false;
        }

        if (!playerIn.getHeldItem(hand).isEmpty() && FluidUtil.getFluidHandler(stack) != null) {
            return FluidUtil.interactWithFluidHandler(playerIn, hand, Objects.requireNonNull(barrel.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)));
        } else if (playerIn.isSneaking()) {
            Survivalism.logger.error(barrel.getTankBase().getFluid().getLocalizedName() + ":" + barrel.getTankBase().getFluidAmount());
        } else if (!playerIn.isSneaking()) {
            playerIn.openGui(Survivalism.INSTANCE, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(BARREL_STATE, EnumsBarrelStates.VALUES[0]), 2);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BARREL_STATE, EnumsBarrelStates.VALUES[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BARREL_STATE).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{BARREL_STATE}, new IUnlistedProperty[]{PropertySideType.SIDE_TYPE[0], PropertySideType.SIDE_TYPE[1],
                PropertySideType.SIDE_TYPE[2], PropertySideType.SIDE_TYPE[3],
                PropertySideType.SIDE_TYPE[4], PropertySideType.SIDE_TYPE[5]});
    }


}
