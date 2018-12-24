package com.teamacronymcoders.survivalism.common.blocks;

import com.teamacronymcoders.base.blocks.properties.PropertySideType;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.defaults.BlockDefault;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.helpers.FluidHelper;
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
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class BlockBarrel extends BlockDefault {

    private static final int GUI_ID = 1;
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum<EnumsBarrelStates> BARREL_STATE = PropertyEnum.create("barrel_state", EnumsBarrelStates.class);

    public BlockBarrel() {
        super(Material.WOOD);
        setCreativeTab(Survivalism.TAB);
        setUnlocalizedName(Survivalism.MODID + ".barrel");
        setRegistryName(Survivalism.MODID, "barrel");
        setSoundType(SoundType.WOOD);
        setLightOpacity(255);
    }

    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBarrel();
    }

    private TileBarrel getTE(World world, BlockPos pos) {
        return (TileBarrel) world.getTileEntity(pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity tile = worldIn.getTileEntity(pos);
        if (!(tile instanceof TileBarrel)) {
            return false;
        }

        if (playerIn.isSneaking()) {
            if (playerIn.getHeldItem(hand).getItem() instanceof ItemBucket) {
                ItemStack stack = playerIn.getHeldItem(hand);
                TileBarrel tb = getTE(worldIn, pos);

                FluidStack stackFS = FluidHelper.getFluidStackFromHandler(stack);
                FluidStack tileFS = FluidHelper.getFluidStackFromHandler(tb);

                if (FluidHelper.isFluidStacksSame(stackFS, tileFS)) {
                    tileFS.amount = (stackFS.amount + tileFS.amount);
                }
            }
        }

        playerIn.openGui(Survivalism.INSTANCE, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront((meta & 3) + 2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() - 2;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{BARREL_STATE}, new IUnlistedProperty[]{PropertySideType.SIDE_TYPE[0], PropertySideType.SIDE_TYPE[1],
                PropertySideType.SIDE_TYPE[2], PropertySideType.SIDE_TYPE[3],
                PropertySideType.SIDE_TYPE[4], PropertySideType.SIDE_TYPE[5]});
    }
}
