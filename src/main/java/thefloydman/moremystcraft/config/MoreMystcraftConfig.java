/*
 * Thank you to Choonster for help starting this code.
 * View the original example at
 * https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/acf537dad272a4a7148d8e2f124e1bdf2226f2a4/src/main/java/choonster/testmod3/config/ModConfig.java
 */

package thefloydman.moremystcraft.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.util.Reference;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
@Config(modid = Reference.MOD_ID, name = "moremystcraft/core")
public class MoreMystcraftConfig {

	@RequiresMcRestart
	public static CategoryBlocks blocks = new CategoryBlocks();

	public static CategoryStudies abandonedStudies = new CategoryStudies();

	@RequiresMcRestart
	public static CategoryRecipes recipes = new CategoryRecipes();

	public static class CategoryBlocks {

		@Name("Locked lectern enabled")
		public boolean lockedLecternEnabled = true;

		@Name("Locked bookstand enabled")
		public boolean lockedBookstandEnabled = true;

		@Name("Traffic cone enabled")
		public boolean trafficConeEnabled = true;

	}

	public static class CategoryStudies {

		@Name("Generate abandoned studies in overworld")
		public boolean abandonedStudiesOverworldEnabled = true;

		@Name("Abandoned study generation frequency")
		@Comment("The frequency at which Abandoned Studies will spawn. The lower the number, the more frequent the spawns. 0 will spawn them in every valid location.")
		@RangeInt(min = 0)
		public int studyFrequency = 1000;

		@Name("Abandoned study minimum y")
		@Comment("The lowest height at which thr Abandoned Study will spawn. Decrease if you have dimensions with a low ground level; increase to incrrease performance.")
		@RangeInt(min = 0, max = 255)
		public int studyMinimumY = 64;

	}

	public static class CategoryRecipes {

		@Name("Mystcraft's book binder recipe enabled")
		public boolean bookBinderRecipeEnabled = true;

		@Name("Locked lectern recipe enabled")
		public boolean lockedLecternRecipeEnabled = true;

		@Name("Locked bookstand recipe enabled")
		public boolean lockedBookstandRecipeEnabled = true;

		@Name("Traffic cone recipe enabled")
		public boolean trafficConeRecipeEnabled = true;

	}

	@Mod.EventBusSubscriber
	public static class EventHandler {

		// Inject the new values and save to the config file when the config has been
		// changed from the GUI.
		@SubscribeEvent
		public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MOD_ID)) {
				ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}