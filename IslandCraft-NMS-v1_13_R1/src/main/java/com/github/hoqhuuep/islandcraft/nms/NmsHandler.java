package com.github.hoqhuuep.islandcraft.nms;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import java.lang.reflect.Field;

public class NmsHandler extends NmsWrapper {

    @Override
    public boolean installBiomeGenerator(final World world, final BiomeGenerator biomeGenerator) {
        if (!(world instanceof CraftWorld)) {
            return false;
        }
        final CraftWorld craftWorld = (CraftWorld) world;
        try {
            ChunkGenerator<?> nmsChunkGenerator = craftWorld.getHandle().getChunkProvider().getChunkGenerator();
            Field nmsChunkProviderGenerateField = nmsChunkGenerator.getClass().getDeclaredField("generator");
            nmsChunkProviderGenerateField.setAccessible(true);
            ChunkProviderGenerate nmsChunkProviderGenerate = (ChunkProviderGenerate) nmsChunkProviderGenerateField.get(nmsChunkGenerator);
            Field nmsWorldChunkManagerField = ChunkGeneratorAbstract.class.getDeclaredField("c");
            nmsWorldChunkManagerField.setAccessible(true);
            nmsWorldChunkManagerField.set(nmsChunkProviderGenerate, new CustomWorldChunkManager(biomeGenerator));
            return true;

        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }
    }
}

