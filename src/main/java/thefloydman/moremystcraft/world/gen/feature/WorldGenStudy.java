package thefloydman.moremystcraft.world.gen.feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import com.xcompwiz.mystcraft.Mystcraft;
import com.xcompwiz.mystcraft.api.hook.DimensionAPI;
import com.xcompwiz.mystcraft.world.agedata.AgeData;
import com.xcompwiz.mystcraft.world.agedata.AgeData.AgeDataData;
import com.xcompwiz.mystcraft.api.impl.linking.DimensionAPIWrapper;

import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.proxy.CommonProxy;
import thefloydman.moremystcraft.util.Reference;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;

public class WorldGenStudy extends WorldGenerator implements IWorldGenerator {

	static HashMap<Biome, HashMap<String, IBlockState>> biomeMap = new HashMap<>();

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;

		switch (world.provider.getDimension()) {
		case -1:
			generateNether(world, rand, blockX + 8, blockZ + 8);
			break;
		case 0:
			if (new MoreMystcraftConfig().getStudiesEnabled() == true) {
				generateOverworld(world, rand, blockX + 8, blockZ + 8);
			}
			break;
		case 1:
			generateEnd(world, rand, blockX + 8, blockZ + 8);
			break;
		default:
			break;
		}

		// If the dimension is a Mystcraft Dimension and has an Abandoned Studies Page,
		// generate in that dimension.
		int dimId = world.provider.getDimension();
		try {
			if (dimId < 0) {
				return;
			} else if (world.provider.getDimensionType().equals(Mystcraft.dimensionType)) {
				AgeData data = new AgeData("currentDim").getAge(dimId, false);
				List symbolList = data.getSymbols(false);
				ResourceLocation studyLoc = Reference.forMoreMystcraft("abandoned_study");
				if (symbolList.contains(studyLoc))
					generateOverworld(world, rand, blockX + 8, blockZ + 8);
			}
		} catch (NullPointerException e) {
			return;
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid Dimension ID: " + Integer.toString(dimId));
		}
	}

	private void generateOverworld(World world, Random rand, int blockX, int blockZ) {
		if ((int) (rand.nextFloat() * new MoreMystcraftConfig().getStudyFrequency()) == 0) {
			int y = getGroundFromAbove(world, blockX, blockZ);
			BlockPos pos = new BlockPos(blockX, y, blockZ);
			// Don't spawn on these blocks.
			if (world.getBlockState(pos).getMaterial().isLiquid() == true
					|| world.getBlockState(pos).getMaterial() == Material.LEAVES
					|| world.getBlockState(pos).getMaterial() == Material.WOOD) {
				return;
			}
			HashMap<String, IBlockState> blockMap = biomeMap.get(world.getBiome(pos));
			if (blockMap == null) {
				blockMap = biomeMap.get(Biomes.PLAINS);
			}
			WorldGenerator structure = new SubWorldGenStudy(blockMap);
			structure.generate(world, rand, pos.add(0, -4, 0));
		}
	}

	private void generateNether(World world, Random rand, int chunkX, int chunkZ) {
	}

	private void generateEnd(World world, Random rand, int chunkX, int chunkZ) {
	}

	public static int getGroundFromAbove(World world, int x, int z) {
		int y = 255;
		boolean foundGround = false;
		while (!foundGround && y-- >= new MoreMystcraftConfig().getStudyMinimumY()) {
			IBlockState blockAt = world.getBlockState(new BlockPos(x, y, z));
			foundGround = blockAt.getMaterial().isSolid() == true;
		}

		return y;
	}

	public static boolean canSpawnHere(Template template, World world, BlockPos posAboveGround) {
		int zwidth = template.getSize().getZ();
		int xwidth = template.getSize().getX();

		// check all the corners to see which ones are replaceable
		boolean corner1 = isCornerValid(world, posAboveGround);
		boolean corner2 = isCornerValid(world, posAboveGround.add(xwidth, 0, zwidth));

		// if Y > 20 and all corners pass the test, it's okay to spawn the structure
		return posAboveGround.getY() > 31 && corner1 && corner2;
	}

	public static boolean isCornerValid(World world, BlockPos pos) {
		int variation = 3;
		int highestBlock = getGroundFromAbove(world, pos.getX(), pos.getZ());

		if (highestBlock > pos.getY() - variation && highestBlock < pos.getY() + variation)
			return true;

		return false;
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		return false;
	}

	protected static HashMap<String, IBlockState> assignBlocks(IBlockState log, IBlockState planks, IBlockState slab,
			IBlockState stairs) {
		IBlockState stairsSouthBottomStraight = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
		IBlockState stairsNorthBottomStraight = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
		IBlockState stairsSouthTopStraight = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
		IBlockState stairsNorthTopStraight = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
		IBlockState stairsWestBottomStraight = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.WEST)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
		IBlockState stairsNorthBottomOuterLeft = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
		IBlockState stairsSouthBottomOuterRight = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);
		IBlockState stairsWestTopStraight = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.WEST)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
		IBlockState stairsEastTopStraight = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.EAST)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
		IBlockState stairsNorthTopInnerLeft = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
		IBlockState stairsNorthTopInnerRight = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.NORTH)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
		IBlockState stairsSouthTopInnerLeft = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
		IBlockState stairsSouthTopInnerRight = stairs.withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH)
				.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
		HashMap<String, IBlockState> map = new HashMap<>();
		map.put("log", log);
		map.put("planks", planks);
		map.put("slab", slab);
		map.put("stairs_south_bottom_straight", stairsSouthBottomStraight);
		map.put("stairs_north_bottom_straight", stairsNorthBottomStraight);
		map.put("stairs_south_top_straight", stairsSouthTopStraight);
		map.put("stairs_north_top_straight", stairsNorthTopStraight);
		map.put("stairs_west_bottom_straight", stairsWestBottomStraight);
		map.put("stairs_north_bottom_outer_left", stairsNorthBottomOuterLeft);
		map.put("stairs_south_bottom_outer_right", stairsSouthBottomOuterRight);
		map.put("stairs_west_top_straight", stairsWestTopStraight);
		map.put("stairs_east_top_straight", stairsEastTopStraight);
		map.put("stairs_north_top_inner_left", stairsNorthTopInnerLeft);
		map.put("stairs_north_top_inner_right", stairsNorthTopInnerRight);
		map.put("stairs_south_top_inner_left", stairsSouthTopInnerLeft);
		map.put("stairs_south_top_inner_right", stairsSouthTopInnerRight);
		return map;
	}

	static {

		HashMap<String, IBlockState> mapOak = assignBlocks(
				Blocks.LOG.getDefaultState().withProperty(BlockOldLog.LOG_AXIS, BlockOldLog.EnumAxis.Y)
						.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK),
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK),
				Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK),
				Blocks.OAK_STAIRS.getDefaultState());

		HashMap<String, IBlockState> mapAcacia = assignBlocks(
				Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.LOG_AXIS, BlockNewLog.EnumAxis.Y)
						.withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA),
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA),
				Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA),
				Blocks.ACACIA_STAIRS.getDefaultState());

		HashMap<String, IBlockState> mapBirch = assignBlocks(
				Blocks.LOG.getDefaultState().withProperty(BlockOldLog.LOG_AXIS, BlockOldLog.EnumAxis.Y)
						.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH),
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH),
				Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.BIRCH),
				Blocks.BIRCH_STAIRS.getDefaultState());

		HashMap<String, IBlockState> mapDarkOak = assignBlocks(
				Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.LOG_AXIS, BlockNewLog.EnumAxis.Y)
						.withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK),
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK),
				Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.DARK_OAK),
				Blocks.DARK_OAK_STAIRS.getDefaultState());

		HashMap<String, IBlockState> mapJungle = assignBlocks(
				Blocks.LOG.getDefaultState().withProperty(BlockOldLog.LOG_AXIS, BlockOldLog.EnumAxis.Y)
						.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE),
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE),
				Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.JUNGLE),
				Blocks.JUNGLE_STAIRS.getDefaultState());

		HashMap<String, IBlockState> mapSpruce = assignBlocks(
				Blocks.LOG.getDefaultState().withProperty(BlockOldLog.LOG_AXIS, BlockOldLog.EnumAxis.Y)
						.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE),
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE),
				Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE),
				Blocks.SPRUCE_STAIRS.getDefaultState());

		HashMap<String, IBlockState> mapSandstone = assignBlocks(
				Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH),
				Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.DEFAULT),
				Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND),
				Blocks.SANDSTONE_STAIRS.getDefaultState());

		HashMap<String, IBlockState> mapStone = assignBlocks(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE),
				Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
						BlockStoneBrick.EnumType.DEFAULT),
				Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT,
						BlockStoneSlab.EnumType.COBBLESTONE),
				Blocks.STONE_STAIRS.getDefaultState());

		biomeMap.put(Biomes.BEACH, mapOak);
		biomeMap.put(Biomes.BIRCH_FOREST, mapBirch);
		biomeMap.put(Biomes.BIRCH_FOREST_HILLS, mapBirch);
		biomeMap.put(Biomes.COLD_BEACH, mapSpruce);
		biomeMap.put(Biomes.COLD_TAIGA, mapSpruce);
		biomeMap.put(Biomes.DEEP_OCEAN, mapOak);
		biomeMap.put(Biomes.DEFAULT, mapOak);
		biomeMap.put(Biomes.DESERT, mapSandstone);
		biomeMap.put(Biomes.DESERT_HILLS, mapSandstone);
		biomeMap.put(Biomes.EXTREME_HILLS, mapSpruce);
		biomeMap.put(Biomes.EXTREME_HILLS_EDGE, mapSpruce);
		biomeMap.put(Biomes.EXTREME_HILLS_WITH_TREES, mapSpruce);
		biomeMap.put(Biomes.FOREST, mapOak);
		biomeMap.put(Biomes.FOREST_HILLS, mapOak);
		biomeMap.put(Biomes.FROZEN_OCEAN, mapSpruce);
		biomeMap.put(Biomes.FROZEN_RIVER, mapOak);
		biomeMap.put(Biomes.HELL, mapOak);
		biomeMap.put(Biomes.ICE_MOUNTAINS, mapSpruce);
		biomeMap.put(Biomes.ICE_PLAINS, mapSpruce);
		biomeMap.put(Biomes.JUNGLE, mapJungle);
		biomeMap.put(Biomes.JUNGLE_EDGE, mapJungle);
		biomeMap.put(Biomes.JUNGLE_HILLS, mapJungle);
		biomeMap.put(Biomes.MESA, mapAcacia);
		biomeMap.put(Biomes.MESA_CLEAR_ROCK, mapAcacia);
		biomeMap.put(Biomes.MESA_ROCK, mapAcacia);
		biomeMap.put(Biomes.MUSHROOM_ISLAND, mapOak);
		biomeMap.put(Biomes.MUSHROOM_ISLAND_SHORE, mapOak);
		biomeMap.put(Biomes.MUTATED_BIRCH_FOREST, mapBirch);
		biomeMap.put(Biomes.MUTATED_BIRCH_FOREST_HILLS, mapSpruce);
		biomeMap.put(Biomes.MUTATED_DESERT, mapSandstone);
		biomeMap.put(Biomes.MUTATED_EXTREME_HILLS, mapSpruce);
		biomeMap.put(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES, mapSpruce);
		biomeMap.put(Biomes.MUTATED_FOREST, mapOak);
		biomeMap.put(Biomes.MUTATED_ICE_FLATS, mapSpruce);
		biomeMap.put(Biomes.MUTATED_JUNGLE, mapJungle);
		biomeMap.put(Biomes.MUTATED_JUNGLE_EDGE, mapJungle);
		biomeMap.put(Biomes.MUTATED_MESA, mapAcacia);
		biomeMap.put(Biomes.MUTATED_MESA_CLEAR_ROCK, mapAcacia);
		biomeMap.put(Biomes.MUTATED_MESA_ROCK, mapAcacia);
		biomeMap.put(Biomes.MUTATED_PLAINS, mapOak);
		biomeMap.put(Biomes.MUTATED_REDWOOD_TAIGA, mapSpruce);
		biomeMap.put(Biomes.MUTATED_REDWOOD_TAIGA_HILLS, mapSpruce);
		biomeMap.put(Biomes.MUTATED_ROOFED_FOREST, mapDarkOak);
		biomeMap.put(Biomes.MUTATED_SAVANNA, mapAcacia);
		biomeMap.put(Biomes.MUTATED_SAVANNA_ROCK, mapAcacia);
		biomeMap.put(Biomes.MUTATED_SWAMPLAND, mapOak);
		biomeMap.put(Biomes.MUTATED_TAIGA, mapSpruce);
		biomeMap.put(Biomes.MUTATED_TAIGA_COLD, mapSpruce);
		biomeMap.put(Biomes.OCEAN, mapOak);
		biomeMap.put(Biomes.PLAINS, mapOak);
		biomeMap.put(Biomes.REDWOOD_TAIGA, mapSpruce);
		biomeMap.put(Biomes.REDWOOD_TAIGA_HILLS, mapSpruce);
		biomeMap.put(Biomes.RIVER, mapOak);
		biomeMap.put(Biomes.ROOFED_FOREST, mapDarkOak);
		biomeMap.put(Biomes.SAVANNA, mapAcacia);
		biomeMap.put(Biomes.SAVANNA_PLATEAU, mapAcacia);
		biomeMap.put(Biomes.SKY, mapStone);
		biomeMap.put(Biomes.STONE_BEACH, mapStone);
		biomeMap.put(Biomes.SWAMPLAND, mapOak);
		biomeMap.put(Biomes.TAIGA, mapSpruce);
		biomeMap.put(Biomes.TAIGA_HILLS, mapSpruce);
		biomeMap.put(Biomes.VOID, mapStone);
	}
}