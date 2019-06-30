/*
 * Special thanks to jabelar for this example:
 * https://github.com/jabelar/ExampleMod-1.12/blob/master/src/main/java/com/blogspot/jabelarminecraft/examplemod/WorldData.java
 */

package thefloydman.moremystcraft.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import thefloydman.moremystcraft.util.Reference;

public class MoreMystcraftSavedDataPerDimension extends WorldSavedData {
	private static final String NAME = Reference.MOD_ID;
	private boolean conflictingOrePagesInstabilityAdded = false;
	private NBTTagList potionEffects = new NBTTagList();
	private static MoreMystcraftSavedDataPerDimension instance;

	public MoreMystcraftSavedDataPerDimension() {
		super(NAME);
	}

	public MoreMystcraftSavedDataPerDimension(String str) {
		super(str);
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		conflictingOrePagesInstabilityAdded = nbt.getBoolean("conflictingOrePagesInstabilityAdded");
		potionEffects = nbt.getTagList("potionEffects", 8);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("conflictingOrePagesInstabilityAdded", conflictingOrePagesInstabilityAdded);
		nbt.setTag("potionEffects", potionEffects);
		return nbt;
	}

	public static MoreMystcraftSavedDataPerDimension get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		instance = (MoreMystcraftSavedDataPerDimension) storage.getOrLoadData(MoreMystcraftSavedDataPerDimension.class,
				NAME);
		if (instance == null) {
			instance = new MoreMystcraftSavedDataPerDimension();
			storage.setData(NAME, instance);
		}
		return instance;
	}

	public boolean getConflictingOreInstabilityAdded() {
		return conflictingOrePagesInstabilityAdded;
	}

	public void setConflictingOreInstabilityAdded(boolean added) {
		conflictingOrePagesInstabilityAdded = added;
		markDirty();
	}

	public NBTTagList getPotionEffects() {
		return potionEffects;
	}

	public void setPotionEffects(NBTTagList nbt) {
		potionEffects = nbt;
		markDirty();
	}

}