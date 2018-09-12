package thefloydman.moremystcraft.world.gen.structure.feature;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
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
import net.minecraft.world.storage.loot.LootTableList;

import com.xcompwiz.mystcraft.tileentity.TileEntityInkMixer;

import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.util.Reference;

public class SubWorldGen extends WorldGenerator {
	Random r2 = new Random();

	int r;

	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		WorldServer worldserver = (WorldServer) world;
		MinecraftServer minecraftserver = world.getMinecraftServer();
		TemplateManager templatemanager = worldserver.getStructureTemplateManager();
		Template template = templatemanager.getTemplate(minecraftserver,
				new ResourceLocation(Reference.MOD_ID + ":abandoned_study"));

		if (template == null) {
			System.out.println("NO STRUCTURE");
			return false;
		}

		if (ModWorldGenerator.canSpawnHere(template, worldserver, position)) {
			IBlockState iblockstate = world.getBlockState(position);
			world.notifyBlockUpdate(position, iblockstate, iblockstate, 3);

			PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE)
					.setRotation(Rotation.NONE).setIgnoreEntities(false).setChunk((ChunkPos) null)
					.setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

			template.getDataBlocks(position, placementsettings);
			template.addBlocksToWorld(world, position.add(0, 1, 0), placementsettings);

			Map<BlockPos, String> map = template.getDataBlocks(position, placementsettings);

			for (Entry<BlockPos, String> entry : map.entrySet()) {

				if ("chest".equals(entry.getValue())) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2.up(), Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = world.getTileEntity(blockpos2);

					if (tileentity instanceof TileEntityChest) {
						((TileEntityChest) tileentity)
								.setLootTable(new ResourceLocation("mystcraft", "mystcraft_treasure"), rand.nextLong());
					}
				}

				if ("floor".equals(entry.getValue())) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2.up(), Blocks.PLANKS.getDefaultState(), 3);
					world.setBlockState(blockpos2, Blocks.COBBLESTONE.getDefaultState(), 3);
				}
				
				if ("floor_cobblestone_2".equals(entry.getValue())) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2.up(), Blocks.PLANKS.getDefaultState(), 3);
					world.setBlockState(blockpos2, Blocks.COBBLESTONE.getDefaultState(), 3);
					world.setBlockState(blockpos2.down(), Blocks.COBBLESTONE.getDefaultState(), 3);
				}
				
				if ("piston_redstone_air".equals(entry.getValue())) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2.up(), Blocks.STICKY_PISTON.getStateFromMeta(0), 3);
					world.setBlockState(blockpos2, Blocks.REDSTONE_BLOCK.getDefaultState(), 3);
					world.setBlockState(blockpos2.down(), Blocks.AIR.getDefaultState(), 3);
				}
				
				if ("floor_piston_cobblestone".equals(entry.getValue())) {
					BlockPos blockpos2 = entry.getKey();
					world.setBlockState(blockpos2.up(), Blocks.PLANKS.getDefaultState(), 3);
					world.setBlockState(blockpos2, Blocks.STICKY_PISTON.getStateFromMeta(1), 3);
					world.setBlockState(blockpos2.down(), Blocks.COBBLESTONE.getDefaultState(), 3);
				}

			}

			return true;
		}

		return false;
	}

	public void addLoot(World world) {

	}

}