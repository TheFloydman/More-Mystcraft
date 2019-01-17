package thefloydman.moremystcraft.world;

import net.minecraft.world.gen.*;

import com.xcompwiz.mystcraft.world.AgeController;
import com.xcompwiz.mystcraft.world.agedata.*;
import net.minecraft.world.biome.*;
import com.xcompwiz.mystcraft.world.gen.structure.*;
import net.minecraft.init.*;
import net.minecraft.block.state.pattern.*;
import com.google.common.base.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.world.chunk.*;
import com.xcompwiz.mystcraft.world.chunk.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraftforge.event.*;
import net.minecraft.world.*;
import net.minecraftforge.event.terraingen.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.entity.*;
import java.util.*;
import javax.annotation.*;

public class ChunkProviderMoreMystcraft implements IChunkGenerator
{
    private AgeController controller;
    private Random rand;
    private NoiseGeneratorPerlin stoneNoiseGen;
    private World worldObj;
    private AgeData agedata;
    private double[] stoneNoise;
    private Biome[] tempBiomesArray;
    private MapGenScatteredFeatureMyst scatteredFeatureGenerator;
    private WorldGenMinable worldgenminablequartz;
    
    public ChunkProviderMoreMystcraft(final AgeController ageController, final World world, final AgeData age) {
        this.scatteredFeatureGenerator = new MapGenScatteredFeatureMyst();
        this.worldgenminablequartz = new WorldGenMinable(Blocks.QUARTZ_ORE.getDefaultState(), 13, (Predicate)BlockMatcher.forBlock(Blocks.NETHERRACK));
        this.controller = ageController;
        this.stoneNoise = new double[256];
        this.worldObj = world;
        this.agedata = age;
        this.rand = new Random(this.agedata.getSeed());
        this.stoneNoiseGen = new NoiseGeneratorPerlin(this.rand, 4);
    }
    
    private void replaceBlocksForBiome(final int chunkX, final int chunkZ, final ChunkPrimer primer, final Biome[] aBiome) {
        final ChunkGeneratorEvent.ReplaceBiomeBlocks event = new ChunkGeneratorEvent.ReplaceBiomeBlocks((IChunkGenerator)this, chunkX, chunkZ, primer, this.worldObj);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.getResult() != Event.Result.DENY) {
            final double noisefactor = 0.03125;
            this.stoneNoise = this.stoneNoiseGen.getRegion(this.stoneNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 16, 16, noisefactor * 2.0, noisefactor * 2.0, 1.0);
            for (int xoff = 0; xoff < 16; ++xoff) {
                for (int zoff = 0; zoff < 16; ++zoff) {
                    final Biome b = aBiome[zoff + xoff * 16];
                    b.generateBiomeTerrain(this.worldObj, this.rand, primer, chunkX * 16 + xoff, chunkZ * 16 + zoff, this.stoneNoise[zoff + xoff * 16]);
                }
            }
        }
    }
    
    @Nonnull
    public Chunk generateChunk(final int chunkX, final int chunkZ) {
        this.rand.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
        final ChunkPrimerMyst primer = new ChunkPrimerMyst();
        this.controller.generateTerrain(chunkX, chunkZ, primer);
        this.tempBiomesArray = this.worldObj.getBiomeProvider().getBiomes(this.tempBiomesArray, chunkX * 16, chunkZ * 16, 16, 16);
        primer.inBiomeDecoration = true;
        this.replaceBlocksForBiome(chunkX, chunkZ, primer, this.tempBiomesArray);
        primer.inBiomeDecoration = false;
        this.controller.modifyTerrain(chunkX, chunkZ, primer);
        this.scatteredFeatureGenerator.generate(this.worldObj, chunkX, chunkZ, (ChunkPrimer)primer);
        final Chunk chunk = new Chunk(this.worldObj, (ChunkPrimer)primer, chunkX, chunkZ);
        chunk.generateSkylightMap();
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < 128; ++y) {
                    chunk.setLightFor(EnumSkyBlock.BLOCK, new BlockPos(x, y, z), 0);
                }
            }
        }
        final byte[] chunkBiomeArray = chunk.getBiomeArray();
        for (int i = 0; i < chunkBiomeArray.length; ++i) {
            chunkBiomeArray[i] = (byte)Biome.getIdForBiome(this.tempBiomesArray[i]);
        }
        this.controller.finalizeChunk(chunk, chunkX, chunkZ);
        return chunk;
    }
    
    public void populate(final int chunkX, final int chunkZ) {
        final Chunk chunk = this.worldObj.getChunkFromChunkCoords(chunkX, chunkZ);
        chunk.setTerrainPopulated(false);
        BlockFalling.fallInstantly = true;
        final int x = chunkX * 16;
        final int z = chunkZ * 16;
        BlockPos blockpos = new BlockPos(x, 0, z);
        final Biome biome = this.worldObj.getBiome(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.worldObj.getSeed());
        final long k = this.rand.nextLong() / 2L * 2L + 1L;
        final long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(chunkX * k + chunkZ * l ^ this.worldObj.getSeed());
        final boolean flag = false;
        final ChunkPos chunkpos = new ChunkPos(chunkX, chunkZ);
        ForgeEventFactory.onChunkPopulate(true, (IChunkGenerator)this, this.worldObj, this.rand, chunkX, chunkZ, flag);
        this.scatteredFeatureGenerator.generateStructure(this.worldObj, this.rand, chunkpos);
        try {
            biome.decorate(this.worldObj, this.rand, new BlockPos(x, 0, z));
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("Biome [%s] threw an error while populating chunk.", biome.getRegistryName()), e);
        }
        if (TerrainGen.populate((IChunkGenerator)this, this.worldObj, this.rand, chunkX, chunkZ, flag, PopulateChunkEvent.Populate.EventType.ANIMALS)) {
            WorldEntitySpawner.performWorldGenSpawning(this.worldObj, biome, x + 8, z + 8, 16, 16, this.rand);
        }
        this.controller.populate(this.worldObj, this.rand, x, z);
        blockpos = blockpos.add(8, 0, 8);
        final boolean doGen = TerrainGen.generateOre(this.worldObj, this.rand, (WorldGenerator)this.worldgenminablequartz, new BlockPos(x, 0, z), OreGenEvent.GenerateMinable.EventType.QUARTZ);
        for (int k2 = 0; doGen && k2 < 16; ++k2) {
            final int gx = x + this.rand.nextInt(16);
            final int gy = this.rand.nextInt(108) + 10;
            final int gz = z + this.rand.nextInt(16);
            this.worldgenminablequartz.generate(this.worldObj, this.rand, new BlockPos(gx, gy, gz));
        }
        if (TerrainGen.populate((IChunkGenerator)this, this.worldObj, this.rand, chunkX, chunkZ, flag, PopulateChunkEvent.Populate.EventType.ICE)) {
            for (int k3 = 0; k3 < 16; ++k3) {
                for (int j3 = 0; j3 < 16; ++j3) {
                    final BlockPos blockpos2 = this.worldObj.getPrecipitationHeight(blockpos.add(k3, 0, j3));
                    final BlockPos blockpos3 = blockpos2.down();
                    if (this.worldObj.canBlockFreezeWater(blockpos3)) {
                        this.worldObj.setBlockState(blockpos3, Blocks.ICE.getDefaultState(), 2);
                    }
                    if (this.worldObj.canSnowAt(blockpos2, true)) {
                        this.worldObj.setBlockState(blockpos2, Blocks.SNOW_LAYER.getDefaultState(), 2);
                    }
                }
            }
        }
        ForgeEventFactory.onChunkPopulate(false, (IChunkGenerator)this, this.worldObj, this.rand, chunkX, chunkZ, flag);
        BlockFalling.fallInstantly = false;
        chunk.setTerrainPopulated(true);
    }
    
    public List<Biome.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        List<Biome.SpawnListEntry> list = null;
        final Biome b = this.worldObj.getBiome(pos);
        list = (List<Biome.SpawnListEntry>)b.getSpawnableList(creatureType);
        return this.controller.affectCreatureList(creatureType, list, pos);
    }
    
    @Nullable
    public BlockPos getNearestStructurePos(final World worldIn, final String structureName, final BlockPos position, final boolean findUnexplored) {
        return this.controller.locateTerrainFeature(worldIn, structureName, position, findUnexplored);
    }
    
    public boolean isInsideStructure(final World worldIn, final String structureName, final BlockPos pos) {
        return this.controller.isInsideFeature(worldIn, structureName, pos);
    }
    
    public boolean generateStructures(final Chunk chunkIn, final int x, final int z) {
        return false;
    }
    
    public void recreateStructures(final Chunk chunkIn, final int x, final int z) {
        this.scatteredFeatureGenerator.generate(this.worldObj, x, z, (ChunkPrimer)null);
    }
}