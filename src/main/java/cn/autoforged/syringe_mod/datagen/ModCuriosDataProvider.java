package cn.autoforged.syringe_mod.datagen;

import cn.autoforged.syringe_mod.SyringeMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class ModCuriosDataProvider extends CuriosDataProvider {
    public ModCuriosDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelper) {
        super(SyringeMod.MODID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        createSlot("belt")
                .size(1)
                .order(1);

        createEntities("belt")
                .addPlayer()
                .addSlots("belt");
    }
}
