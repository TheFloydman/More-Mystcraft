package thefloydman.moremystcraft.world.gen.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.google.common.collect.Maps;

import mystlibrary.book.BookGenerator;
import mystlibrary.book.BookPage;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import thefloydman.moremystcraft.util.Reference;

public class SubWorldGenStudy extends WorldGenerator {

	Map<String, IBlockState> blockMap;

	public SubWorldGenStudy(final Map<String, IBlockState> map) {
		this.blockMap = map;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		WorldServer worldserver = (WorldServer) world;
		MinecraftServer minecraftserver = world.getMinecraftServer();
		TemplateManager templatemanager = worldserver.getStructureTemplateManager();
		Template template = templatemanager.getTemplate(minecraftserver,
				new ResourceLocation(Reference.MOD_ID, "abandoned_study_version_2"));

		if (template == null) {
			System.out.println("Structure not found: abandoned_study_version_2");
			return false;
		}

		if (WorldGenStudy.canSpawnHere(template, worldserver, position.add(0, 4, 0))) {
			IBlockState iblockstate = world.getBlockState(position.add(0, 4, 0));
			world.notifyBlockUpdate(position.add(0, 4, 0), iblockstate, iblockstate, 3);

			PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE)
					.setRotation(Rotation.NONE).setIgnoreEntities(false).setChunk((ChunkPos) null)
					.setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			template.getDataBlocks(position.add(0, 4, 0), placementsettings);
			template.addBlocksToWorld(world, position, placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(position, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {

				if (entry.getValue().equals("chest")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = world.getTileEntity(blockpos2.down());

					if (tileentity instanceof TileEntityChest) {
						((TileEntityChest) tileentity)
								.setLootTable(new ResourceLocation("mystcraft", "mystcraft_treasure"), rand.nextLong());
					}
				} else if (entry.getValue().equals("cobblestone_down")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, Blocks.COBBLESTONE.getDefaultState(), 3);
					fillBelow(world, blockpos2.down(), Blocks.COBBLESTONE.getDefaultState());
				} else if (entry.getValue().equals("link_point")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, Blocks.AIR.getDefaultState(), 3);

					// Add Descriptive Book.
					TileEntity entityLecternLeft = world.getTileEntity(blockpos2.add(-4, 1, 1));
					IItemHandler handlerLecternLeft = entityLecternLeft
							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					List<BookPage> pageList = new ArrayList<BookPage>();
					pageList.add(BookPage.linkPanelPage());
					pageList.add(BookPage.symbolPage(new ResourceLocation("mystcraft:starfissure")));
					int randMain = rand.nextInt(100);
					int randSub = 0;
					if (randMain <= 50) {
						randSub = rand.nextInt(10);
					} else if (randMain >= 51 && randMain <= 80) {
						randSub = rand.nextInt(20) + 10;
					} else if (randMain >= 81 && randMain <= 95) {
						randSub = rand.nextInt(40) + 30;
					} else {
						randSub = rand.nextInt(30) + 70;
					}
					for (int i = 0; i < randSub; i++) {
						pageList.add(BookPage.blankPage());
					}
					ItemStack stackDesBook = BookGenerator.generateDescriptiveBook(pageList, "Unexplored Age",
							Arrays.asList("Unknown Author"));
					handlerLecternLeft.insertItem(0, stackDesBook, false);

					// Add Linking Book.
					TileEntity entityLecternRight = world.getTileEntity(blockpos2.add(-4, 1, -1));
					IItemHandler handlerLecternRight = entityLecternRight
							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					handlerLecternRight.insertItem(0, BookGenerator.generateLinkingBook(blockpos2, 90.0F,
							world.provider.getDimension(), "Abandoned Study", Maps.newHashMap()), false);
				} else if (entry.getValue().equals("log")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("log"));
				} else if (entry.getValue().equals("planks")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("planks"));
				} else if (entry.getValue().equals("slab")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("slab"));
				} else if (entry.getValue().equals("stairs_south_bottom_straight")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_south_bottom_straight"));
				} else if (entry.getValue().equals("stairs_north_bottom_straight")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_north_bottom_straight"));
				} else if (entry.getValue().equals("stairs_south_top_straight")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_south_top_straight"));
				} else if (entry.getValue().equals("stairs_north_top_straight")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_north_top_straight"));
				} else if (entry.getValue().equals("stairs_west_bottom_straight")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_west_bottom_straight"));
				} else if (entry.getValue().equals("stairs_north_bottom_outer_left")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_north_bottom_outer_left"));
				} else if (entry.getValue().equals("stairs_south_bottom_outer_right")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_south_bottom_outer_right"));
				} else if (entry.getValue().equals("stairs_west_top_straight")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_west_top_straight"));
				} else if (entry.getValue().equals("stairs_east_top_straight")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_east_top_straight"));
				} else if (entry.getValue().equals("stairs_north_top_inner_left")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_north_top_inner_left"));
				} else if (entry.getValue().equals("stairs_north_top_inner_right")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_north_top_inner_right"));
				} else if (entry.getValue().equals("stairs_south_top_inner_left")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_south_top_inner_left"));
				} else if (entry.getValue().equals("stairs_south_top_inner_right")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, this.blockMap.get("stairs_south_top_inner_right"));
				}

			}

			return true;
		}

		return false;
	}

	public void addLoot(World world) {

	}

	private void fillBelow(World world, BlockPos pos, IBlockState state) {
		boolean empty = true;
		while (empty == true && pos.getY() >= 0) {
			if (world.getBlockState(pos) == Blocks.AIR.getDefaultState()
					|| world.getBlockState(pos) == Blocks.STRUCTURE_VOID.getDefaultState()
					|| world.getBlockState(pos) == Blocks.WATER.getDefaultState()
					|| world.getBlockState(pos) == Blocks.FLOWING_WATER.getDefaultState()
					|| world.getBlockState(pos) == Blocks.LAVA.getDefaultState()
					|| world.getBlockState(pos) == Blocks.FLOWING_LAVA.getDefaultState()
					|| world.getBlockState(pos) == Blocks.TALLGRASS.getDefaultState()
					|| world.getBlockState(pos) == Blocks.DOUBLE_PLANT.getDefaultState()
					|| world.getBlockState(pos) == Blocks.LOG.getDefaultState()
					|| world.getBlockState(pos) == Blocks.LEAVES.getDefaultState()) {
				world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState(), 3);
				pos = pos.down();
			} else {
				empty = false;
			}
		}
	}

}