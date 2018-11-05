/*
 * Thank you to Choonster for this code.
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

@Config(modid = Reference.MOD_ID, name = "moremystcraft/core")
@Config.LangKey("moremystcraft.config.title")
public class ModConfig {

	//public static CategoryBlocks blocks = new CategoryBlocks();
	//public static CategoryStudies abandonedStudies = new CategoryStudies();
	//public static CategoryRecipes recipes = new CategoryRecipes();

	//private static class CategoryBlocks {

		@Name("Locked lectern enabled")
		@RequiresMcRestart
		public static boolean lockedLecternEnabled = true;

		@Name("Locked bookstand enabled")
		@RequiresMcRestart
		public static boolean lockedBookstandEnabled = true;

		@Name("Traffic cone enabled")
		@RequiresMcRestart
		public static boolean trafficConeEnabled = true;

	//}

	//private static class CategoryStudies {

		@Name("Generate abandoned studies in overworld")
		public static boolean abandonedStudiesOverworldEnabled = true;

		@Name("Abandoned study generation frequency")
		@Comment("The frequency at which Abandoned Studies will spawn. The lower the number, the more frequent the spawns. 0 will spawn them in every valid location.")
		@RangeInt(min = 0)
		public static int studyFrequency = 1000;

		@Name("Abandoned study minimum y")
		@Comment("The lowest height at which thr Abandoned Study will spawn. Decrease if you have dimensions with a low ground level; increase to incrrease performance.")
		@RangeInt(min = 0, max = 255)
		public static int studyMinimumY = 64;

	//}

	//private static class CategoryRecipes {

		@Name("Mystcraft's book binder recipe enabled")
		@RequiresMcRestart
		public static boolean bookBinderRecipeEnabled = true;

		@Name("Locked lectern recipe enabled")
		@RequiresMcRestart
		public static boolean lockedLecternRecipeEnabled = true;

		@Name("Locked bookstand recipe enabled")
		@RequiresMcRestart
		public static boolean lockedBookstandRecipeEnabled = true;

		@Name("Traffic cone recipe enabled")
		@RequiresMcRestart
		public static boolean trafficConeRecipeEnabled = true;

	//}

	@Mod.EventBusSubscriber
	private static class EventHandler {

		// Inject the new values and save to the config file when the config has been
		// changed from the GUI.
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MOD_ID)) {
				ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}