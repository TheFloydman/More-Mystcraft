package thefloydman.moremystcraft.world.gen.feature;

import java.util.Map;
import java.util.Random;

import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;

import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
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
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import com.xcompwiz.mystcraft.block.BlockLectern;
import com.xcompwiz.mystcraft.item.ItemAgebook;

import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.util.BookSpawner;
import thefloydman.moremystcraft.util.Reference;

public class SubWorldGenStudy extends WorldGenerator {
	Random r2 = new Random();

	int r;

	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		WorldServer worldserver = (WorldServer) world;
		MinecraftServer minecraftserver = world.getMinecraftServer();
		TemplateManager templatemanager = worldserver.getStructureTemplateManager();
		Template template = templatemanager.getTemplate(minecraftserver,
				new ResourceLocation(Reference.MOD_ID, "abandoned_study"));

		if (template == null) {
			System.out.println("Structure not found: abandoned_study");
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
				}

				if (entry.getValue().equals("cobblestone_down")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, Blocks.COBBLESTONE.getDefaultState(), 3);
					fillBelow(world, blockpos2.down(), Blocks.COBBLESTONE.getDefaultState());
				}

				// Add a blank Descriptive Book and a Linking Book back to the study.
				if (entry.getValue().equals("link_point")) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2, Blocks.AIR.getDefaultState(), 3);

					// Add Descriptive Book.
					TileEntity entityLecternLeft = world.getTileEntity(blockpos2.add(-4, 1, 1));
					IItemHandler handlerLecternLeft = entityLecternLeft
							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					ItemStack stackDesBook = BookSpawner.generateBlankDescriptiveBook(rand);
					handlerLecternLeft.insertItem(0, stackDesBook, false);

					// Add Linking Book.
					TileEntity entityLecternRight = world.getTileEntity(blockpos2.add(-4, 1, -1));
					IItemHandler handlerLecternRight = entityLecternRight
							.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					ItemStack stackBook = new ItemStack(Item.getByNameOrId("mystcraft:linkbook"));
					NBTTagCompound compoundBook = new NBTTagCompound();
					compoundBook.setInteger("SpawnX", blockpos2.getX());
					compoundBook.setInteger("SpawnY", blockpos2.getY());
					compoundBook.setInteger("SpawnZ", blockpos2.getZ());
					compoundBook.setFloat("SpawnYaw", 90);
					compoundBook.setInteger("Dimension", world.provider.getDimension());
					compoundBook.setString("DisplayName", "Abandoned Study");
					compoundBook.setFloat("MaxHealth", 10);
					compoundBook.setFloat("damage", 0);
					compoundBook.setString("TargetUUID", "00000000-0000-0000-0000-000000000000");
					stackBook.setTagCompound(compoundBook);
					handlerLecternRight.insertItem(0, stackBook, false);
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