package com.teamacronymcoders.survivalism.common.blocks;

import com.teamacronymcoders.base.blocks.properties.PropertySideType;
import com.teamacronymcoders.survivalism.Survivalism;
import com.teamacronymcoders.survivalism.common.defaults.BlockDefault;
import com.teamacronymcoders.survivalism.common.tiles.TileBarrel;
import com.teamacronymcoders.survivalism.common.tiles.UpdatingItemStackHandler;
import com.teamacronymcoders.survivalism.utils.SurvivalismTab;
import com.teamacronymcoders.survivalism.utils.storages.StorageEnumsBarrelStates;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockBarrel extends BlockDefault {

    public static final PropertyEnum<StorageEnumsBarrelStates> BARREL_STATE = PropertyEnum.create("barrel_state", StorageEnumsBarrelStates.class);
    private static final StorageEnumsBarrelStates[] statesArray = StorageEnumsBarrelStates.values();
    public static final int GUI_ID = 1;
    public static final PropertyBool SEALED_STATE = PropertyBool.create("sealed");

    private NBTTagCompound compound = new NBTTagCompound();

    public BlockBarrel() {
        super(Material.WOOD);
        setCreativeTab(Survivalism.TAB);
        setUnlocalizedName(Survivalism.MODID + ".barrel");
        setRegistryName("barrel");
        setSoundType(SoundType.WOOD);
        setLightOpacity(255);
    }

    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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

    private TileBarrel getTE(IBlockAccess access, BlockPos pos) {
        TileEntity te = access.getTileEntity(pos);
        if (te instanceof TileBarrel) {
            return (TileBarrel) te;
        }
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        compound = new NBTTagCompound();
        TileEntity te = getTE(worldIn, pos);
        if (te != null) {
            compound.setInteger("barrel_state", state.getValue(BARREL_STATE).ordinal());
            compound.setBoolean("sealed", state.getValue(SEALED_STATE));
            compound.setTag("inputTank", ((TileBarrel) te).getInputTank().writeToNBT(new NBTTagCompound()));
            compound.setTag("outputTank", ((TileBarrel) te).getOutputTank().writeToNBT(new NBTTagCompound()));
            compound.setTag("items", ((TileBarrel) te).getItemHandler().serializeNBT());
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.clear();
        int meta = getMetaFromState(state);
        ItemStack stack = new ItemStack(this, 1, meta);
        stack.setTagCompound(compound);
        drops.add(stack);
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

        if (!barrel.checkBarrelState(StorageEnumsBarrelStates.STORAGE)) {
            if (!state.getValue(SEALED_STATE)) {
                if (playerIn.getHeldItem(hand).getItem() instanceof ItemBucket) {
                    ItemStack stack = playerIn.getHeldItem(hand);
                    FluidUtil.tryEmptyContainer(stack, FluidUtil.getFluidHandler(worldIn, pos, null), Integer.MAX_VALUE, playerIn, true);
                    playerIn.openContainer.detectAndSendChanges();
                    return true;
                }
            }
        }

        playerIn.openGui(Survivalism.INSTANCE, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
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
    @SuppressWarnings("deprecation")
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
        if (itemIn instanceof SurvivalismTab) {
            for (int i = 0; i < 3; i++) {
                ItemStack stack  = new ItemStack(this,  1, i);
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger("barrel_state", i);
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
                        NonNullList<ItemStack> nonNullList = NonNullList.withSize(9, ItemStack.EMPTY);
                        loadAllItems(compound, nonNullList);
                        int i = 0;
                        int j = 0;

                        for (ItemStack itemstack : nonNullList) {
                            if (!itemstack.isEmpty()) {
                                ++j;

                                if (i <= 4) {
                                    ++i;
                                    tooltip.add(String.format("%s x%d", itemstack.getDisplayName(), itemstack.getCount()));
                                }
                            }
                        }
                        if (j - i > 0) {
                            tooltip.add(String.format(TextFormatting.ITALIC + I18n.format("container.shulkerBox.more"), j - i));
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

    private static void loadAllItems(NBTTagCompound tag, NonNullList<ItemStack> list) {
        NBTTagList nbttaglist = tag.getTagList("items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("slot") & 255;

            if (j >= 0 && j < list.size()) {
                list.set(j, new ItemStack(nbttagcompound));
            }
        }
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{BARREL_STATE, SEALED_STATE}, new IUnlistedProperty[]{PropertySideType.SIDE_TYPE[0], PropertySideType.SIDE_TYPE[1],
                PropertySideType.SIDE_TYPE[2], PropertySideType.SIDE_TYPE[3],
                PropertySideType.SIDE_TYPE[4], PropertySideType.SIDE_TYPE[5]});
    }


}
