package thefloydman.moremystcraft.world.gen.feature;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.xcompwiz.mystcraft.Mystcraft;
import com.xcompwiz.mystcraft.block.BlockLectern;
import com.xcompwiz.mystcraft.data.ModBlocks;
import com.xcompwiz.mystcraft.nbt.NBTUtils;
import com.xcompwiz.mystcraft.tileentity.TileEntityLectern;
import com.xcompwiz.mystcraft.treasure.LootTableHandler;
import com.xcompwiz.mystcraft.world.agedata.AgeData;
import com.xcompwiz.mystcraft.world.gen.feature.WorldGeneratorAdv;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlab.EnumType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.IWorldGenerator;
import scala.actors.threadpool.Arrays;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.util.Reference;

public class WorldGenLibraryReplacement implements IWorldGenerator {

	List<String> changedLibraries = new ArrayList<String>();
	List<String> unchangedLibraries = new ArrayList<String>();
	boolean processing = false;
	int generationSetting;
	
	public WorldGenLibraryReplacement() {
		MoreMystcraftConfig config = new MoreMystcraftConfig();
		if (config.getLibrariesEnabled()) {
			if (config.getLibrariesUpgraded()) {
				this.generationSetting = generationSettings.UPGRADED_LIBRARIES.ordinal();
			} else {
				this.generationSetting = generationSettings.DEFAULT.ordinal();
			}
		} else {
			this.generationSetting = generationSettings.NO_LIBRARIES.ordinal();
		}
	}
	
	public WorldGenLibraryReplacement(final int genSet) {
		this.generationSetting = genSet;
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {

		if (this.processing == true) {
			return;
		}

		if (!world.provider.getDimensionType().equals(Mystcraft.dimensionType)) {
			return;
		}
		
		// If a "No Libraries" page is present, set to 
		int dimId = world.provider.getDimension();
		try {
			AgeData data = new AgeData("currentDim").getAge(dimId, false);
			List symbolList = data.getSymbols(false);
			ResourceLocation studyLoc = Reference.forMoreMystcraft("no_libraries");
			if (symbolList.contains(studyLoc)) {
				this.generationSetting = generationSettings.NO_LIBRARIES.ordinal();
			} else {
				if (new MoreMystcraftConfig().getLibrariesUpgraded()) {
					this.generationSetting = generationSettings.UPGRADED_LIBRARIES.ordinal();
				} else {
					this.generationSetting = generationSettings.DEFAULT.ordinal();
				}
			}
		} catch (NullPointerException e) {
			return;
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid Dimension ID: " + Integer.toString(dimId));
		}

		this.processing = true;

		NBTTagCompound libraries = loadLibraryFile(world);
		if (libraries == null) {
			this.processing = false;
			return;
		}
		this.unchangedLibraries.addAll(libraries.getKeySet());
		for (int i = 0; i < this.unchangedLibraries.size(); i++) {
			if (this.changedLibraries.contains(this.unchangedLibraries.get(i))
					|| this.unchangedLibraries.contains(this.unchangedLibraries.get(i))) {
				this.unchangedLibraries.remove(i);
				i -= 1;
			}
			this.updateLibraries(world, libraries, new ChunkPos(chunkX, chunkZ));
		}
		this.processing = false;
	}

	private void updateLibraries(final World world, final NBTTagCompound libraries, final ChunkPos chunkPos) {
		while (this.unchangedLibraries.size() > 0) {
			int separator = this.unchangedLibraries.get(0).indexOf(",");
			if (separator < 0) {
				return;
			}
			int listedX = Integer.valueOf(this.unchangedLibraries.get(0).substring(1, separator));
			int listedZ = Integer.valueOf(this.unchangedLibraries.get(0).substring(separator + 1,
					this.unchangedLibraries.get(0).length() - 1));
			int[] libraryBounds = libraries.getCompoundTag("[" + listedX + "," + listedZ + "]").getIntArray("BB");
			if (!isMystcraftLibraryGenerated(world, new BlockPos(libraryBounds[0], 64, libraryBounds[2]))) {
				this.unchangedLibraries.remove(0);
				continue;
			}
			this.updateLibrary(world, libraries,
					new ChunkPos(new BlockPos(libraryBounds[0], libraryBounds[1], libraryBounds[2])));
			this.changedLibraries.add(unchangedLibraries.get(0));
			this.unchangedLibraries.remove(0);
		}
	}

	protected boolean isMystcraftLibraryGenerated(final World world, BlockPos pos) {
		while (pos.getY() <= 255) {
			if (world.getBlockState(pos.east(1).south(4)).getBlock().equals(Blocks.COBBLESTONE_WALL)
					&& world.getBlockState(pos.east(10).up(6).south(10)).getBlock().equals(Blocks.STONE_STAIRS)
					&& world.getBlockState(pos.east(10).up(6)).getBlock().equals(Blocks.STONE_STAIRS)
					&& world.getBlockState(pos.south(10).up(6).east(2)).getBlock().equals(Blocks.STONE_STAIRS)) {
				return true;
			}
			if (world.getBlockState(pos.east(4).south(1)).getBlock().equals(Blocks.COBBLESTONE_WALL)
					&& world.getBlockState(pos.east(10).up(6).south(10)).getBlock().equals(Blocks.STONE_STAIRS)
					&& world.getBlockState(pos.east(10).up(6).south(2)).getBlock().equals(Blocks.STONE_STAIRS)
					&& world.getBlockState(pos.south(10).up(6)).getBlock().equals(Blocks.STONE_STAIRS)) {
				return true;
			}
			pos = pos.up();
		}
		return false;
	}

	protected void updateLibrary(final World world, final NBTTagCompound libraries, final ChunkPos chunkPos) {

		int[] libraryBounds = libraries.getCompoundTag("[" + chunkPos.x + "," + chunkPos.z + "]").getIntArray("BB");
		BlockPos pos = new BlockPos(libraryBounds[0], 64, libraryBounds[2]);
		EnumFacing facing = EnumFacing.NORTH;
		while (pos.getY() <= 255) {
			if (world.getBlockState(pos.east(1).south(4)).getBlock().equals(Blocks.COBBLESTONE_WALL)) {
				facing = EnumFacing.WEST;
				break;
			} else if (world.getBlockState(pos.east(4).south(1)).getBlock().equals(Blocks.COBBLESTONE_WALL)) {
				facing = EnumFacing.NORTH;
				break;
			}
			pos = pos.up();
		}

		if (this.generationSetting == generationSettings.NO_LIBRARIES.ordinal()) {
			this.removeLibrary(world, pos.down(2), facing);
			this.removePedestal(world, pos.down(2), facing);
		} else if (this.generationSetting == generationSettings.UPGRADED_LIBRARIES.ordinal()) {
			if (new Random().nextInt(100) < new MoreMystcraftConfig().getGreatLibraryPercentage()) {
				this.removeLibrary(world, pos.down(2), facing);
				if (facing.equals(EnumFacing.WEST))
					pos = pos.south(15);
				this.generateGreatLibrary(world, pos.down(), facing);
			} else {
				this.generateUpgradedLibrary(world, pos.down(2), facing);
			}
		}

	}

	protected void generateUpgradedLibrary(final World world, final BlockPos pos, EnumFacing facing) {

		for (int y = pos.getY() + 1; y < pos.getY() + 12; y++) {
			for (int x = pos.getX(); x < pos.getX() + 16; x++) {
				for (int z = pos.getZ(); z < pos.getZ() + 16; z++) {
					if (world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.COBBLESTONE)) {
						world.setBlockState(new BlockPos(x, y, z), Blocks.STONEBRICK.getDefaultState());
					} else if (world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.STONE_STAIRS)) {
						EnumFacing propFacing = (EnumFacing) world.getBlockState(new BlockPos(x, y, z)).getProperties()
								.get(BlockStairs.FACING);
						EnumHalf propHalf = (EnumHalf) world.getBlockState(new BlockPos(x, y, z)).getProperties()
								.get(BlockStairs.HALF);
						EnumShape propShape = (EnumShape) world.getBlockState(new BlockPos(x, y, z)).getProperties()
								.get(BlockStairs.SHAPE);
						world.setBlockState(new BlockPos(x, y, z),
								Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, propFacing)
										.withProperty(BlockStairs.HALF, propHalf)
										.withProperty(BlockStairs.SHAPE, propShape));
					} else if (world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.STONE_SLAB)) {
						EnumBlockHalf propHalf = (EnumBlockHalf) world.getBlockState(new BlockPos(x, y, z))
								.getProperties().get(BlockStoneSlab.HALF);
						world.setBlockState(new BlockPos(x, y, z),
								Blocks.STONE_SLAB.getDefaultState()
										.withProperty(BlockStoneSlab.VARIANT, EnumType.SMOOTHBRICK)
										.withProperty(BlockStoneSlab.HALF, propHalf));
					}
				}
			}
		}
		if (facing.equals(EnumFacing.WEST)) {
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			world.setBlockState(new BlockPos(x + 2, y + 2, z + 4),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 2, y + 2, z + 6),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 3, y + 2, z + 5),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 4, y + 2, z + 5),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 4, y + 2, z + 6),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 5, y + 2, z + 7),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 5, y + 2, z + 6),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 5, y + 2, z + 4),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 5, y + 2, z + 3),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 6, y + 2, z + 5),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 6, y + 2, z + 7),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 7, y + 2, z + 6),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 7, y + 2, z + 4),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 7, y + 2, z + 3),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
		} else {
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			world.setBlockState(new BlockPos(x + 4, y + 2, z + 2),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 6, y + 2, z + 2),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 5, y + 2, z + 3),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 5, y + 2, z + 4),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 6, y + 2, z + 4),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 7, y + 2, z + 5),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 6, y + 2, z + 5),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 4, y + 2, z + 5),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 3, y + 2, z + 5),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 5, y + 2, z + 6),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 7, y + 2, z + 6),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 6, y + 2, z + 7),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 4, y + 2, z + 7),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
			world.setBlockState(new BlockPos(x + 3, y + 2, z + 7),
					Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
		}
	}

	protected void generateGreatLibrary(final World world, final BlockPos pos, EnumFacing facing) {
		WorldServer worldserver = (WorldServer) world;
		MinecraftServer minecraftserver = world.getMinecraftServer();
		TemplateManager templatemanager = worldserver.getStructureTemplateManager();
		Template template = templatemanager.getTemplate(minecraftserver,
				new ResourceLocation(Reference.MOD_ID, "great_library"));

		PlacementSettings placementsettings;

		if (facing.equals(EnumFacing.NORTH)) {
			placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE)
					.setIgnoreEntities(true).setChunk(null).setReplacedBlock(null).setIgnoreStructureBlock(false);
		} else {
			placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE)
					.setRotation(Rotation.COUNTERCLOCKWISE_90).setIgnoreEntities(true).setChunk(null)
					.setReplacedBlock(null).setIgnoreStructureBlock(false);
		}

		template.addBlocksToWorld(world, pos, placementsettings);
		Map<BlockPos, String> map = template.getDataBlocks(pos, placementsettings);

		for (Entry<BlockPos, String> entry : map.entrySet()) {

			if (entry.getValue().equals("lectern_right")) {
				BlockPos dataPos = entry.getKey();
				if (facing.equals(EnumFacing.NORTH))
					world.setBlockState(dataPos,
							ModBlocks.lectern.getDefaultState().withProperty(BlockLectern.ROTATION, EnumFacing.WEST));
				if (facing.equals(EnumFacing.WEST))
					world.setBlockState(dataPos,
							ModBlocks.lectern.getDefaultState().withProperty(BlockLectern.ROTATION, EnumFacing.SOUTH));
				TileEntityLectern tileEntityLectern = (TileEntityLectern) world.getTileEntity(dataPos);
				if (tileEntityLectern != null) {
					final ItemStack item = LootTableHandler.generateLecternItem(tileEntityLectern, new Random(),
							world.getLootTableManager().getLootTableFromLocation(LootTableHandler.MYST_TREASURE),
							new LootContext.Builder((WorldServer) world).build());
					if (item != null) {
						tileEntityLectern.setBook(item);
					}
				}
			} else if (entry.getValue().equals("lectern_front")) {
				BlockPos dataPos = entry.getKey();
				if (facing.equals(EnumFacing.NORTH))
					world.setBlockState(dataPos,
							ModBlocks.lectern.getDefaultState().withProperty(BlockLectern.ROTATION, EnumFacing.NORTH));
				if (facing.equals(EnumFacing.WEST))
					world.setBlockState(dataPos,
							ModBlocks.lectern.getDefaultState().withProperty(BlockLectern.ROTATION, EnumFacing.WEST));
				TileEntityLectern tileEntityLectern = (TileEntityLectern) world.getTileEntity(dataPos);
				if (tileEntityLectern != null) {
					final ItemStack item = LootTableHandler.generateLecternItem(tileEntityLectern, new Random(),
							world.getLootTableManager().getLootTableFromLocation(LootTableHandler.MYST_TREASURE),
							new LootContext.Builder((WorldServer) world).build());
					if (item != null) {
						tileEntityLectern.setBook(item);
					}
				}
			} else if (entry.getValue().equals("lectern_left")) {
				BlockPos dataPos = entry.getKey();
				if (facing.equals(EnumFacing.NORTH))
					world.setBlockState(dataPos,
							ModBlocks.lectern.getDefaultState().withProperty(BlockLectern.ROTATION, EnumFacing.EAST));
				if (facing.equals(EnumFacing.WEST))
					world.setBlockState(dataPos,
							ModBlocks.lectern.getDefaultState().withProperty(BlockLectern.ROTATION, EnumFacing.NORTH));
				TileEntityLectern tileEntityLectern = (TileEntityLectern) world.getTileEntity(dataPos);
				if (tileEntityLectern != null) {
					final ItemStack item = LootTableHandler.generateLecternItem(tileEntityLectern, new Random(),
							world.getLootTableManager().getLootTableFromLocation(LootTableHandler.MYST_TREASURE),
							new LootContext.Builder((WorldServer) world).build());
					if (item != null) {
						tileEntityLectern.setBook(item);
					}
				}
			} else if (entry.getValue().equals("lectern_back")) {
				BlockPos dataPos = entry.getKey();
				if (facing.equals(EnumFacing.NORTH))
					world.setBlockState(dataPos,
							ModBlocks.lectern.getDefaultState().withProperty(BlockLectern.ROTATION, EnumFacing.SOUTH));
				if (facing.equals(EnumFacing.WEST))
					world.setBlockState(dataPos,
							ModBlocks.lectern.getDefaultState().withProperty(BlockLectern.ROTATION, EnumFacing.EAST));
				TileEntityLectern tileEntityLectern = (TileEntityLectern) world.getTileEntity(dataPos);
				if (tileEntityLectern != null) {
					final ItemStack item = LootTableHandler.generateLecternItem(tileEntityLectern, new Random(),
							world.getLootTableManager().getLootTableFromLocation(LootTableHandler.MYST_TREASURE),
							new LootContext.Builder((WorldServer) world).build());
					if (item != null) {
						tileEntityLectern.setBook(item);
					}
				}
			} else if (entry.getValue().equals("chest")) {
				BlockPos dataPos = entry.getKey();
				world.setBlockToAir(dataPos);
				TileEntityChest tileentity = (TileEntityChest) world.getTileEntity(dataPos.down());
				tileentity.setLootTable(new ResourceLocation("mystcraft", "mystcraft_treasure"),
						new Random().nextLong());
			}
		}

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				BlockPos currentPos = pos;
				if (facing.equals(EnumFacing.WEST))
					currentPos = currentPos.north(15);
				while (currentPos.getY() > 0) {
					if (world.getBlockState(currentPos.east(x).south(z).down()).getMaterial().isSolid())
						break;
					world.setBlockState(currentPos.east(x).south(z).down(), Blocks.COBBLESTONE.getDefaultState());
					currentPos = currentPos.down();
				}
			}
		}
	}

	protected void removeLibrary(final World world, final BlockPos pos, EnumFacing facing) {

		for (int y = pos.getY() + 1; y < pos.getY() + 12; y++) {
			for (int x = pos.getX(); x < pos.getX() + 16; x++) {
				for (int z = pos.getZ(); z < pos.getZ() + 16; z++) {
					world.removeTileEntity(new BlockPos(x, y, z));
					world.setBlockToAir(new BlockPos(x, y, z));
				}
			}
		}
	}

	protected void removePedestal(final World world, final BlockPos pos, EnumFacing facing) {

		int y = pos.getY();
		int x = pos.getX() - 1;
		int z = pos.getZ() - 1;

		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				y = pos.getY();
				for (int k = 0; y > 0; k--) {
					if (world.getBlockState(new BlockPos(x + i, y + k, z + j)).getBlock().equals(Blocks.COBBLESTONE)) {
						world.setBlockToAir(new BlockPos(x + i, y + k, z + j));
					} else {
						break;
					}
				}
			}
		}

	}

	protected NBTTagCompound loadLibraryFile(final World world) {
		File rootdir = null;
		NBTTagCompound libraries = null;
		try {
			rootdir = DimensionManager.getCurrentSaveRootDirectory().getCanonicalFile();
		} catch (IOException e1) {
		}
		final File dimensionfolder = new File(rootdir, "/" + world.provider.getSaveFolder());
		final File libraryFile = new File(dimensionfolder, "/data/MystLibrary.dat");
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(libraryFile);
		} catch (Exception e) {
		}

		try {
			libraries = CompressedStreamTools.readCompressed(inputStream).getCompoundTag("data")
					.getCompoundTag("Features");
		} catch (Exception e) {
		}

		return libraries;

	}
	
	static enum generationSettings {
		DEFAULT,
		NO_LIBRARIES,
		UPGRADED_LIBRARIES;
	}

}