package com.teamacronymcoders.survivalism.common.blocks;

import com.teamacronymcoders.base.blocks.BlockBase;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.utils.SurvivalismTab;
import com.teamacronymcoders.survivalism.utils.storages.StorageEnumsBarrelStates;
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
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockBarrel extends BlockBase {

    public static final PropertyEnum<StorageEnumsBarrelStates> BARREL_STATE = PropertyEnum.create("barrel_state", StorageEnumsBarrelStates.class);
    public static final int GUI_ID = 1;
    public static final PropertyBool SEALED_STATE = PropertyBool.create("sealed");
    private static final StorageEnumsBarrelStates[] statesArray = StorageEnumsBarrelStates.values();
    private NBTTagCompound compound = new NBTTagCompound();

    public BlockBarrel() {
        super(Material.WOOD);
        setCreativeTab(Survivalism.TAB);
        setTranslationKey(Survivalism.MODID + ".barrel");
        setRegistryName("barrel");
        setSoundType(SoundType.WOOD);
        setLightOpacity(0);
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

    private TileBarrel getTE(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBarrel) {
            return (TileBarrel) te;
        }
        return null;
    }

    public static TileBarrel getTE(IBlockAccess access, BlockPos pos) {
        TileEntity te = access.getTileEntity(pos);
        if (te instanceof TileBarrel) {
            return (TileBarrel) te;
        }
        return null;
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return state.getValue(SEALED_STATE);
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
    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
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
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileBarrel te = getTE(worldIn, pos);
        if (te.getItemHandler() != null) {
            if (state.getValue(SEALED_STATE)) {
                compound = new NBTTagCompound();
                compound.setInteger("barrel_state", state.getValue(BARREL_STATE).ordinal());
                compound.setBoolean("sealed", state.getValue(SEALED_STATE));
                compound.setTag("inputTank", (te.getInputTank().writeToNBT(new NBTTagCompound())));
                compound.setTag("outputTank", (te.getOutputTank().writeToNBT(new NBTTagCompound())));
                compound.setTag("items", (te.getItemHandler().serializeNBT()));
            } else {
                for (int i = 0; i < te.getItemHandler().getSlots(); i++) {
                    ItemStack stack = te.getItemHandler().getStackInSlot(i);
                    InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            }
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (!state.getValue(SEALED_STATE)) {
            int meta = getMetaFromState(state);
            ItemStack stack = new ItemStack(this, 1, meta);
            drops.add(stack);
        } else {
            drops.clear();
            int meta = getMetaFromState(state);
            ItemStack stack = new ItemStack(this, 1, meta);
            stack.setTagCompound(compound);
            drops.add(stack);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileBarrel barrel = getTE(worldIn, pos);

        if (barrel == null) {
            return false;
        }

        if (worldIn.isRemote) {
            return true;
        }

        if (!state.getValue(SEALED_STATE)) {
            if (playerIn.getHeldItem(hand).getItem().equals(Item.getItemFromBlock(Blocks.SPONGE))) {
                FluidUtil.getFluidHandler(worldIn, pos, EnumFacing.NORTH).drain(Integer.MAX_VALUE, true);
                FluidUtil.getFluidHandler(worldIn, pos, EnumFacing.DOWN).drain(Integer.MAX_VALUE, true);
                barrel.sendUpdatePacketClient();
            } else if (playerIn.getHeldItem(hand).getItem() instanceof ItemBucket) {
                ItemStack stack = playerIn.getHeldItem(hand);
                FluidStack fs = FluidUtil.getFluidContained(stack);
                if (fs != null) {
                    if (fs.amount == 0) {
                        FluidUtil.tryFillContainer(stack, FluidUtil.getFluidHandler(worldIn, pos, EnumFacing.DOWN), Integer.MAX_VALUE, playerIn, true);
                        barrel.sendUpdatePacketClient();
                    }
                    if (fs.amount == 1000) {
                        FluidUtil.tryEmptyContainer(stack, FluidUtil.getFluidHandler(worldIn, pos, null), Integer.MAX_VALUE, playerIn, true);
                        barrel.sendUpdatePacketClient();
                    }
                }

                playerIn.openContainer.detectAndSendChanges();
                return true;
            }
        }

        playerIn.openGui(Survivalism.INSTANCE, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return side != EnumFacing.UP && side != EnumFacing.DOWN;
    }

    @Override
    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.isRemote) {
            return;
        }
        if (worldIn.isBlockPowered(pos)) {
            TileBarrel tb = getTE(worldIn, pos);
            if (tb != null) {
                tb.handleRedstone(worldIn.isBlockPowered(pos));
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound compound = stack.getTagCompound();
            TileBarrel te = getTE(worldIn, pos);
            if (compound != null && te != null && !stack.isEmpty()) {
                te.setInputTank(te.getInputTank().readFromNBT(stack.getTagCompound().getCompoundTag("inputTank")));
                te.setOutputTank(te.getOutputTank().readFromNBT(stack.getTagCompound().getCompoundTag("outputTank")));
                ItemStackHandler stackHandler = te.getItemHandler();
                stackHandler.deserializeNBT(compound.getCompoundTag("items"));
            }
        }
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getStateFromMeta(placer.getHeldItem(hand).getMetadata());
    }

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState();
        if (meta >= 3) {
            state = state.withProperty(SEALED_STATE, true);
            meta = meta - 3;
        } else {
            state = state.withProperty(SEALED_STATE, false);
        }
        state = state.withProperty(BARREL_STATE, statesArray[meta]);
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(BARREL_STATE).ordinal();
        if (state.getValue(SEALED_STATE)) {
            meta += 3;
        }
        return meta;
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        if (itemIn instanceof SurvivalismTab || itemIn == CreativeTabs.SEARCH) {
            for (int i = 0; i < 6; i++) {
                ItemStack stack = new ItemStack(this, 1, i);
                NBTTagCompound nbt = new NBTTagCompound();
                if (i <= 2) {
                    nbt.setInteger("barrel_state", i);
                    nbt.setBoolean("sealed", false);
                } else {
                    nbt.setInteger("barrel_state", i - 3);
                    nbt.setBoolean("sealed", true);
                }
                stack.setTagCompound(nbt);
                items.add(stack);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound compound = stack.getTagCompound();

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (compound.hasKey("barrel_state")) {
                    if (compound.getInteger("barrel_state") == 0) {
                        tooltip.add(TextFormatting.GRAY + I18n.format("state.barrel") + " " + TextFormatting.WHITE + I18n.format("state.barrel.storage"));
                    } else if (compound.getInteger("barrel_state") == 1) {
                        tooltip.add(TextFormatting.GRAY + I18n.format("state.barrel") + " " + TextFormatting.WHITE + I18n.format("state.barrel.brewing"));
                    } else if (compound.getInteger("barrel_state") == 2) {
                        tooltip.add(TextFormatting.GRAY + I18n.format("state.barrel") + " " + TextFormatting.WHITE + I18n.format("state.barrel.soaking"));
                    }
                }

                if (compound.hasKey("sealed")) {
                    tooltip.add(TextFormatting.GRAY + I18n.format("state.sealed") + " " + TextFormatting.WHITE + compound.getBoolean("sealed"));
                }

                if (compound.hasKey("inputTank")) {
                    FluidTank fluidTank = new FluidTank(TileBarrel.TANK_CAPACITY);
                    FluidStack fluidStack = fluidTank.readFromNBT(compound.getCompoundTag("inputTank")).getFluid();
                    if (fluidStack != null) {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            tooltip.add(TextFormatting.GRAY + "Input: " + TextFormatting.WHITE + fluidStack.getLocalizedName() + ": " + fluidStack.amount);
                        } else if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            tooltip.add(TextFormatting.GRAY + "Press Shift + L-Ctrl for Fluid Information");
                        }
                    }
                }

                if (compound.hasKey("outputTank")) {
                    FluidTank fluidTank = new FluidTank(TileBarrel.TANK_CAPACITY);
                    FluidStack fluidStack = fluidTank.readFromNBT(compound.getCompoundTag("outputTank")).getFluid();
                    if (fluidStack != null) {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            tooltip.add(TextFormatting.GRAY + "Output: " + TextFormatting.WHITE + fluidStack.getLocalizedName() + ": " + fluidStack.amount);
                        } else if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            tooltip.add(TextFormatting.GRAY + "Press Shift + L-Ctrl for Fluid Information");
                        }
                    }
                }

                if (compound.hasKey("items")) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                        int c = 0;
                        int d = 0;

                        NonNullList<ItemStack> nonNullList = NonNullList.withSize(9, ItemStack.EMPTY);
                        ItemStackHelper.loadAllItems(compound.getCompoundTag("items"), nonNullList);

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
            } else if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(TextFormatting.GRAY + "Press Shift for Barrel Information");
            }
        }
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BARREL_STATE, SEALED_STATE);
    }


}
