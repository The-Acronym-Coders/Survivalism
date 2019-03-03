package com.teamacronymcoders.survivalism.common.blocks.vats;

import com.teamacronymcoders.base.blocks.BlockTEBase;
import com.teamacronymcoders.base.guisystem.GuiOpener;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBase;
import com.teamacronymcoders.survivalism.common.items.ItemMixingSpoon;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileMixingVat;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMixingVat extends BlockTEBase<TileMixingVat> {

    public BlockMixingVat() {
        super(Material.WOOD, "mixing_vat");
        setTranslationKey("mixing_vat");
        setLightOpacity(0);
    }

    @Nullable
    public static TileMixingVat getTE(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileMixingVat ? (TileMixingVat) te : null;
    }

    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("survivalism:mixing_vat", "inventory"));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileMixingVat vat = getTE(worldIn, pos);
        if (vat == null) {
            return false;
        }

        if (worldIn.isRemote) {
            return true;
        }

        // Mix the Mixing Vat
        ItemStack stack = playerIn.getHeldItem(hand);
        if (stack.getItem() instanceof ItemMixingSpoon) {
            if (vat.onMix()) {
                stack.damageItem(1, playerIn);
            }
            return true;
        }

        // Clears Input Tanks using a Sponge
        if (stack.getItem() == BlockBarrelBase.SPONGE) {
            vat.getMain().setFluid(null);
            vat.getSecondary().setFluid(null);
            if (!playerIn.capabilities.isCreativeMode) {
                stack.shrink(1);
                playerIn.inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(Blocks.SPONGE), 1, 1));
            }
            return true;
        }

        // Allows the Vat to Be Filled
        if (playerIn.getHeldItem(hand).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, null);
            return true;
        }

        if (stack.getItem().equals(Item.getItemFromBlock(Blocks.BEDROCK))) {
            vat.getMain().setFluid(FluidRegistry.getFluidStack("water", 1000));
            vat.getSecondary().setFluid(FluidRegistry.getFluidStack("lava", 1000));
            vat.getOutput().setFluid(FluidRegistry.getFluidStack("water", 2000));
            return true;
        }

        vat.onBlockActivated(playerIn);
        return true;
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
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileMixingVat te = getTE(world, pos);
        if (te != null) {
            if (!te.getHandler().getStackInSlot(0).isEmpty()) {
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), te.getHandler().getStackInSlot(0));
            }
        }
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 1, 0.875, 1);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState blockState) {
        return new TileMixingVat();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileMixingVat.class;
    }
}
