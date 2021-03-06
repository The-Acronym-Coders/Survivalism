package com.teamacronymcoders.survivalism.common.blocks.barrels;

import com.teamacronymcoders.base.util.Coloring;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.client.render.BarrelTESR;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import com.teamacronymcoders.survivalism.compat.theoneprobe.TOPInfoProvider;
import com.teamacronymcoders.survivalism.modules.recipes.thermalfoundation.PotionHelper;
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
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
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

public class BlockBarrelBrewing extends BlockBarrelBase implements TOPInfoProvider {

    public BlockBarrelBrewing() {
        super("barrel_brewing");
        setTranslationKey("barrel_brewing");
    }

    @SideOnly(Side.CLIENT)
    public void initModels() {
        NonNullList<ItemStack> items = NonNullList.create();
        this.getSubBlocks(CreativeTabs.SEARCH, items);
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            ModelLoader.setCustomModelResourceLocation(item.getItem(), i, new ModelResourceLocation("survivalism:barrel_brewing", "inventory"));
        }
        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrelBrewing.class, new BarrelTESR());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBarrelBrewing();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileBarrelBrewing.class;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileBarrelBrewing) {
            TileBarrelBrewing brewing = (TileBarrelBrewing) te;
            if (stack.getTagCompound() != null) {
                NBTTagCompound compound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
                brewing.getInv().deserializeNBT(compound.getCompoundTag("items"));
                brewing.getInput().readFromNBT(compound.getCompoundTag("inputTank"));
                brewing.getOutput().readFromNBT(compound.getCompoundTag("outputTank"));
            }
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof TileBarrelBrewing) {
            TileBarrelBrewing brewing = (TileBarrelBrewing) te;
            stack = new ItemStack(this, 1, getMetaFromState(state));
            if (state.getValue(SEALED)) {
                NBTTagCompound tag = new NBTTagCompound();
                brewing.writeToNBT(tag);
                stack.setTagCompound(new NBTTagCompound());
                if (stack.getTagCompound() != null) {
                    stack.getTagCompound().setTag("BlockEntityTag", tag);
                }
            } else {
                for (int i = 0; i < brewing.getInv().getSlots(); i++) {
                    ItemStack iStack = brewing.getInv().getStackInSlot(i);
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

        if (barrel instanceof TileBarrelBrewing) {
            TileBarrelBrewing brewing = (TileBarrelBrewing) barrel;
            ItemStack held = playerIn.getHeldItem(hand);

            if (!state.getValue(SEALED)) {
                // Tank > Holder : Holder > Tank
                if (held.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                    FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, null);
                    brewing.markDirty();
                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                    return true;
                }

                // Reset Tanks
                if (held.getItem() == SurvivalismReferenceObjects.SPONGE) {
                    ItemStack stack = playerIn.getHeldItem(hand);
                    brewing.getInput().setFluid(null);
                    brewing.getOutput().setFluid(null);
                    if (!playerIn.capabilities.isCreativeMode) {
                        stack.shrink(1);
                        playerIn.inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(Blocks.SPONGE), 1, 1));
                    }
                    brewing.markDirty();
                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                    return true;
                }

                // Potion Fluid Module
                if (Survivalism.INSTANCE.getModuleHandler().isModuleEnabled("Potion Fluids")) {
                    // Potion Tank > Bottle
                    if (held.getItem() instanceof ItemGlassBottle) {
                        if (brewing.getOutput().getFluid() != null) {
                            if (FluidRegistry.isFluidRegistered("potion")) {
                                if (PotionHelper.isPotion(brewing.getOutput().getFluid())) {
                                    String id = brewing.getOutput().getFluid().tag.getString("Potion");
                                    PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(id));
                                    if (type != null) {
                                        ItemStack potion = new ItemStack(Items.POTIONITEM);
                                        PotionUtils.addPotionToItemStack(potion, type);
                                        held.shrink(1);
                                        brewing.getOutput().drainInternal(SurvivalismConfigs.potionToBottleDrainAmount, true);
                                        playerIn.inventory.addItemStackToInventory(potion);
                                    }
                                    brewing.markDirty();
                                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                                    return true;
                                }
                            }
                            if (FluidRegistry.isFluidRegistered("potion_splash")) {
                                if (PotionHelper.isSplashPotion(brewing.getOutput().getFluid())) {
                                    String id = brewing.getOutput().getFluid().tag.getString("Potion");
                                    PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(id));
                                    if (type != null) {
                                        ItemStack potion = new ItemStack(Items.SPLASH_POTION);
                                        PotionUtils.addPotionToItemStack(potion, type);
                                        held.shrink(1);
                                        brewing.getOutput().drainInternal(SurvivalismConfigs.potionToBottleDrainAmount, true);
                                        playerIn.inventory.addItemStackToInventory(potion);
                                    }
                                    brewing.markDirty();
                                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                                    return true;
                                }
                            }
                            if (FluidRegistry.isFluidRegistered("potion_lingering")) {
                                if (PotionHelper.isLingeringPotion(brewing.getOutput().getFluid())) {
                                    String id = brewing.getOutput().getFluid().tag.getString("Potion");
                                    PotionType type = ForgeRegistries.POTION_TYPES.getValue(new ResourceLocation(id));
                                    if (type != null) {
                                        ItemStack potion = new ItemStack(Items.LINGERING_POTION);
                                        PotionUtils.addPotionToItemStack(potion, type);
                                        held.shrink(1);
                                        brewing.getOutput().drainInternal(SurvivalismConfigs.potionToBottleDrainAmount, true);
                                        playerIn.inventory.addItemStackToInventory(potion);
                                    }
                                    brewing.markDirty();
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
                                    FluidStack fluid = PotionHelper.getPotion(SurvivalismConfigs.potionToBottleDrainAmount, type);
                                    brewing.getInput().fillInternal(fluid, true);
                                    brewing.markDirty();
                                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                                    return true;
                                } else if (held.getItem().equals(Items.SPLASH_POTION)) {
                                    FluidStack fluid = PotionHelper.getSplashPotion(SurvivalismConfigs.potionToBottleDrainAmount, type);
                                    brewing.getInput().fillInternal(fluid, true);
                                    brewing.markDirty();
                                    worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 8);
                                    return true;
                                } else if (held.getItem().equals(Items.LINGERING_POTION)) {
                                    FluidStack fluid = PotionHelper.getLingeringPotion(SurvivalismConfigs.potionToBottleDrainAmount, type);
                                    brewing.getInput().fillInternal(fluid, true);
                                    brewing.markDirty();
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
            }
            brewing.onBlockActivated(playerIn);
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
                    FluidTank fluidTank = new FluidTank(SurvivalismConfigs.brewingInputSize);
                    FluidStack fluidStack = fluidTank.readFromNBT(tag.getCompoundTag("inputTank")).getFluid();
                    if (fluidStack != null) {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            tooltip.add(TextFormatting.GRAY + "Input: " + TextFormatting.WHITE + fluidStack.getLocalizedName() + ": " + fluidStack.amount);
                        } else if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            tooltip.add(TextFormatting.GRAY + "Press Shift + L-Ctrl for Fluid Information");
                        }
                    }
                }

                if (compound != null && tag.hasKey("outputTank")) {
                    FluidTank fluidTank = new FluidTank(SurvivalismConfigs.brewingOutputSize);
                    FluidStack fluidStack = fluidTank.readFromNBT(tag.getCompoundTag("outputTank")).getFluid();
                    if (fluidStack != null) {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            tooltip.add(TextFormatting.GRAY + "Output: " + TextFormatting.WHITE + fluidStack.getLocalizedName() + ": " + fluidStack.amount);
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
        if (te instanceof TileBarrelBrewing) {
            TileBarrelBrewing brewing = (TileBarrelBrewing) te;
            FluidStack input = brewing.getInput().getFluid();
            FluidStack output = brewing.getOutput().getFluid();
            probeInfo.text("Working: " + brewing.getWorking());
            if (input != null) {
                probeInfo.text("Input: " + input.getLocalizedName() + ": " + input.amount + " / " + brewing.getInput().getCapacity());
            }
            if (output != null) {
                probeInfo.text("Output: " + output.getLocalizedName() + ": " + output.amount + " / " + brewing.getOutput().getCapacity());
            }
            if (brewing.getRecipe() != null) {
                int ticksLeft = (brewing.getRecipe().getTicks() - brewing.getTicks()) / 20;
                if (brewing.getWorking()) {
                    probeInfo.horizontal().text("Time Left: " + StringHelper.getDurationString(ticksLeft));
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().borderColor(Coloring.fromHex("c19a6b").getIntColor())).progress(brewing.getTicks(), brewing.getRecipe().getTicks(), probeInfo.defaultProgressStyle().borderColor(Coloring.fromHex("c19a6b").getIntColor()).showText(false).alternateFilledColor(Coloring.fromHex("6b92c1").getIntColor()));
                }
            }
        }
    }
}
