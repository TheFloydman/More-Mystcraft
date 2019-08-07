/*
 * Special thanks to jabelar for this example:
 * https://github.com/jabelar/ExampleMod-1.12/blob/master/src/main/java/com/blogspot/jabelarminecraft/examplemod/WorldData.java
 */

package thefloydman.moremystcraft.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import thefloydman.moremystcraft.util.JourneyClothUtils.ClothType;
import thefloydman.moremystcraft.util.Reference;

public class MoreMystcraftSavedDataPerSave extends WorldSavedData {
	private static final String NAME = Reference.MOD_ID;
	private NBTTagCompound journeyClothInfo = new NBTTagCompound();
	private static MoreMystcraftSavedDataPerSave instance;

	public MoreMystcraftSavedDataPerSave() {
		super(NAME);
		setupClothCompounds();
		markDirty();
	}

	public MoreMystcraftSavedDataPerSave(String str) {
		super(str);
		setupClothCompounds();
		markDirty();
	}

	protected void setupClothCompounds() {
		for (ClothType type : ClothType.values()) {
			journeyClothInfo.setTag(type.name().toLowerCase(), new NBTTagList());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		journeyClothInfo = nbt.getCompoundTag("journeyClothInfo");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("journeyClothInfo", journeyClothInfo);
		return nbt;
	}

	public static MoreMystcraftSavedDataPerSave get(World world) {
		MapStorage storage = world.getMapStorage();
		instance = (MoreMystcraftSavedDataPerSave) storage.getOrLoadData(MoreMystcraftSavedDataPerSave.class,
				NAME);
		if (instance == null) {
			instance = new MoreMystcraftSavedDataPerSave();
			storage.setData(NAME, instance);
		}
		return instance;
	}

	public NBTTagCompound getJourneyClothInfo() {
		return journeyClothInfo;
	}

	public void setJourneyClothInfo(NBTTagCompound nbt) {
		journeyClothInfo = nbt;
		markDirty();
	}

}