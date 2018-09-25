/*
 * Thank you to Choonster for this code.
 * View the original example at
 * https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/acf537dad272a4a7148d8e2f124e1bdf2226f2a4/src/main/java/choonster/testmod3/config/ModConfig.java
 */

package thefloydman.moremystcraft.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.util.Reference;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("moremystcraft.config.title")
public class ModConfig {

	@Config.Comment("The frequency at which Abandoned Studies will spawn. The lower the number, the more frequent the spawns. 0 will spawn them in every possible location. (Default: 1000)")
	public static int studyFrequency = 1000;
	
	@Config.Comment("Enable or disable the Locked Lectern. REQUIRES RESTART (Default: true)")
	public static boolean lockedLecternEnabled = true;
	
	@Config.Comment("Enable or disable the Locked Bookstand. REQUIRES RESTART (Default: true)")
	public static boolean lockedBookstandEnabled = true;
	
	@Config.Comment("Enable or disable the recipe for the Book Binder. REQUIRES RESTART (Default: true)")
	public static boolean bookBinderRecipeEnabled = true;

	@Mod.EventBusSubscriber
	private static class EventHandler {

		// Inject the new values and save to the config file when the config has been changed from the GUI.
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MOD_ID)) {
				ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}