package thefloydman.moremystcraft.init;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.xcompwiz.mystcraft.api.word.WordData;
import com.xcompwiz.mystcraft.config.MystConfig;
import com.xcompwiz.mystcraft.data.ModRegistryPrimer;

import thefloydman.moremystcraft.block.BlockTrafficCone;
import thefloydman.moremystcraft.block.BlockUnstableBookReceptacle;
import thefloydman.moremystcraft.block.BlockUnstablePortal;
import thefloydman.moremystcraft.block.BlockLockedBookstand;
import thefloydman.moremystcraft.block.BlockLockedLectern;
import thefloydman.moremystcraft.block.BlockNexusController;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.item.ItemMaintainerSuit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import thefloydman.moremystcraft.util.Reference;
import thefloydman.whatloomsahead.item.ItemBannerPattern;

@EventBusSubscriber
public class MoreMystcraftItems {
	
	public static List<String> mystcraftWords = Arrays.asList(WordData.Balance, WordData.Believe, WordData.Celestial,
			WordData.Chain, WordData.Change, WordData.Chaos, WordData.Civilization, WordData.Constraint,
			WordData.Contradict, WordData.Control, WordData.Convey, WordData.Creativity, WordData.Cycle,
			WordData.Dependence, WordData.Discover, WordData.Dynamic, WordData.Elevate, WordData.Encourage,
			WordData.Energy, WordData.Entropy, WordData.Ethereal, WordData.Exist, WordData.Explore, WordData.Flow,
			WordData.Force, WordData.Form, WordData.Future, WordData.Growth, WordData.Harmony, WordData.Honor,
			WordData.Image, WordData.Infinite, WordData.Inhibit, WordData.Intelligence, WordData.Love, WordData.Machine,
			WordData.Merge, WordData.Momentum, WordData.Motion, WordData.Mutual, WordData.Nature, WordData.Nurture,
			WordData.Order, WordData.Possibility, WordData.Power, WordData.Question, WordData.Rebirth,
			WordData.Remember, WordData.Resilience, WordData.Resurrect, WordData.Sacrifice, WordData.Society,
			WordData.Spur, WordData.Static, WordData.Stimulate, WordData.Survival, WordData.Sustain, WordData.System,
			WordData.Terrain, WordData.Time, WordData.Tradition, WordData.Transform, WordData.Void, WordData.Weave,
			WordData.Wisdom);

	public static final Item MAINTAINER_SUIT = (Item) new ItemMaintainerSuit();
	public static final Item BANNER_PATTERN_MYST_0 = new ItemBannerPattern("0", Reference.MOD_ID, BannerPattern.);

	public static void registerItems(final RegistryEvent.Register<Item> event) {

		IForgeRegistry<Item> itemRegistry = event.getRegistry();
		itemRegistry.register(MAINTAINER_SUIT);

	}

	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		registerRender(MAINTAINER_SUIT);
	}

	private static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}