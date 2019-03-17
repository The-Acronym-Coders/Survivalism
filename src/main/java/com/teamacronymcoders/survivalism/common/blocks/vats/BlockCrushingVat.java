package com.teamacronymcoders.survivalism.common.blocks.vats;

import com.teamacronymcoders.base.blocks.BlockTEBase;
import com.teamacronymcoders.base.util.Coloring;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileCrushingVat;
import com.teamacronymcoders.survivalism.compat.theoneprobe.TOPInfoProvider;
import com.teamacronymcoders.survivalism.modules.recipes.thermalfoundation.TFPHelper;
import com.teamacronymcoders.survivalism.utils.SurvivalismReferenceObjects;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.event.CrushingEvent;
import com.teamacronymcoders.survivalism.utils.helpers.HelperString;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockCrushingVat extends BlockTEBase<TileCrushingVat> implements TOPInfoProvider {

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

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("survivalism:crushing_vat", "inventory"));
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
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        TileCrushingVat vat = getTE(world, pos);
        if (vat != null && entity instanceof EntityLivingBase) {
            if (MinecraftForge.EVENT_BUS.post(new CrushingEvent.Pre((EntityLivingBase) entity, vat))) {
                vat.onJump((EntityLivingBase) entity, vat);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileCrushingVat vat = getTE(world, pos);
        ItemStack held = player.getHeldItem(hand);

        if (vat == null) {
            return false;
        }

        if (world.isRemote) {
            return true;
        }

        // Tank > Holder : Holder > Tank
        if (held.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            FluidUtil.interactWithFluidHandler(player, hand, world, pos, null);
            vat.markDirty();
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 8);
            return true;
        }

        // Reset Tanks
        if (held.getItem() == SurvivalismReferenceObjects.SPONGE) {
            ItemStack stack = player.getHeldItem(hand);
            vat.getTank().setFluid(null);
            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
                player.inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(Blocks.SPONGE), 1, 1));
            }
            vat.markDirty();
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 8);
            return true;
        }

        // Potion Fluid Module
        if (Survivalism.INSTANCE.getModuleHandler().isModuleEnabled("Potion Fluids")) {
            // Potion Tank > Bottle
            if (held.getItem() instanceof ItemGlassBottle) {
                if (vat.getTank().getFluid() != null) {
                    if (FluidRegistry.isFluidRegistered("potion")) {
                        if (TFPHelper.isPotion(vat.getTank().getFluid())) {
                            String id = vat.getTank().getFluid().tag.getString("Potion");
                            PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(id));
                            if (type != null) {
                                ItemStack potion = new ItemStack(Items.POTIONITEM);
                                PotionUtils.addPotionToItemStack(potion, type);
                                held.shrink(1);
                                vat.getTank().drainInternal(SurvivalismConfigs.potionToBottleDrainAmount, true);
                                player.inventory.addItemStackToInventory(potion);
                            }
                            vat.markDirty();
                            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 8);
                            return true;
                        }
                    }
                    if (FluidRegistry.isFluidRegistered("potion_splash")) {
                        if (TFPHelper.isSplashPotion(vat.getTank().getFluid())) {
                            String id = vat.getTank().getFluid().tag.getString("Potion");
                            PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(id));
                            if (type != null) {
                                ItemStack potion = new ItemStack(Items.SPLASH_POTION);
                                PotionUtils.addPotionToItemStack(potion, type);
                                held.shrink(1);
                                vat.getTank().drainInternal(SurvivalismConfigs.potionToBottleDrainAmount, true);
                                player.inventory.addItemStackToInventory(potion);
                            }
                            vat.markDirty();
                            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 8);
                            return true;
                        }
                    }
                    if (FluidRegistry.isFluidRegistered("potion_lingering")) {
                        if (TFPHelper.isLingeringPotion(vat.getTank().getFluid())) {
                            String id = vat.getTank().getFluid().tag.getString("Potion");
                            PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(id));
                            if (type != null) {
                                ItemStack potion = new ItemStack(Items.LINGERING_POTION);
                                PotionUtils.addPotionToItemStack(potion, type);
                                held.shrink(1);
                                vat.getTank().drainInternal(SurvivalismConfigs.potionToBottleDrainAmount, true);
                                player.inventory.addItemStackToInventory(potion);
                            }
                            vat.markDirty();
                            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 8);
                            return true;
                        }
                    }
                }
            }
        }

        vat.onBlockActivated(player);
        return false;
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


    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof TileCrushingVat) {
            TileCrushingVat vat = (TileCrushingVat) te;
            FluidStack output = vat.getTank().getFluid();
            if (output != null) {
                probeInfo.text("Tank: " + output.getLocalizedName() + ": " + output.amount + " / " + vat.getTank().getCapacity());
            }
            if (vat.getRecipe() != null) {
                double crushing = vat.jumps;
                probeInfo.horizontal().text("Crushing Left: " + crushing + " / " + vat.getRecipe().getJumps());
            }
        }
    }
}
