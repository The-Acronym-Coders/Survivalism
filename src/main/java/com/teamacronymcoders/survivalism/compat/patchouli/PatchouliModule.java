package com.teamacronymcoders.survivalism.compat.patchouli;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.survivalism.Survivalism;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import vazkii.patchouli.api.PatchouliAPI;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

@Module(Survivalism.MODID)
public class PatchouliModule extends ModuleBase {

    @Override
    public String getName() {
        return "Patchouli";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        ResourceLocation brewing = new ResourceLocation(Survivalism.MODID, "patchouli_books/survivalism/en_us/templates/brewing.json");
        ResourceLocation crushing = new ResourceLocation(Survivalism.MODID, "patchouli_books/survivalism/en_us/templates/brewing.json");
        ResourceLocation soaking = new ResourceLocation(Survivalism.MODID, "patchouli_books/survivalism/en_us/templates/brewing.json");

        Supplier<InputStream> brewSupplier = () -> {
            try {
                return Minecraft.getMinecraft().getResourceManager().getResource(brewing).getInputStream();
            } catch (IOException e) {
                this.getMod().getLogger().error("Failed to get File: " + brewing + "Exception: " + e);
            }
            return null;
        };

        Supplier<InputStream> crushSupplier = () -> {
            try {
                return Minecraft.getMinecraft().getResourceManager().getResource(crushing).getInputStream();
            } catch (IOException e) {
                this.getMod().getLogger().error("Failed to get File: " + crushing + "Exception: " + e);
            }
            return null;
        };

        Supplier<InputStream> soakSupplier = () -> {
            try {
                return Minecraft.getMinecraft().getResourceManager().getResource(soaking).getInputStream();
            } catch (IOException e) {
                this.getMod().getLogger().error("Failed to get File: " + soaking + "Exception: " + e);
            }
            return null;
        };

        PatchouliAPI.instance.registerTemplateAsBuiltin(new ResourceLocation(Survivalism.MODID, "brewing"), brewSupplier);
        PatchouliAPI.instance.registerTemplateAsBuiltin(new ResourceLocation(Survivalism.MODID, "crushing"), crushSupplier);
        PatchouliAPI.instance.registerTemplateAsBuiltin(new ResourceLocation(Survivalism.MODID, "soaking"), soakSupplier);
    }
}
