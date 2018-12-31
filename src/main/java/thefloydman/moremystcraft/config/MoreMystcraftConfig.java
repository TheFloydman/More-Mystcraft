/*
 * Thank you to Choonster for help starting this code.
 * View the original example at
 * https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/acf537dad272a4a7148d8e2f124e1bdf2226f2a4/src/main/java/choonster/testmod3/config/ModConfig.java
 */

package thefloydman.moremystcraft.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.minecraft.client.Minecraft;
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

@Config(modid = Reference.MOD_ID)
public class MoreMystcraftConfig {

	@Name("locked_blocks")
	public static CategoryLocked catLockedBlocks = new CategoryLocked();

	@Name("unstable_receptacle")
	public static CategoryUnstable catUnstable = new CategoryUnstable();

	@Name("misc_blocks")
	public static CategoryMisc catMisc = new CategoryMisc();

	@Name("abandoned_studies")
	public static CategoryStudies catStudies = new CategoryStudies();

	@Name("other")
	public static CategoryOther catOther = new CategoryOther();

	private static class CategoryLocked {

		@RequiresMcRestart
		@Name("Locked lectern enabled")
		public boolean lockedLecternEnabled = true;

		@RequiresMcRestart
		@Name("Locked bookstand enabled")
		public boolean lockedBookstandEnabled = true;

		@RequiresMcRestart
		@Name("Locked lectern recipe enabled")
		public boolean lockedLecternRecipeEnabled = true;

		@RequiresMcRestart
		@Name("Locked bookstand recipe enabled")
		public boolean lockedBookstandRecipeEnabled = true;

	}

	private static class CategoryUnstable {

		@RequiresMcRestart
		@Name("Unstable receptacle enabled")
		public boolean unstableReceptacleEnabled = true;

		@RequiresMcRestart
		@Name("Unstable receptacle recipe enabled")
		public boolean unstableReceptacleRecipeEnabled = false;

		@Name("Unstable receptacle linking book attempts")
		@Comment("How many additional times the unstable receptacle should try to find ground to spawn on.")
		@RangeInt(min = 0)
		public int unstableLinkingAttempts = 1000;

	}

	private static class CategoryMisc {

		@RequiresMcRestart
		@Name("Traffic cone enabled")
		public boolean trafficConeEnabled = true;

		@RequiresMcRestart
		@Name("Traffic cone recipe enabled")
		public boolean trafficConeRecipeEnabled = true;

	}

	private static class CategoryStudies {

		@Name("Generate abandoned studies in overworld")
		public boolean abandonedStudiesOverworldEnabled = true;

		@Name("Abandoned study generation frequency")
		@Comment("The frequency at which Abandoned Studies will spawn. The lower the number, the more frequent the spawns. 0 will spawn them in every valid location.")
		@RangeInt(min = 0)
		public int studyFrequency = 1000;

		@Name("Abandoned study minimum y")
		@Comment("The lowest height at which the Abandoned Study will spawn. Decrease if you have dimensions with a low ground level; increase to incrrease performance.")
		@RangeInt(min = 0, max = 255)
		public int studyMinimumY = 64;

	}

	private static class CategoryOther {

		@RequiresMcRestart
		@Name("Mystcraft's book binder recipe enabled")
		public boolean bookBinderRecipeEnabled = true;

	}

	/*
	 * Getters and setters for config options.
	 */

	public boolean getBookBinderRecipeEnabled() {
		return catOther.bookBinderRecipeEnabled;
	}

	public void setBookBinderRecipeEnabled(boolean var) {
		catOther.bookBinderRecipeEnabled = var;
	}

	public int getStudyMinimumY() {
		return catStudies.studyMinimumY;
	}

	public void setStudyMinimumY(int var) {
		catStudies.studyFrequency = var;
	}

	public int getStudyFrequency() {
		return catStudies.studyFrequency;
	}

	public boolean getStudiesEnabled() {
		return catStudies.abandonedStudiesOverworldEnabled;
	}

	public boolean getTrafficConeEnabled() {
		return catMisc.trafficConeEnabled;
	}

	public boolean getTrafficConeRecipeEnabled() {
		return catMisc.trafficConeRecipeEnabled;
	}

	public boolean getUnstableReceptacleEnabled() {
		return catUnstable.unstableReceptacleEnabled;
	}

	public boolean getUnstableReceptacleRecipeEnabled() {
		return catUnstable.unstableReceptacleRecipeEnabled;
	}

	public int getSpawnAttempts() {
		return catUnstable.unstableLinkingAttempts;
	}

	public boolean getLockedLecternEnabled() {
		return catLockedBlocks.lockedLecternEnabled;
	}

	public boolean getLockedBookstandEnabled() {
		return catLockedBlocks.lockedBookstandEnabled;
	}

	public boolean getLockedLecternRecipeEnabled() {
		return catLockedBlocks.lockedLecternRecipeEnabled;
	}

	public boolean getLockedBookstandRecipeEnabled() {
		return catLockedBlocks.lockedBookstandRecipeEnabled;
	}

	@Mod.EventBusSubscriber
	public static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MOD_ID)) {
				ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}