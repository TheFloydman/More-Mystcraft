package thefloydman.moremystcraft.world.gen.structure.feature;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.IWorldGenerator;

import com.xcompwiz.mystcraft.api.hook.DimensionAPI;
import com.xcompwiz.mystcraft.world.agedata.AgeData;
import com.xcompwiz.mystcraft.world.agedata.AgeData.AgeDataData;
import com.xcompwiz.mystcraft.api.impl.linking.DimensionAPIWrapper;

import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.proxy.CommonProxy;

public class WorldGeneratorAbandonedStudy extends WorldGenerator implements IWorldGenerator {

	static Random rand2 = new Random();

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
			generateOverworld(world, rand, blockX + 8, blockZ + 8);
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
			if (dimId < 0 || MoreMystcraft.proxy.dimensionApi.isMystcraftAge(dimId) == false) {
				return;
			}
			if (MoreMystcraft.proxy.dimensionApi.isMystcraftAge(dimId) == true) {
				AgeData data = new AgeData("currentDim").getAge(dimId, false);
				List symbolList = data.getSymbols(false);
				ResourceLocation studyLoc = new ResourceLocation("moremystcraft", "abandoned_study");
				for (int i = 0; i < symbolList.size(); i++) {
					if (symbolList.get(i).equals(studyLoc) == true) {
						generateOverworld(world, rand, blockX + 8, blockZ + 8);
					}
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Null Pointer Exception!");
			System.out.println(e.getStackTrace());
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid Dimension ID: " + Integer.toString(dimId));
		}
	}

	private void generateOverworld(World world, Random rand, int blockX, int blockZ) {
		if ((int) (Math.random() * 0) == 0) {
			int y = getGroundFromAbove(world, blockX, blockZ);
			BlockPos pos = new BlockPos(blockX, y, blockZ);
			// Don't spawn on these blocks.
			if (world.getBlockState(pos) == Blocks.WATER.getDefaultState()
					|| world.getBlockState(pos) == Blocks.FLOWING_WATER.getDefaultState()
					|| world.getBlockState(pos) == Blocks.LAVA.getDefaultState()
					|| world.getBlockState(pos) == Blocks.FLOWING_LAVA.getDefaultState()
					|| world.getBlockState(pos) == Blocks.LEAVES.getDefaultState()
					|| world.getBlockState(pos) == Blocks.LEAVES2.getDefaultState()
					|| world.getBlockState(pos) == Blocks.LOG.getDefaultState()
					|| world.getBlockState(pos) == Blocks.LOG2.getDefaultState()) {
				return;
			}
			WorldGenerator structure = new SubWorldGeneratorAbandonedStudy();
			structure.generate(world, rand, pos.add(0, -5, 0));
		}
	}

	private void generateNether(World world, Random rand, int chunkX, int chunkZ) {
	}

	private void generateEnd(World world, Random rand, int chunkX, int chunkZ) {
	}

	private void addOreSpawn(IBlockState block, World world, Random random, int blockXPos, int blockZPos, int maxX,
			int maxZ, int maxVeinSize, int chanceToSpawn, int minY, int maxY) {
		for (int i = 0; i < chanceToSpawn; i++) {
			int posX = blockXPos + random.nextInt(maxX);
			int posY = minY + random.nextInt(maxY - minY);
			int posZ = blockZPos + random.nextInt(maxZ);
			new WorldGenMinable(block, maxVeinSize).generate(world, random, new BlockPos(posX, posY, posZ));
		}
	}

	public static int getGroundFromAbove(World world, int x, int z) {
		int y = 255;
		boolean foundGround = false;
		while (!foundGround && y-- >= 64) {
			Block blockAt = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			foundGround = blockAt != Blocks.AIR && blockAt != Blocks.WATER && blockAt != Blocks.FLOWING_WATER
					&& blockAt != Blocks.LAVA && blockAt != Blocks.FLOWING_LAVA && blockAt != Blocks.LOG
					&& blockAt != Blocks.LOG2 && blockAt != Blocks.LEAVES && blockAt != Blocks.LEAVES2;
		}

		return y;
	}

	public static int getLakeFromAbove(World world, int x, int z) {
		int y = 255;
		boolean foundGround = false;
		while (!foundGround && y-- >= 64) {
			Block blockAt = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			foundGround = blockAt == Blocks.WATER || blockAt == Blocks.FLOWING_WATER;
		}

		return y;
	}

	public void vineGen(World world, int x, int z) {

		int y = 255;

		Block blockAt;
		BlockPos pos = new BlockPos(x, y, z);
		boolean foundGround = false;
		boolean foundSide = false;
		int r = rand2.nextInt(4);

		int i = 0;
		int drop = 1;
		while (!foundGround && y-- >= 64) {

			blockAt = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			foundGround = blockAt == Blocks.WATER || blockAt == Blocks.FLOWING_WATER || blockAt == Blocks.GRASS
					|| blockAt == Blocks.SAND || blockAt == Blocks.SNOW || blockAt == Blocks.SNOW_LAYER
					|| blockAt == Blocks.GLASS || blockAt == Blocks.MYCELIUM;

		}

		while (!foundSide && ++i <= 48) {

			switch (r) {
			case 0:
				x++;
			case 1:
				x--;
			case 2:
				z++;
			case 3:
				z--;
			}
			// System.out.println(r);
			// System.out.println(new BlockPos(x,y,z));
			blockAt = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			foundSide = blockAt == Blocks.AIR
					&& world.getBlockState(new BlockPos(x, y - drop, z)).getBlock() == Blocks.AIR;

		}

		i = 0;
		BlockPos blockpos$mutableblockpos1 = new BlockPos(x, y, z);

		// System.out.println(world.getBlockState(blockpos$mutableblockpos1).getMaterial()
		// == Material.AIR);

		if ((world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR)) {
			BlockPos blockpos3 = blockpos$mutableblockpos1.west();
			BlockPos blockpos4 = blockpos$mutableblockpos1.east();
			BlockPos blockpos1 = blockpos$mutableblockpos1.north();
			BlockPos blockpos2 = blockpos$mutableblockpos1.south();

			if ((world.isAirBlock(blockpos3))) {
				// System.out.println("EAST");
				addVine(world, blockpos3, BlockVine.EAST);

			}

			if (world.isAirBlock(blockpos4)) {
				addVine(world, blockpos4, BlockVine.WEST);
				// System.out.println("WEST");
			}

			if (world.isAirBlock(blockpos1)) {
				addVine(world, blockpos1, BlockVine.SOUTH);
				// System.out.println("SOUTH");
			}

			if (world.isAirBlock(blockpos2)) {
				addVine(world, blockpos2, BlockVine.NORTH);
				// System.out.println("NORTH");
			}

		}

	}

	private void addVine(World worldIn, BlockPos pos, PropertyBool prop) {
		IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty(prop, Boolean.valueOf(true));
		this.setBlockAndNotifyAdequately(worldIn, pos, iblockstate);
		int i = 15 + rand2.nextInt(15);
		;

		for (BlockPos blockpos = pos.down(); worldIn.isAirBlock(blockpos) && i > 0; --i) {
			setBlockAndNotifyAdequately(worldIn, blockpos, iblockstate);
			blockpos = blockpos.down();
		}
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
}