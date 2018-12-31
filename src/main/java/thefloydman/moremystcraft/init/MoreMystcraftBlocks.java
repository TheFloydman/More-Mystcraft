package thefloydman.moremystcraft.init;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.xcompwiz.mystcraft.config.MystConfig;
import com.xcompwiz.mystcraft.data.ModRegistryPrimer;

import thefloydman.moremystcraft.block.BlockTrafficCone;
import thefloydman.moremystcraft.block.BlockUnstableBookReceptacle;
import thefloydman.moremystcraft.block.BlockUnstablePortal;
import thefloydman.moremystcraft.block.BlockLockedBookstand;
import thefloydman.moremystcraft.block.BlockLockedLectern;
import thefloydman.moremystcraft.block.BlockNexusController;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import thefloydman.moremystcraft.util.Reference;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class MoreMystcraftBlocks {
	public static final Block LOCKED_LECTERN = (Block) new BlockLockedLectern();
	public static final Block LOCKED_BOOKSTAND = (Block) new BlockLockedBookstand();
	public static final Block TRAFFIC_CONE = (Block) new BlockTrafficCone();
	public static final Block UNSTABLE_RECEPTACLE = (Block) new BlockUnstableBookReceptacle();
	public static final Block UNSTABLE_PORTAL = (Block) new BlockUnstablePortal();

	public static void init() {
		if (new MoreMystcraftConfig().getLockedLecternEnabled() == true) {
			LOCKED_LECTERN.setRegistryName(Reference.forMoreMystcraft("locked_lectern"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) LOCKED_LECTERN);
			registerMystcraftItemBlock(LOCKED_LECTERN);
		}
		if (new MoreMystcraftConfig().getLockedBookstandEnabled() == true) {
			LOCKED_BOOKSTAND.setRegistryName(Reference.forMoreMystcraft("locked_bookstand"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) LOCKED_BOOKSTAND);
			registerMystcraftItemBlock(LOCKED_BOOKSTAND);
		}

		if (new MoreMystcraftConfig().getUnstableReceptacleEnabled() == true) {

			UNSTABLE_RECEPTACLE.setRegistryName(Reference.forMoreMystcraft("unstable_receptacle"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) UNSTABLE_RECEPTACLE);
			registerMystcraftItemBlock(UNSTABLE_RECEPTACLE);

			UNSTABLE_PORTAL.setRegistryName(Reference.forMoreMystcraft("unstable_portal"));
			ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) UNSTABLE_PORTAL);
			registerMystcraftItemBlock(UNSTABLE_PORTAL);
		}
	}

	@SubscribeEvent
	public static void registerBlock(RegistryEvent.Register<Block> event) {
		if (new MoreMystcraftConfig().getTrafficConeEnabled() == true) {
			event.getRegistry().registerAll(TRAFFIC_CONE);
		}
	}

	@SubscribeEvent
	public static void registerItemBlock(RegistryEvent.Register<Item> event) {
		if (new MoreMystcraftConfig().getTrafficConeEnabled() == true) {
			event.getRegistry()
					.registerAll(new ItemBlock(TRAFFIC_CONE).setRegistryName(TRAFFIC_CONE.getRegistryName()));
		}
	}

	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		if (new MoreMystcraftConfig().getTrafficConeEnabled() == true) {
			registerRender(Item.getItemFromBlock(TRAFFIC_CONE));
		}
	}

	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	private static void registerMystcraftItemBlock(final Block b) {
		final ItemBlock ib = new ItemBlock(b);
		ib.setRegistryName(b.getRegistryName());
		ModRegistryPrimer.queueForRegistration((IForgeRegistryEntry<?>) ib);
	}

	public static void registerMystcraftModels() {
		if (new MoreMystcraftConfig().getLockedLecternEnabled() == true) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(LOCKED_LECTERN), 0,
					mrlItemBlockModel("blocklectern"));
		}
		if (new MoreMystcraftConfig().getLockedBookstandEnabled() == true) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(LOCKED_BOOKSTAND), 0,
					mrlItemBlockModel("blockbookstand"));
		}
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(UNSTABLE_RECEPTACLE), 0,
				mrlItemBlockModelMore("unstable_receptacle"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(UNSTABLE_PORTAL), 0,
				mrlItemBlockModelMore("unstable_portal"));
	}

	private static ModelResourceLocation mrlItemBlockModel(final String name) {
		return new ModelResourceLocation(new ResourceLocation("mystcraft", name), "inventory");
	}

	private static ModelResourceLocation mrlItemBlockModelMore(final String name) {
		return new ModelResourceLocation(new ResourceLocation("moremystcraft", name), "inventory");
	}

	@SideOnly(Side.CLIENT)
	public static void registerModelColors() {
		final BlockColors colors = Minecraft.getMinecraft().getBlockColors();
		colors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			if (worldIn == null || pos == null) {
				return new Color(255, 0, 0).getRGB();
			}
			return BlockUnstablePortal.colorMultiplier(worldIn, pos);
		}, new Block[] { MoreMystcraftBlocks.UNSTABLE_PORTAL });
	}
}