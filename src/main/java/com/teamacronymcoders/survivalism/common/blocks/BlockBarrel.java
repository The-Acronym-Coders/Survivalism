package com.teamacronymcoders.survivalism.common.blocks;

import com.teamacronymcoders.base.blocks.BlockBase;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.storages.BarrelState;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockBarrel extends BlockBase {

    public static final PropertyEnum<BarrelState> BARREL_STATE = PropertyEnum.create("barrel_state", BarrelState.class);
    public static final PropertyBool SEALED = PropertyBool.create("sealed");
    public static final int GUI_ID = 1;
    @ObjectHolder("minecraft:sponge")
    public static final Item SPONGE = null;

    public BlockBarrel() {
        super(Material.WOOD);
        setCreativeTab(Survivalism.TAB);
        setTranslationKey(Survivalism.MODID + ".barrel");
        setRegistryName(Survivalism.MODID, "barrel");
        setSoundType(SoundType.WOOD);
        setLightOpacity(0);
    }

    public static TileBarrel getTE(IBlockAccess access, BlockPos pos) {
        TileEntity te = access.getTileEntity(pos);
        if (te instanceof TileBarrel) {
            return (TileBarrel) te;
        }
        return null;
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

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return state.getValue(SEALED);
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return true;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileBarrel te = getTE(world, pos);
        ItemStack stack = new ItemStack(this, 1, getMetaFromState(state));
        if (state.getValue(SEALED)) {
            NBTTagCompound tag = new NBTTagCompound();
            if (te != null) {
                te.writeToNBT(tag);
            }
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setTag("BlockEntityTag", tag);
        } else {
            for (int i = 0; i < te.getInv().getSlots(); i++) {
                ItemStack iStack = te.getInv().getStackInSlot(i);
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), iStack);
            }
        }
        Block.spawnAsEntity(world, pos, stack);
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileBarrel barrel = getTE(worldIn, pos);
        if (worldIn.isRemote || barrel == null) {
            return true;
        }

        if (!state.getValue(SEALED)) {
            if (playerIn.getHeldItem(hand).getItem() == SPONGE) {
                ItemStack stack = playerIn.getHeldItem(hand);
                barrel.getInput().setFluid(null);
                barrel.getOutput().setFluid(null);
                if (!playerIn.capabilities.isCreativeMode) {
                    stack.shrink(1);
                    playerIn.inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(Blocks.SPONGE), 1, 1));
                }
                if (worldIn.isRemote) {
                    barrel.updateClientInputFluid(barrel.getInput());
                    barrel.updateClientOutputFluid(barrel.getOutput());
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

        playerIn.openGui(Survivalism.INSTANCE, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return side != null && side.getAxis() != Axis.Y;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BARREL_STATE, BarrelState.VALUES[meta & 0b11]).withProperty(SEALED, (meta & 0b100) == 0b100);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BARREL_STATE).ordinal() | ((state.getValue(SEALED) ? 1 : 0) << 2);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (IBlockState state : getBlockState().getValidStates()) {
            items.add(new ItemStack(this, 1, getMetaFromState(state)));
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            IBlockState state = getStateFromMeta(stack.getMetadata());
            if (state.getValue(BARREL_STATE).ordinal() == 0) {
                tooltip.add(TextFormatting.GRAY + I18n.format("state.barrel") + " " + TextFormatting.WHITE + I18n.format("state.barrel.storage"));
            } else if (state.getValue(BARREL_STATE).ordinal() == 1) {
                tooltip.add(TextFormatting.GRAY + I18n.format("state.barrel") + " " + TextFormatting.WHITE + I18n.format("state.barrel.brewing"));
            } else if (state.getValue(BARREL_STATE).ordinal() == 2) {
                tooltip.add(TextFormatting.GRAY + I18n.format("state.barrel") + " " + TextFormatting.WHITE + I18n.format("state.barrel.soaking"));
            }

            tooltip.add(TextFormatting.GRAY + I18n.format("state.sealed") + " " + TextFormatting.WHITE + state.getValue(SEALED));
        } else {
            tooltip.add(TextFormatting.GRAY + I18n.format("info.survivalism.shift"));
        }

        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound compound = stack.getTagCompound();
            NBTTagCompound tag = stack.getSubCompound("BlockEntityTag");


            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (compound != null && tag.hasKey("inputTank")) {
                    FluidTank fluidTank = new FluidTank(TileBarrel.TANK_CAPACITY);
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
                    FluidTank fluidTank = new FluidTank(TileBarrel.TANK_CAPACITY);
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
            } else {
                tooltip.add(TextFormatting.GRAY + I18n.format("info.survivalism.shift"));
            }
        }
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BARREL_STATE, SEALED);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 1, 1.0625, 1);
    }
}
