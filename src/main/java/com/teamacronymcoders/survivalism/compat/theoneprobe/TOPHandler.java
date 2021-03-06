package com.teamacronymcoders.survivalism.compat.theoneprobe;

import com.teamacronymcoders.survivalism.common.blocks.BlockDryingRack;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelBrewing;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelSoaking;
import com.teamacronymcoders.survivalism.common.blocks.barrels.BlockBarrelStorage;
import com.teamacronymcoders.survivalism.common.blocks.vats.BlockCrushingVat;
import com.teamacronymcoders.survivalism.common.blocks.vats.BlockMixingVat;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.function.Function;

public class TOPHandler implements Function<ITheOneProbe, Void> {

    public static ITheOneProbe probe;

    @Override
    public Void apply(ITheOneProbe iTheOneProbe) {
        probe = iTheOneProbe;
        probe.registerProvider(new IProbeInfoProvider() {
            @Override
            public String getID() {
                return "survivalism:brewing_barrel";
            }

            @Override
            public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                if (blockState.getBlock() instanceof BlockBarrelBrewing) {
                    TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
                    provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
                }
            }
        });
        probe.registerProvider(new IProbeInfoProvider() {
            @Override
            public String getID() {
                return "survivalism:soaking_barrel";
            }

            @Override
            public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                if (blockState.getBlock() instanceof BlockBarrelSoaking) {
                    TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
                    provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
                }
            }
        });
        probe.registerProvider(new IProbeInfoProvider() {
            @Override
            public String getID() {
                return "survivalism:storage_barrel";
            }

            @Override
            public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                if (blockState.getBlock() instanceof BlockBarrelStorage) {
                    TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
                    provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
                }
            }
        });
        probe.registerProvider(new IProbeInfoProvider() {
            @Override
            public String getID() {
                return "survivalism:crushing_vat";
            }

            @Override
            public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                if (blockState.getBlock() instanceof BlockCrushingVat) {
                    TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
                    provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
                }
            }
        });
        probe.registerProvider(new IProbeInfoProvider() {
            @Override
            public String getID() {
                return "survivalism:mixing_vat";
            }

            @Override
            public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                if (blockState.getBlock() instanceof BlockMixingVat) {
                    TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
                    provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
                }
            }
        });
        probe.registerProvider(new IProbeInfoProvider() {
            @Override
            public String getID() {
                return "survivalism:drying_rack";
            }

            @Override
            public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                if (blockState.getBlock() instanceof BlockDryingRack) {
                    TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
                    provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
                }
            }
        });
        return null;
    }
}
