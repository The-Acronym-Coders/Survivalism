package com.teamacronymcoders.survivalism.common.blocks.vats;

import com.teamacronymcoders.base.blocks.BlockTEBase;
import com.teamacronymcoders.base.util.Coloring;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.items.ItemMixingSpoon;
import com.teamacronymcoders.survivalism.common.tiles.vats.TileMixingVat;
import com.teamacronymcoders.survivalism.compat.theoneprobe.TOPInfoProvider;
import com.teamacronymcoders.survivalism.modules.recipes.thermalfoundation.TFPHelper;
import com.teamacronymcoders.survivalism.utils.SurvivalismReferenceObjects;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.helpers.HelperString;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
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
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMixingVat extends BlockTEBase<TileMixingVat> implements TOPInfoProvider {

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

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("survivalism:mixing_vat", "inventory"));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileMixingVat vat = getTE(worldIn, pos);
        ItemStack held = playerIn.getHeldItem(hand);

        if (vat == null) {
            return false;
        }

        if (worldIn.isRemote) {
            return true;
        }

        // Mix the Mixing Vat
        if (held.getItem() instanceof ItemMixingSpoon) {
            if (vat.onMix()) {
                held.damageItem(1, playerIn);
                vat.markDirty();
            }
            vat.markDirty();
            return true;
        }

        // Tank > Holder : Holder > Tank
        if (held.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, null);
            vat.markDirty();
            worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
            return true;
        }

        // Reset Tanks
        if (held.getItem() == SurvivalismReferenceObjects.SPONGE) {
            ItemStack stack = playerIn.getHeldItem(hand);
            vat.getMain().setFluid(null);
            vat.getSecondary().setFluid(null);
            vat.getOutput().setFluid(null);
            if (!playerIn.capabilities.isCreativeMode) {
                stack.shrink(1);
                playerIn.inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(Blocks.SPONGE), 1, 1));
            }
            vat.markDirty();
            worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
            return true;
        }

        // Potion Fluid Module
        if (Survivalism.INSTANCE.getModuleHandler().isModuleEnabled("Potion Fluids")) {
            // Potion Tank > Bottle
            if (held.getItem() instanceof ItemGlassBottle) {
                if (vat.getOutput().getFluid() != null) {
                    if (FluidRegistry.isFluidRegistered("potion")) {
                        if (TFPHelper.isPotion(vat.getOutput().getFluid())) {
                            String id = vat.getOutput().getFluid().tag.getString("Potion");
                            PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(id));
                            if (type != null) {
                                ItemStack potion = new ItemStack(Items.POTIONITEM);
                                PotionUtils.addPotionToItemStack(potion, type);
                                held.shrink(1);
                                vat.getOutput().drainInternal(SurvivalismConfigs.potionToBottleDrainAmount, true);
                                playerIn.inventory.addItemStackToInventory(potion);
                            }
                            vat.markDirty();
                            worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                            return true;
                        }
                    }
                    if (FluidRegistry.isFluidRegistered("potion_splash")) {
                        if (TFPHelper.isSplashPotion(vat.getOutput().getFluid())) {
                            String id = vat.getOutput().getFluid().tag.getString("Potion");
                            PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(id));
                            if (type != null) {
                                ItemStack potion = new ItemStack(Items.SPLASH_POTION);
                                PotionUtils.addPotionToItemStack(potion, type);
                                held.shrink(1);
                                vat.getOutput().drainInternal(SurvivalismConfigs.potionToBottleDrainAmount, true);
                                playerIn.inventory.addItemStackToInventory(potion);
                            }
                            vat.markDirty();
                            worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                            return true;
                        }
                    }
                    if (FluidRegistry.isFluidRegistered("potion_lingering")) {
                        if (TFPHelper.isLingeringPotion(vat.getOutput().getFluid())) {
                            String id = vat.getOutput().getFluid().tag.getString("Potion");
                            PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(id));
                            if (type != null) {
                                ItemStack potion = new ItemStack(Items.LINGERING_POTION);
                                PotionUtils.addPotionToItemStack(potion, type);
                                held.shrink(1);
                                vat.getOutput().drainInternal(SurvivalismConfigs.potionToBottleDrainAmount, true);
                                playerIn.inventory.addItemStackToInventory(potion);
                            }
                            vat.markDirty();
                            worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                            return true;
                        }
                    }
                }
            }

            // Potion Bottle > Tank
            if (FluidRegistry.isFluidRegistered("potion") && FluidRegistry.isFluidRegistered("potion_splash") && FluidRegistry.isFluidRegistered("potion_lingering")) {
                if (held.getItem().equals(Items.POTIONITEM) || held.getItem().equals(Items.SPLASH_POTION) || held.getItem().equals(Items.LINGERING_POTION)) {
                    if (held.getTagCompound() != null && !held.getTagCompound().isEmpty() && playerIn.getHeldItem(hand).getTagCompound().hasKey("Potion")) {
                        String id = held.getTagCompound().getString("Potion");
                        PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(id));
                        if (held.getItem().equals(Items.POTIONITEM)) {
                            FluidStack fluid = TFPHelper.getPotion(SurvivalismConfigs.potionToBottleDrainAmount, type);
                            fillTanksWithFluid(vat, fluid);
                            vat.markDirty();
                            worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                            return true;
                        } else if (held.getItem().equals(Items.SPLASH_POTION)) {
                            FluidStack fluid = TFPHelper.getSplashPotion(SurvivalismConfigs.potionToBottleDrainAmount, type);
                            fillTanksWithFluid(vat, fluid);
                            vat.markDirty();
                            worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                            return true;
                        } else if (held.getItem().equals(Items.LINGERING_POTION)) {
                            FluidStack fluid = TFPHelper.getLingeringPotion(SurvivalismConfigs.potionToBottleDrainAmount, type);
                            fillTanksWithFluid(vat, fluid);
                            vat.markDirty();
                            worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                            return true;
                        }
                        if (!playerIn.capabilities.isCreativeMode) {
                            held.shrink(1);
                            playerIn.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
                        }
                    }
                }
            }
        }
        vat.onBlockActivated(playerIn);
        return true;
    }

    private static void fillTanksWithFluid(TileMixingVat vat, FluidStack fluid) {
        if (vat.getMain().getFluid() == null) {
            vat.getMain().fillInternal(fluid, true);
        } else if (vat.getSecondary().getFluid() == null) {
            vat.getSecondary().fillInternal(fluid, true);
        } else if (vat.getMain().getFluid().containsFluid(fluid)) {
            vat.getMain().fillInternal(fluid, true);
        } else if (vat.getSecondary().getFluid().containsFluid(fluid)) {
            vat.getSecondary().fillInternal(fluid, true);
        }
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

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof TileMixingVat) {
            TileMixingVat vat = (TileMixingVat) te;
            FluidStack main = vat.getMain().getFluid();
            FluidStack secondary = vat.getSecondary().getFluid();
            FluidStack output = vat.getOutput().getFluid();
            probeInfo.text("Working: " + vat.isWorking());
            if (main != null) {
                probeInfo.text(I18n.format("survivalism.hwyla.fluids.main") + " " + main.getLocalizedName() + " - " + main.amount + " / " + vat.getMain().getCapacity());
            }
            if (secondary != null) {
                probeInfo.text(I18n.format("survivalism.hwyla.fluids.secondary") + " " + secondary.getLocalizedName() + " - " + secondary.amount + " / " + vat.getSecondary().getCapacity());
            }
            if (output != null) {
                probeInfo.text(I18n.format("survivalism.hwyla.fluids.output") + " " + output.getLocalizedName() + " - " + output.amount + " / " + vat.getOutput().getCapacity());
            }
            if (vat.getRecipe() != null) {
                if (vat.isWorking()) {
                    probeInfo.horizontal().text(I18n.format("survivalism.hwyla.stirs") + " " + (vat.getRecipe().getClicks() - vat.getClicks()));
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().borderColor(Coloring.fromHex("c19a6b").getIntColor())).progress(vat.getClicks(), vat.getRecipe().getClicks(), probeInfo.defaultProgressStyle().borderColor(Coloring.fromHex("c19a6b").getIntColor()).showText(false).alternateFilledColor(Coloring.fromHex("6b92c1").getIntColor()));
                }
            }
        }
    }
}
