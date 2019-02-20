package com.teamacronymcoders.survivalism.common.blocks.barrels;

import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBrewing;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelSoaking;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelStorage;
import com.teamacronymcoders.survivalism.utils.SurvivalismStorage;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockBarrelStorage extends BlockBarrelBase {

    public BlockBarrelStorage() {
        super("barrel_storage");
        setTranslationKey("barrel_storage");
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBarrelStorage();
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileBarrelStorage.class;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {}

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileBarrelStorage) {
            TileBarrelStorage storage = (TileBarrelStorage) te;
            if (stack.getTagCompound() != null) {
                NBTTagCompound compound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
                storage.getInv().deserializeNBT(compound.getCompoundTag("items"));
                storage.getInput().readFromNBT(compound.getCompoundTag("inputTank"));
            }
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof TileBarrelStorage) {
            TileBarrelStorage storage = (TileBarrelStorage) te;
            stack = new ItemStack(this, 1, getMetaFromState(state));
            if (state.getValue(SEALED)) {
                NBTTagCompound tag = new NBTTagCompound();
                storage.writeToNBT(tag);
                stack.setTagCompound(new NBTTagCompound());
                if (stack.getTagCompound() != null) {
                    stack.getTagCompound().setTag("BlockEntityTag", tag);
                }
            } else {
                for (int i = 0; i < storage.getInv().getSlots(); i++) {
                    ItemStack iStack = storage.getInv().getStackInSlot(i);
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

        if (barrel instanceof TileBarrelStorage) {
            TileBarrelStorage storage = (TileBarrelStorage) barrel;
            if (!state.getValue(SEALED)) {
                if (playerIn.getHeldItem(hand).getItem() == BlockBarrelBase.SPONGE) {
                    ItemStack stack = playerIn.getHeldItem(hand);
                    storage.getInput().setFluid(null);
                    if (!playerIn.capabilities.isCreativeMode) {
                        stack.shrink(1);
                        playerIn.inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(Blocks.SPONGE), 1, 1));
                    }
                    return true;
                } else if (playerIn.getHeldItem(hand).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                    FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, null);
                    return true;
                }
            }
            storage.onBlockActivated(playerIn);
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
                    FluidTank fluidTank = new FluidTank(SurvivalismStorage.TANK_CAPACITY);
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
}
