package com.teamacronymcoders.survivalism.common.blocks;

import com.teamacronymcoders.base.blocks.BlockBase;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.ModBlocks;
import com.teamacronymcoders.survivalism.common.recipe.CompostMaterialList;
import com.teamacronymcoders.survivalism.common.tiles.TileCompostBin;
import com.teamacronymcoders.survivalism.compat.theoneprobe.TOPInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockCompostBin extends BlockBase implements TOPInfoProvider {
    public static PropertyInteger Compost_Amount = PropertyInteger.create("compost", 0, 11);

    public BlockCompostBin() {
        super(Material.WOOD);
        setCreativeTab(Survivalism.TAB);
        setTranslationKey("compost_bin");
        setSoundType(SoundType.WOOD);
        setLightOpacity(0);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.blockCompostBin), 0, new ModelResourceLocation("survivalism:compost_bin", "inventory"));
    }

    private TileCompostBin getTE(World world, BlockPos pos) {
        if (world.getTileEntity(pos) != null) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileCompostBin) {
                return (TileCompostBin) te;
            }
        }
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileCompostBin bin = getTE(worldIn, pos);

        if (bin == null) {
            return false;
        }

        if (worldIn.isRemote) {
            return true;
        }

        ItemStack materialStack = playerIn.getHeldItem(hand);

        for (Ingredient ingredient : CompostMaterialList.getCompostMaterials()) {
            if (ingredient.apply(materialStack)) {
                if (state.getValue(Compost_Amount) < 10) {
                    materialStack.shrink(1);
                }
                bin.addCompost(playerIn);
                break;
            }
        }

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
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(Compost_Amount, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(Compost_Amount);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCompostBin();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, Compost_Amount);
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
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 0.7d, 1.0d);
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        probeInfo.text(TextFormatting.GREEN + "Compost: " + blockState.getValue(Compost_Amount));
    }
}
