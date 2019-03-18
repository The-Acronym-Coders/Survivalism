package com.teamacronymcoders.survivalism.common.blocks.barrels;

import com.teamacronymcoders.base.util.Coloring;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.render.BarrelTESR;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelSoaking;
import com.teamacronymcoders.survivalism.compat.theoneprobe.TOPInfoProvider;
import com.teamacronymcoders.survivalism.modules.recipes.thermalfoundation.TFPHelper;
import com.teamacronymcoders.survivalism.utils.SurvivalismReferenceObjects;
import com.teamacronymcoders.survivalism.utils.configs.SurvivalismConfigs;
import com.teamacronymcoders.survivalism.utils.helpers.StringHelper;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBarrelSoaking extends BlockBarrelBase implements TOPInfoProvider {

    public BlockBarrelSoaking() {
        super("barrel_soaking");
        setTranslationKey("barrel_soaking");
    }

    @SideOnly(Side.CLIENT)
    public void initModels() {
        NonNullList<ItemStack> items = NonNullList.create();
        this.getSubBlocks(CreativeTabs.SEARCH, items);
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            ModelLoader.setCustomModelResourceLocation(item.getItem(), i, new ModelResourceLocation("survivalism:barrel_soaking", "inventory"));
        }
        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrelSoaking.class, new BarrelTESR());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBarrelSoaking();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileBarrelSoaking.class;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileBarrelSoaking) {
            TileBarrelSoaking soaking = (TileBarrelSoaking) te;
            if (stack.getTagCompound() != null) {
                NBTTagCompound compound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
                soaking.getInv().deserializeNBT(compound.getCompoundTag("items"));
                soaking.getInput().readFromNBT(compound.getCompoundTag("inputTank"));
            }
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof TileBarrelSoaking) {
            TileBarrelSoaking soaking = (TileBarrelSoaking) te;
            stack = new ItemStack(this, 1, getMetaFromState(state));
            if (state.getValue(SEALED)) {
                NBTTagCompound tag = new NBTTagCompound();
                soaking.writeToNBT(tag);
                stack.setTagCompound(new NBTTagCompound());
                if (stack.getTagCompound() != null) {
                    stack.getTagCompound().setTag("BlockEntityTag", tag);
                }
            } else {
                for (int i = 0; i < soaking.getInv().getSlots(); i++) {
                    ItemStack iStack = soaking.getInv().getStackInSlot(i);
                    InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), iStack);
                }
            }
            Block.spawnAsEntity(worldIn, pos, stack);
        } else {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileBarrelBase barrel = getTE(worldIn, pos);
        if (worldIn.isRemote || barrel == null) {
            return true;
        }

        if (barrel instanceof TileBarrelSoaking) {
            TileBarrelSoaking soaking = (TileBarrelSoaking) barrel;
            ItemStack held = playerIn.getHeldItem(hand);
            if (!state.getValue(SEALED)) {
                // Tank > Holder : Holder > Tank
                if (playerIn.getHeldItem(hand).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                    FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, null);
                    soaking.markDirty();
                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                    return true;
                }

                // Reset Tank
                if (playerIn.getHeldItem(hand).getItem() == SurvivalismReferenceObjects.SPONGE) {
                    ItemStack stack = playerIn.getHeldItem(hand);
                    soaking.getInput().setFluid(null);
                    if (!playerIn.capabilities.isCreativeMode) {
                        stack.shrink(1);
                        playerIn.inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(Blocks.SPONGE), 1, 1));
                    }
                    soaking.markDirty();
                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                    return true;
                }

                // Potion Fluid Module
                if (Survivalism.INSTANCE.getModuleHandler().isModuleEnabled("Potion Fluids")) {
                    // Potion Bottle > Tank
                    if (FluidRegistry.isFluidRegistered("potion") && FluidRegistry.isFluidRegistered("potion_splash") && FluidRegistry.isFluidRegistered("potion_lingering")) {
                        if (held.getItem().equals(Items.POTIONITEM) || held.getItem().equals(Items.SPLASH_POTION) || held.getItem().equals(Items.LINGERING_POTION)) {
                            if (held.getTagCompound() != null && !held.getTagCompound().isEmpty() && playerIn.getHeldItem(hand).getTagCompound().hasKey("Potion")) {
                                String id = held.getTagCompound().getString("Potion");
                                PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(id));
                                if (held.getItem().equals(Items.POTIONITEM)) {
                                    FluidStack fluid = TFPHelper.getPotion(SurvivalismConfigs.potionToBottleDrainAmount, type);
                                    soaking.getInput().fillInternal(fluid, true);
                                    soaking.markDirty();
                                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                                    return true;
                                } else if (held.getItem().equals(Items.SPLASH_POTION)) {
                                    FluidStack fluid = TFPHelper.getSplashPotion(SurvivalismConfigs.potionToBottleDrainAmount, type);
                                    soaking.getInput().fillInternal(fluid, true);
                                    soaking.markDirty();
                                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                                    return true;
                                } else if (held.getItem().equals(Items.LINGERING_POTION)) {
                                    FluidStack fluid = TFPHelper.getLingeringPotion(SurvivalismConfigs.potionToBottleDrainAmount, type);
                                    soaking.getInput().fillInternal(fluid, true);
                                    soaking.markDirty();
                                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            soaking.onBlockActivated(playerIn);
            return true;
        }
        return false;
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            IBlockState state = getStateFromMeta(stack.getMetadata());
            tooltip.add(TextFormatting.GRAY + I18n.format("state.sealed") + " " + TextFormatting.WHITE + state.getValue(SEALED));
        } else {
            tooltip.add(TextFormatting.GRAY + I18n.format("info.survivalism.shift"));
        }

        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound compound = stack.getTagCompound();
            NBTTagCompound tag = stack.getSubCompound("BlockEntityTag");


            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (compound != null && tag.hasKey("inputTank")) {
                    FluidTank fluidTank = new FluidTank(SurvivalismConfigs.soakingTankSize);
                    FluidStack fluidStack = fluidTank.readFromNBT(tag.getCompoundTag("inputTank")).getFluid();
                    if (fluidStack != null) {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            tooltip.add(TextFormatting.GRAY + "Input: " + TextFormatting.WHITE + fluidStack.getLocalizedName() + ": " + fluidStack.amount);
                        } else if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            tooltip.add(TextFormatting.GRAY + "Press Shift + L-Ctrl for Fluid Information");
                        }
                    }
                }

                if (compound != null && tag.hasKey("items")) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                        int c = 0;
                        int d = 0;

                        NonNullList<ItemStack> nonNullList = NonNullList.withSize(9, ItemStack.EMPTY);
                        ItemStackHelper.loadAllItems(tag.getCompoundTag("items"), nonNullList);

                        tooltip.add("Inventory:");

                        for (ItemStack itemstack : nonNullList) {
                            if (!itemstack.isEmpty()) {
                                ++d;
                                if (c <= 4) {
                                    ++c;
                                    tooltip.add(String.format(" %dx %s", itemstack.getCount(), itemstack.getDisplayName()));
                                }
                            }
                        }
                        if (d - c > 0) {
                            tooltip.add(" " + TextFormatting.ITALIC + I18n.format("container.shulkerBox.more", d - c));
                        }
                    } else if (!Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                        tooltip.add(TextFormatting.GRAY + "Press Shift + R-Ctrl for Item Information");
                    }
                }
            } else if (!tooltip.contains(TextFormatting.GRAY + I18n.format("info.survivalism.shift"))) {
                tooltip.add(TextFormatting.GRAY + I18n.format("info.survivalism.shift"));
            }
        }
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        probeInfo.text("Sealed: " + blockState.getValue(BlockBarrelBase.SEALED));
        if (te instanceof TileBarrelSoaking) {
            TileBarrelSoaking soaking = (TileBarrelSoaking) te;
            FluidStack input = soaking.getInput().getFluid();
            probeInfo.text("Working: " + soaking.getWorking());
            if (input != null) {
                probeInfo.text("Input: " + input.getLocalizedName() + ": " + input.amount + " / " + soaking.getInput().getCapacity());
            }
            if (soaking.getRecipe() != null) {
                int ticksLeft = (soaking.getRecipe().getTicks() - soaking.getTicks()) / 20;
                if (soaking.getWorking()) {
                    probeInfo.horizontal().text("Time Left: " + StringHelper.getDurationString(ticksLeft));
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().borderColor(Coloring.fromHex("c19a6b").getIntColor())).progress(soaking.getTicks(), soaking.getRecipe().getTicks(), probeInfo.defaultProgressStyle().borderColor(Coloring.fromHex("c19a6b").getIntColor()).showText(false).alternateFilledColor(Coloring.fromHex("6b92c1").getIntColor()));
                }
            }
        }
    }
}
