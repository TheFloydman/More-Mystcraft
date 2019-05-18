/*
 * Special thanks to jabelar for this example:
 * https://github.com/jabelar/ExampleMod-1.12/blob/master/src/main/java/com/blogspot/jabelarminecraft/examplemod/WorldData.java
 */

package thefloydman.moremystcraft.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import thefloydman.moremystcraft.util.Reference;

public class MoreMystcraftWorldSavedData extends WorldSavedData {
	private static final String NAME = Reference.MOD_ID;
	private boolean conflictingOrePagesInstabilityAdded = false;
	private static MoreMystcraftWorldSavedData instance;

	public MoreMystcraftWorldSavedData() {
		super(NAME);
	}

	public MoreMystcraftWorldSavedData(String str) {
		super(str);
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		conflictingOrePagesInstabilityAdded = nbt.getBoolean("conflictingOrePagesInstabilityAdded");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("conflictingOrePagesInstabilityAdded", conflictingOrePagesInstabilityAdded);
		return nbt;
	}

	public boolean getAdded() {
		return conflictingOrePagesInstabilityAdded;
	}

	public void setAdded(boolean added) {
		conflictingOrePagesInstabilityAdded = added;
		markDirty();
	}

	public static MoreMystcraftWorldSavedData get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		instance = (MoreMystcraftWorldSavedData) storage.getOrLoadData(MoreMystcraftWorldSavedData.class, NAME);
		if (instance == null) {
			instance = new MoreMystcraftWorldSavedData();
			storage.setData(NAME, instance);
		}
		return instance;
	}

}