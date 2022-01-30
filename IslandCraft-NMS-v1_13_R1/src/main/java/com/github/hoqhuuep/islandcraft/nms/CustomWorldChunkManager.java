package com.github.hoqhuuep.islandcraft.nms;

import com.github.hoqhuuep.islandcraft.api.ICBiome;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_13_R2.block.CraftBlock;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomWorldChunkManager extends WorldChunkManager {

    private static final Map<ICBiome, BiomeBase> biomeMap = new EnumMap<>(ICBiome.class);

    static {
        biomeMap.put(ICBiome.BEACH, CraftBlock.biomeToBiomeBase(Biome.BEACH));
        biomeMap.put(ICBiome.BIRCH_FOREST, CraftBlock.biomeToBiomeBase(Biome.BIRCH_FOREST));
        biomeMap.put(ICBiome.BIRCH_FOREST_HILLS, CraftBlock.biomeToBiomeBase(Biome.BIRCH_FOREST_HILLS));
        biomeMap.put(ICBiome.BIRCH_FOREST_HILLS_M, CraftBlock.biomeToBiomeBase(Biome.TALL_BIRCH_HILLS));
        biomeMap.put(ICBiome.BIRCH_FOREST_M, CraftBlock.biomeToBiomeBase(Biome.TALL_BIRCH_FOREST));
        biomeMap.put(ICBiome.COLD_BEACH, CraftBlock.biomeToBiomeBase(Biome.SNOWY_BEACH));
        biomeMap.put(ICBiome.COLD_TAIGA, CraftBlock.biomeToBiomeBase(Biome.SNOWY_TAIGA));
        biomeMap.put(ICBiome.COLD_TAIGA_HILLS, CraftBlock.biomeToBiomeBase(Biome.SNOWY_TAIGA_HILLS));
        biomeMap.put(ICBiome.COLD_TAIGA_M, CraftBlock.biomeToBiomeBase(Biome.SNOWY_TAIGA_MOUNTAINS));
        biomeMap.put(ICBiome.DEEP_OCEAN, CraftBlock.biomeToBiomeBase(Biome.DEEP_OCEAN));
        biomeMap.put(ICBiome.DESERT, CraftBlock.biomeToBiomeBase(Biome.DESERT));
        biomeMap.put(ICBiome.DESERT_HILLS, CraftBlock.biomeToBiomeBase(Biome.DESERT_HILLS));
        biomeMap.put(ICBiome.DESERT_M, CraftBlock.biomeToBiomeBase(Biome.DESERT_LAKES));
        biomeMap.put(ICBiome.END, CraftBlock.biomeToBiomeBase(Biome.THE_END));
        biomeMap.put(ICBiome.EXTREME_HILLS, CraftBlock.biomeToBiomeBase(Biome.MOUNTAINS));
        biomeMap.put(ICBiome.EXTREME_HILLS_EDGE, CraftBlock.biomeToBiomeBase(Biome.MOUNTAIN_EDGE));
        biomeMap.put(ICBiome.EXTREME_HILLS_M, CraftBlock.biomeToBiomeBase(Biome.GRAVELLY_MOUNTAINS));
        biomeMap.put(ICBiome.EXTREME_HILLS_PLUS, CraftBlock.biomeToBiomeBase(Biome.WOODED_MOUNTAINS));
        biomeMap.put(ICBiome.EXTREME_HILLS_PLUS_M, CraftBlock.biomeToBiomeBase(Biome.MODIFIED_GRAVELLY_MOUNTAINS));
        biomeMap.put(ICBiome.FLOWER_FOREST, CraftBlock.biomeToBiomeBase(Biome.FLOWER_FOREST));
        biomeMap.put(ICBiome.FOREST, CraftBlock.biomeToBiomeBase(Biome.FOREST));
        biomeMap.put(ICBiome.FOREST_HILLS, CraftBlock.biomeToBiomeBase(Biome.WOODED_HILLS));
        biomeMap.put(ICBiome.FROZEN_OCEAN, CraftBlock.biomeToBiomeBase(Biome.FROZEN_OCEAN));
        biomeMap.put(ICBiome.FROZEN_RIVER, CraftBlock.biomeToBiomeBase(Biome.FROZEN_RIVER));
        biomeMap.put(ICBiome.ICE_MOUNTAINS, CraftBlock.biomeToBiomeBase(Biome.SNOWY_MOUNTAINS));
        biomeMap.put(ICBiome.ICE_PLAINS, CraftBlock.biomeToBiomeBase(Biome.SNOWY_TUNDRA));
        biomeMap.put(ICBiome.ICE_PLAINS_SPIKES, CraftBlock.biomeToBiomeBase(Biome.ICE_SPIKES));
        biomeMap.put(ICBiome.JUNGLE, CraftBlock.biomeToBiomeBase(Biome.JUNGLE));
        biomeMap.put(ICBiome.JUNGLE_EDGE, CraftBlock.biomeToBiomeBase(Biome.JUNGLE_EDGE));
        biomeMap.put(ICBiome.JUNGLE_HILLS, CraftBlock.biomeToBiomeBase(Biome.JUNGLE_HILLS));
        biomeMap.put(ICBiome.JUNGLE_M, CraftBlock.biomeToBiomeBase(Biome.MODIFIED_JUNGLE));
        biomeMap.put(ICBiome.JUNGLE_EDGE_M, CraftBlock.biomeToBiomeBase(Biome.MODIFIED_JUNGLE_EDGE));
        biomeMap.put(ICBiome.MEGA_SPRUCE_TAIGA, CraftBlock.biomeToBiomeBase(Biome.GIANT_SPRUCE_TAIGA));
        biomeMap.put(ICBiome.MEGA_SPRUCE_TAIGA_HILLS, CraftBlock.biomeToBiomeBase(Biome.GIANT_SPRUCE_TAIGA_HILLS));
        biomeMap.put(ICBiome.MEGA_TAIGA, CraftBlock.biomeToBiomeBase(Biome.GIANT_TREE_TAIGA));
        biomeMap.put(ICBiome.MEGA_TAIGA_HILLS, CraftBlock.biomeToBiomeBase(Biome.GIANT_TREE_TAIGA_HILLS));
        biomeMap.put(ICBiome.MESA, CraftBlock.biomeToBiomeBase(Biome.BADLANDS));
        biomeMap.put(ICBiome.MESA_BRYCE, CraftBlock.biomeToBiomeBase(Biome.ERODED_BADLANDS));
        biomeMap.put(ICBiome.MESA_PLATEAU, CraftBlock.biomeToBiomeBase(Biome.BADLANDS_PLATEAU));
        biomeMap.put(ICBiome.MESA_PLATEAU_F, CraftBlock.biomeToBiomeBase(Biome.WOODED_BADLANDS_PLATEAU));
        biomeMap.put(ICBiome.MESA_PLATEAU_F_M, CraftBlock.biomeToBiomeBase(Biome.MODIFIED_WOODED_BADLANDS_PLATEAU));
        biomeMap.put(ICBiome.MESA_PLATEAU_M, CraftBlock.biomeToBiomeBase(Biome.MODIFIED_BADLANDS_PLATEAU));
        biomeMap.put(ICBiome.MUSHROOM_ISLAND, CraftBlock.biomeToBiomeBase(Biome.MUSHROOM_FIELDS));
        biomeMap.put(ICBiome.MUSHROOM_ISLAND_SHORE, CraftBlock.biomeToBiomeBase(Biome.MUSHROOM_FIELD_SHORE));
        biomeMap.put(ICBiome.NETHER, CraftBlock.biomeToBiomeBase(Biome.NETHER));
        biomeMap.put(ICBiome.OCEAN, CraftBlock.biomeToBiomeBase(Biome.OCEAN));
        biomeMap.put(ICBiome.PLAINS, CraftBlock.biomeToBiomeBase(Biome.PLAINS));
        biomeMap.put(ICBiome.RIVER, CraftBlock.biomeToBiomeBase(Biome.RIVER));
        biomeMap.put(ICBiome.ROOFED_FOREST, CraftBlock.biomeToBiomeBase(Biome.DARK_FOREST));
        biomeMap.put(ICBiome.ROOFED_FOREST_M, CraftBlock.biomeToBiomeBase(Biome.DARK_FOREST_HILLS));
        biomeMap.put(ICBiome.SAVANNA, CraftBlock.biomeToBiomeBase(Biome.SAVANNA));
        biomeMap.put(ICBiome.SAVANNA_M, CraftBlock.biomeToBiomeBase(Biome.SHATTERED_SAVANNA));
        biomeMap.put(ICBiome.SAVANNA_PLATEAU, CraftBlock.biomeToBiomeBase(Biome.SAVANNA_PLATEAU));
        biomeMap.put(ICBiome.SAVANNA_PLATEAU_M, CraftBlock.biomeToBiomeBase(Biome.SHATTERED_SAVANNA_PLATEAU));
        biomeMap.put(ICBiome.STONE_BEACH, CraftBlock.biomeToBiomeBase(Biome.STONE_SHORE));
        biomeMap.put(ICBiome.SUNFLOWER_PLAINS, CraftBlock.biomeToBiomeBase(Biome.SUNFLOWER_PLAINS));
        biomeMap.put(ICBiome.SWAMPLAND, CraftBlock.biomeToBiomeBase(Biome.SWAMP));
        biomeMap.put(ICBiome.SWAMPLAND_M, CraftBlock.biomeToBiomeBase(Biome.SWAMP_HILLS));
        biomeMap.put(ICBiome.TAIGA, CraftBlock.biomeToBiomeBase(Biome.TAIGA));
        biomeMap.put(ICBiome.TAIGA_HILLS, CraftBlock.biomeToBiomeBase(Biome.TAIGA_HILLS));
        biomeMap.put(ICBiome.TAIGA_M, CraftBlock.biomeToBiomeBase(Biome.TAIGA_MOUNTAINS));
        biomeMap.put(ICBiome.WARM_OCEAN, CraftBlock.biomeToBiomeBase(Biome.WARM_OCEAN));
        biomeMap.put(ICBiome.LUKEWARM_OCEAN, CraftBlock.biomeToBiomeBase(Biome.LUKEWARM_OCEAN));
        biomeMap.put(ICBiome.DEEP_LUKEWARM_OCEAN, CraftBlock.biomeToBiomeBase(Biome.DEEP_LUKEWARM_OCEAN));
        biomeMap.put(ICBiome.COLD_OCEAN, CraftBlock.biomeToBiomeBase(Biome.COLD_OCEAN));
        biomeMap.put(ICBiome.DEEP_COLD_OCEAN, CraftBlock.biomeToBiomeBase(Biome.DEEP_COLD_OCEAN));
        biomeMap.put(ICBiome.DEEP_FROZEN_OCEAN, CraftBlock.biomeToBiomeBase(Biome.DEEP_FROZEN_OCEAN));
    }

    private final BiomeCache biomeCache;
    private final BiomeGenerator biomeGenerator;

    public CustomWorldChunkManager(final BiomeGenerator biomeGenerator) {
        this.biomeGenerator = biomeGenerator;
        this.biomeCache = new BiomeCache(this);
    }

    /**
     * Returns the biome at a position. Used for various things.
     */
    @Override
    public BiomeBase getBiome(BlockPosition blockPosition, BiomeBase biomeBase) {
        return this.biomeCache.a(blockPosition.getX(), blockPosition.getZ(), biomeBase);
    }

    @Override
    public BiomeBase[] getBiomes(int xMin, int zMin, int xSize, int zSize) {
        BiomeBase[] result = new BiomeBase[xSize * zSize];
        for (int i = 0; i < xSize * zSize; ++i) {
            final int x = (xMin + (i % xSize)) << 2;
            final int z = (zMin + (i / xSize)) << 2;
            final ICBiome biome = biomeGenerator.generateBiome(x, z);
            result[i] = biomeMap.get(biome);
        }
        return result;
    }

    @Override
    public Set<BiomeBase> a(int x, int z, int radius) {

        final int xMin = (x - radius) >> 2;
        final int zMin = (z - radius) >> 2;
        final int xMax = (x + radius) >> 2;
        final int zMax = (z + radius) >> 2;
        final int xSize = xMax - xMin + 1;
        final int zSize = zMax - zMin + 1;
        final BiomeBase[] biomes = getBiomes(xMin, zMin, xSize, zSize);
        return Stream.of(biomes).collect(Collectors.toSet());
    }

    @Override
    public boolean a(StructureGenerator<?> structureGenerator) {
        return true;
    }

    @Override
    public Set<IBlockData> b() {
        return b;
    }

    @Override
    public BiomeBase[] getBiomeBlock(final int x, final int z, final int xSize, final int zSize) {
        return a(x, z, xSize, zSize, true);
    }

    @Override
    public BiomeBase[] a(final int xMin, final int zMin, final int xSize, final int zSize, final boolean useCache) {
        BiomeBase[] result = new BiomeBase[xSize * zSize];
        if (xSize == 16 && zSize == 16 && (xMin & 0xF) == 0 && (zMin & 0xF) == 0) {
            if (useCache) {
                final BiomeBase[] biomes = this.biomeCache.b(xMin, zMin);
                System.arraycopy(biomes, 0, result, 0, xSize * zSize);
                return result;
            }
            final ICBiome[] temp = biomeGenerator.generateChunkBiomes(xMin, zMin);
            for (int i = 0; i < xSize * zSize; ++i) {
                result[i] = biomeMap.get(temp[i]);
            }
            return result;
        }
        for (int x = 0; x < xSize; ++x) {
            for (int z = 0; z < zSize; ++z) {
                final ICBiome temp = biomeGenerator.generateBiome(xMin + x, zMin + z);
                result[x + z * xSize] = biomeMap.get(temp);
            }
        }
        return result;
    }

    @Override
    public BlockPosition a(final int x, final int z, final int radius, final List allowedBiomes, final Random random) {
        final int xMin = (x - radius) >> 2;
        final int zMin = (z - radius) >> 2;
        final int xMax = (x + radius) >> 2;
        final int zMax = (z + radius) >> 2;
        final int xSize = xMax - xMin + 1;
        final int zSize = zMax - zMin + 1;
        final BiomeBase[] biomes = getBiomes(xMin, zMin, xSize, zSize);
        BlockPosition result = null;
        int count = 0;
        for (int i = 0; i < xSize * zSize; i++) {
            final int xPosition = (xMin + (i % xSize)) << 2;
            final int zPosition = (zMin + (i / xSize)) << 2;
            if (allowedBiomes.contains(biomes[i]) && (result == null || random.nextInt(count + 1) == 0)) {
                result = new BlockPosition(xPosition, 0, zPosition);
                count++;
            }
        }
        return result;
    }

    public void X_() {
        biomeCache.a();
        biomeGenerator.cleanupCache();
    }
}
