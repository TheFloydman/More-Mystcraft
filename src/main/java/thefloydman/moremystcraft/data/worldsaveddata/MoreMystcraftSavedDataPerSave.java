/*
 * Special thanks to jabelar for this example:
 * https://github.com/jabelar/ExampleMod-1.12/blob/master/src/main/java/com/blogspot/jabelarminecraft/examplemod/WorldData.java
 */

package thefloydman.moremystcraft.data.worldsaveddata;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import thefloydman.moremystcraft.util.Reference;

public class MoreMystcraftSavedDataPerSave extends WorldSavedData {
	private static final String NAME = Reference.MOD_ID;
	private NBTTagList allJourneyClothInfo = new NBTTagList();
	private static MoreMystcraftSavedDataPerSave instance;

	public MoreMystcraftSavedDataPerSave() {
		super(NAME);
	}

	public MoreMystcraftSavedDataPerSave(String str) {
		super(str);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		allJourneyClothInfo = nbt.getTagList("journeyClothInfo", 10);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("journeyClothInfo", allJourneyClothInfo);
		return nbt;
	}

	public static MoreMystcraftSavedDataPerSave get(World world) {
		MapStorage storage = world.getMapStorage();
		instance = (MoreMystcraftSavedDataPerSave) storage.getOrLoadData(MoreMystcraftSavedDataPerSave.class, NAME);
		if (instance == null) {
			instance = new MoreMystcraftSavedDataPerSave();
			storage.setData(NAME, instance);
		}
		return instance;
	}

	public NBTTagList getAllJourneyClothInfo() {
		return allJourneyClothInfo;
	}

	public void setAllJourneyClothInfo(NBTTagList nbt) {
		allJourneyClothInfo = nbt;
		markDirty();
	}

	public NBTTagCompound getSingleJourneyClothData(UUID uuidIn) {
		NBTTagList main = this.getAllJourneyClothInfo();
		for (NBTBase base : main) {
			UUID uuidSaved = NBTUtil.getUUIDFromTag(((NBTTagCompound) base).getCompoundTag("uuid"));
			if (uuidIn.equals(uuidSaved)) {
				return (NBTTagCompound) base;
			}
		}
		return null;
	}

	public int indexOfCloth(UUID uuidIn) {
		NBTTagList main = this.getAllJourneyClothInfo();
		for (int i = 0; i < main.tagCount(); i++) {
			UUID uuidSaved = NBTUtil.getUUIDFromTag((main.getCompoundTagAt(i)).getCompoundTag("uuid"));
			if (uuidIn.equals(uuidSaved)) {
				return i;
			}
		}
		return -1;
	}

	public void addJourneyCloth(UUID clothUUID) {
		if (getSingleJourneyClothData(clothUUID) != null) {
			return;
		}
		NBTTagList main = this.getAllJourneyClothInfo();
		NBTTagCompound cloth = new NBTTagCompound();
		cloth.setTag("uuid", NBTUtil.createUUIDTag(clothUUID));
		main.appendTag(cloth);
		setAllJourneyClothInfo(main);
	}

	public void activateJourneyCloth(UUID clothUUID, UUID playerUUID) {
		if (!journeyClothActivatedByPlayer(clothUUID, playerUUID)) {
			if (getSingleJourneyClothData(clothUUID) == null) {
				addJourneyCloth(clothUUID);
			}
			NBTTagList main = this.getAllJourneyClothInfo();
			NBTTagCompound cloth = getSingleJourneyClothData(clothUUID);
			NBTTagList players = cloth.getTagList("players", 10);
			players.appendTag(NBTUtil.createUUIDTag(playerUUID));
			cloth.setTag("players", players);
			int index = indexOfCloth(clothUUID);
			main.set(index, cloth);
			setAllJourneyClothInfo(main);
		}
	}

	public void deactivateJourneyCloth(UUID clothUUID, UUID playerUUID) {
		if (journeyClothActivatedByPlayer(clothUUID, playerUUID)) {
			if (getSingleJourneyClothData(clothUUID) != null) {
				NBTTagList main = this.getAllJourneyClothInfo();
				NBTTagCompound cloth = getSingleJourneyClothData(clothUUID);
				NBTTagList players = cloth.getTagList("players", 10);
				for (int i = 0; i < players.tagCount(); i++) {
					if (playerUUID.equals(NBTUtil.getUUIDFromTag(players.getCompoundTagAt(i)))) {
						players.removeTag(i);
					}
				}
				cloth.setTag("players", players);
				int index = indexOfCloth(clothUUID);
				main.set(index, cloth);
				setAllJourneyClothInfo(main);
			}
		}
	}

	public boolean journeyClothActivatedByPlayer(UUID clothUUID, UUID playerUUID) {
		NBTTagCompound cloth = getSingleJourneyClothData(clothUUID);
		if (cloth != null) {
			NBTTagList players = cloth.getTagList("players", 10);
			for (NBTBase base : players) {
				UUID savedUUID = NBTUtil.getUUIDFromTag(((NBTTagCompound) base));
				if (playerUUID.equals(savedUUID)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean journeyClothActivatedByAnyone(UUID clothUUID) {
		NBTTagCompound cloth = getSingleJourneyClothData(clothUUID);
		if (cloth == null) {
			return false;
		}
		if (!cloth.hasKey("players")) {
			return false;
		}
		NBTTagList players = cloth.getTagList("players", 10);
		if (players.hasNoTags()) {
			return false;
		}
		return true;
	}

}