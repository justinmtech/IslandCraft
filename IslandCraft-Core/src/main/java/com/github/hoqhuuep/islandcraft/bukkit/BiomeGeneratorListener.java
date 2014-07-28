package com.github.hoqhuuep.islandcraft.bukkit;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldInitEvent;

import com.github.hoqhuuep.islandcraft.api.ICWorld;
import com.github.hoqhuuep.islandcraft.core.DefaultIslandCraft;
import com.github.hoqhuuep.islandcraft.core.DefaultWorld;
import com.github.hoqhuuep.islandcraft.database.Database;
import com.github.hoqhuuep.islandcraft.nms.BiomeGenerator;
import com.github.hoqhuuep.islandcraft.nms.NmsWrapper;

public class BiomeGeneratorListener implements Listener {
    private final Set<String> worldsDone;
    private final DefaultIslandCraft islandCraft;
    private final Database database;
    private final ConfigurationSection worldConfigs;
    private final NmsWrapper nms;

    public BiomeGeneratorListener(final DefaultIslandCraft islandCraft, final Database database, final ConfigurationSection config, final NmsWrapper nms) {
        this.islandCraft = islandCraft;
        this.database = database;
        this.nms = nms;
        this.worldConfigs = config.getConfigurationSection("worlds");
        worldsDone = new HashSet<String>();
    }

    @EventHandler
    public void onWorldInit(final WorldInitEvent event) {
        final World world = event.getWorld();
        final String worldName = world.getName();
        final ConfigurationSection config = worldConfigs.getConfigurationSection(worldName);
        if (config != null && !worldsDone.contains(worldName)) {
            final ICWorld icWorld = new DefaultWorld(worldName, world.getSeed(), database, config);
            final BiomeGenerator biomeGenerator = new IslandCraftBiomeGenerator(icWorld);
            nms.installBiomeGenerator(world, biomeGenerator);
            worldsDone.add(worldName);
            islandCraft.addWorld(icWorld);
        }
    }

    @EventHandler
    public void onChunkLoad(final ChunkLoadEvent event) {
        // First time server is run it will generate some chunks to find spawn
        // point this happens before WorldInitEvent. This event catches the
        // first one of those chunks, applies the hack, and regenerates the
        // chunk with the new WorldChunkManager.
        final World world = event.getWorld();
        final String worldName = world.getName();
        final ConfigurationSection config = worldConfigs.getConfigurationSection(worldName);
        if (config != null && !worldsDone.contains(worldName)) {
            final ICWorld icWorld = new DefaultWorld(worldName, world.getSeed(), database, config);
            final BiomeGenerator biomeGenerator = new IslandCraftBiomeGenerator(icWorld);
            if (nms.installBiomeGenerator(world, biomeGenerator)) {
                // If this is the very first time, regenerate the chunk
                if (!database.anyIslands(icWorld)) {
                    final Chunk chunk = event.getChunk();
                    world.regenerateChunk(chunk.getX(), chunk.getZ());
                }
            }
            worldsDone.add(worldName);
            islandCraft.addWorld(icWorld);
        }
    }
}