package thefloydman.moremystcraft.world.gen.feature;

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
import thefloydman.moremystcraft.config.MoreMystcraftConfig;

public class WorldGenStudy extends WorldGenerator implements IWorldGenerator {

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
			} else if (MoreMystcraft.proxy.dimensionApi.isMystcraftAge(dimId) == true) {
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
			return;
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid Dimension ID: " + Integer.toString(dimId));
		}
	}

	private void generateOverworld(World world, Random rand, int blockX, int blockZ) {
		if ((int) (Math.random() * new MoreMystcraftConfig().getStudyFrequency()) == 0) {
			int y = getGroundFromAbove(world, blockX, blockZ);
			BlockPos pos = new BlockPos(blockX, y, blockZ);
			// Don't spawn on these blocks.
			if (world.getBlockState(pos).getMaterial().isLiquid() == true
					|| world.getBlockState(pos).getMaterial() == Material.LEAVES
					|| world.getBlockState(pos).getMaterial() == Material.WOOD) {
				return;
			}
			WorldGenerator structure = new SubWorldGenStudy();
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
}