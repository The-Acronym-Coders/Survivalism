package com.teamacronymcoders.survivalism.common.blocks.barrels;

import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelBase;
import com.teamacronymcoders.survivalism.common.tiles.barrels.TileBarrelSoaking;
import com.teamacronymcoders.survivalism.utils.SurvivalismStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockBarrelSoaking extends BlockBarrelBase {

    public BlockBarrelSoaking() {
        super("barrel_soaking", new TileBarrelSoaking());
        setTranslationKey("barrel_soaking");
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileBarrelBase barrel = getTE(worldIn, pos);
        if (worldIn.isRemote || barrel == null) {
            return true;
        }

        if (barrel instanceof TileBarrelSoaking) {
            TileBarrelSoaking brewing = (TileBarrelSoaking) barrel;
            if (!state.getValue(SEALED)) {
                if (playerIn.getHeldItem(hand).getItem() == BlockBarrelBase.SPONGE) {
                    ItemStack stack = playerIn.getHeldItem(hand);
                    brewing.getInput().setFluid(null);
                    if (!playerIn.capabilities.isCreativeMode) {
                        stack.shrink(1);
                        playerIn.inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(Blocks.SPONGE), 1, 1));
                    }
                    if (worldIn.isRemote) {
                        brewing.updateClientInputFluid(brewing.getInput());
                    }
                    return true;
                } else if (playerIn.getHeldItem(hand).getItem() instanceof ItemBucket) {
                    ItemStack stack = playerIn.getHeldItem(hand);
                    FluidStack fs = FluidUtil.getFluidContained(stack);
                    if (fs != null) {
                        if (fs.amount == 0) {
                            FluidUtil.tryFillContainer(stack, FluidUtil.getFluidHandler(worldIn, pos, EnumFacing.DOWN), Integer.MAX_VALUE, playerIn, true);
                        }
                        if (fs.amount == 1000) {
                            FluidUtil.tryEmptyContainer(stack, FluidUtil.getFluidHandler(worldIn, pos, null), Integer.MAX_VALUE, playerIn, true);
                        }
                    }
                    return true;
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBarrelSoaking();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta == 0) {
            return getDefaultState().withProperty(SEALED, false);
        } else {
            return getDefaultState().withProperty(SEALED, true);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(SEALED) ? 1 : 0;
    }

    @Override
    public void getSubBlocks(@Nullable CreativeTabs creativeTab, @Nonnull NonNullList<ItemStack> list) {
        for (IBlockState state : getBlockState().getValidStates()) {
            list.add(new ItemStack(this, 1, getMetaFromState(state)));
        }
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
