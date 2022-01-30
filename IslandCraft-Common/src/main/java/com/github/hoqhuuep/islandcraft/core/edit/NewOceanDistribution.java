package com.github.hoqhuuep.islandcraft.core.edit;

import com.github.hoqhuuep.islandcraft.api.BiomeDistribution;
import com.github.hoqhuuep.islandcraft.api.ICBiome;
import com.github.hoqhuuep.islandcraft.bukkit.IslandCraftPlugin;
import com.github.hoqhuuep.islandcraft.core.ICLogger;
import com.github.hoqhuuep.islandcraft.core.noise.OctaveNoise;
import com.github.hoqhuuep.islandcraft.util.StringUtils;
import com.sun.tools.javac.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NewOceanDistribution implements BiomeDistribution {

    private final Map<ICBiome, Set<ICBiome>> disallowed = new HashMap<>();
    private final Map<Pair<Integer, Integer>, ICBiome> changed = new HashMap<>();

    private final ICBiome[] biomes;
    private final double biomeSize;

    public NewOceanDistribution(final String[] args) {
        ICLogger.logger.info("Creating NewOceanDistribution with args: " + StringUtils.join(args, " "));

        this.biomeSize = Double.parseDouble(args[0]);
        this.biomes = new ICBiome[args.length - 1];

        for (int i = 1; i < args.length; i++) {
            this.biomes[i - 1] = ICBiome.valueOf(args[i]);
        }

        IslandCraftPlugin plugin = (IslandCraftPlugin) Bukkit.getPluginManager().getPlugin("IslandCraft");
        ConfigurationSection disallowedNear = plugin.getConfig().getConfigurationSection("disallowedNear");

        for (String biomeName: disallowedNear.getKeys(false)) {
            Set<ICBiome> biomes = new HashSet<>();
            for (String b: disallowedNear.getStringList(biomeName)) {
                biomes.add(ICBiome.valueOf(b.toUpperCase()));
            }

            this.disallowed.put(ICBiome.valueOf(biomeName.toUpperCase()), biomes);
        }
    }

    @Override
    public ICBiome biomeAt(int x, int z, long worldSeed) {
        OctaveNoise noise = new OctaveNoise(worldSeed);
        ICBiome baseBiome = this.getBiome(x, z, noise);
        Set<ICBiome> blockedAround = new HashSet<>();

        aroundLoop:
        for (int aroundX = -1; aroundX <= 1; aroundX++) {
            for (int aroundZ = -1; aroundZ <= 1; aroundZ++) {
                ICBiome aroundBaseBiome = this.getBiome(x + aroundX, z + aroundZ, noise);

                if (disallowed.containsKey(aroundBaseBiome)) {
                    blockedAround.addAll(disallowed.get(aroundBaseBiome));
                }

                if (changed.containsKey(Pair.of(x + aroundX, z + aroundZ)) && aroundBaseBiome.equals(baseBiome)) {
                    ICBiome changedBiome = changed.get(Pair.of(x + aroundX, z + aroundZ));
                    changed.put(Pair.of(x, z), changedBiome);
                    baseBiome = changedBiome;
                    break aroundLoop;
                }
            }
        }

        if (!changed.containsKey(Pair.of(x, z)) && blockedAround.contains(baseBiome)) {
            ICBiome targetBiome = null;

            if (blockedAround.size() == biomes.length) {
                targetBiome = biomes[Math.round(biomes.length / 2.0F)];
            } else {
                for (ICBiome possibleBiome: biomes) {
                    if (!blockedAround.contains(possibleBiome)) {
                        targetBiome = possibleBiome;
                        break;
                    }
                }
            }

            changed.put(Pair.of(x, z), targetBiome);
            baseBiome = targetBiome;
        }

        return baseBiome;
    }

    private ICBiome getBiome(int x, int z, OctaveNoise noise) {
        int index = (int) (noise.noise(x / this.biomeSize, z / this.biomeSize) / (1.0D / this.biomes.length));
        return this.biomes[Math.min(this.biomes.length - 1, index)];
    }
}
