/*
 * Special thanks to jabelar for this example:
 * https://github.com/jabelar/ExampleMod-1.12/blob/master/src/main/java/com/blogspot/jabelarminecraft/examplemod/WorldData.java
 */

package thefloydman.moremystcraft.data.worldsaveddata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import thefloydman.moremystcraft.util.JourneyUtils.PatternType;
import thefloydman.moremystcraft.util.Reference;

public class MoreMystcraftSavedDataPerDimension extends WorldSavedData {
	private static final String NAME = Reference.MOD_ID;
	private boolean conflictingOrePagesInstabilityAdded = false;
	private NBTTagList potionEffects = new NBTTagList();
	private NBTTagCompound journeyClothInfo = new NBTTagCompound();
	private static MoreMystcraftSavedDataPerDimension instance;

	public MoreMystcraftSavedDataPerDimension() {
		super(NAME);
		setupClothCompounds();
		markDirty();
	}

	public MoreMystcraftSavedDataPerDimension(String str) {
		super(str);
		setupClothCompounds();
		markDirty();
	}

	protected void setupClothCompounds() {
		for (PatternType type : PatternType.values()) {
			journeyClothInfo.setTag(type.name().toLowerCase(), new NBTTagList());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		conflictingOrePagesInstabilityAdded = nbt.getBoolean("conflictingOrePagesInstabilityAdded");
		potionEffects = nbt.getTagList("potionEffects", 8);
		journeyClothInfo = nbt.getCompoundTag("journeyClothInfo");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("conflictingOrePagesInstabilityAdded", conflictingOrePagesInstabilityAdded);
		nbt.setTag("potionEffects", potionEffects);
		nbt.setTag("journeyClothInfo", journeyClothInfo);
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

	public NBTTagCompound getJourneyClothInfo() {
		return journeyClothInfo;
	}

	public void setJourneyClothInfo(NBTTagCompound nbt) {
		journeyClothInfo = nbt;
		markDirty();
	}

}