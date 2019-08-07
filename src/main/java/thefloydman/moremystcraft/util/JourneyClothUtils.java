package thefloydman.moremystcraft.util;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import thefloydman.moremystcraft.data.MoreMystcraftSavedDataPerDimension;
import thefloydman.moremystcraft.data.MoreMystcraftSavedDataPerSave;

public class JourneyClothUtils {

	public static enum ClothType {
		HAND, SHELL, SPIRAL;
	}

	public static NBTTagCompound getJourneyClothInfo(MoreMystcraftSavedDataPerDimension instance) {
		return instance.getJourneyClothInfo();
	}

	public static void setJourneyClothInfo(MoreMystcraftSavedDataPerDimension instance, NBTTagCompound nbt) {
		instance.setJourneyClothInfo(nbt);
	}

	public static void addJourneyCloth(MoreMystcraftSavedDataPerDimension instance, ClothType type, BlockPos pos) {
		NBTTagCompound main = getJourneyClothInfo(instance);
		if (!main.hasKey(type.name().toLowerCase())) {
			main.setTag(type.name().toLowerCase(), new NBTTagList());
		}
		NBTTagList list = new NBTTagList();
		if (!main.getTag(type.name().toLowerCase()).hasNoTags()) {
			list = main.getTagList(type.name().toLowerCase(), 10);
		}
		while (indexOfBlockPos(list, pos) >= 0) {
			list.removeTag(indexOfBlockPos(list, pos));
		}
		NBTTagCompound subTag = new NBTTagCompound();
		subTag.setTag("pos", NBTUtil.createPosTag(pos));
		subTag.setTag("players", new NBTTagList());
		list.appendTag(subTag);
		main.setTag(type.name().toLowerCase(), list);
		setJourneyClothInfo(instance, main);
	}

	public static void removeJourneyCloth(MoreMystcraftSavedDataPerDimension instance, ClothType type, BlockPos pos) {
		NBTTagCompound main = getJourneyClothInfo(instance);
		if (!main.hasKey(type.name().toLowerCase())) {
			main.setTag(type.name().toLowerCase(), new NBTTagList());
		}
		NBTTagList list = new NBTTagList();
		if (!main.getTag(type.name().toLowerCase()).hasNoTags()) {
			list = main.getTagList(type.name().toLowerCase(), 10);
		}
		while (indexOfBlockPos(list, pos) >= 0) {
			list.removeTag(indexOfBlockPos(list, pos));
		}
		main.setTag(type.name().toLowerCase(), list);
		setJourneyClothInfo(instance, main);
	}

	public static void registerPlayerWithJourneyCloth(MoreMystcraftSavedDataPerDimension instance, ClothType type,
			BlockPos pos, EntityPlayer player) {
		NBTTagCompound main = getJourneyClothInfo(instance);
		NBTTagList list = main.getTagList(type.name().toLowerCase(), 10);
		int index = indexOfBlockPos(list, pos);
		NBTTagCompound subTag = index >= 0 ? (NBTTagCompound) list.get(index) : new NBTTagCompound();
		NBTTagList players = subTag.getTagList("players", 10);
		UUID uuid = player.getUniqueID();
		for (int i = 0; i < players.tagCount(); i++) {
			if (NBTUtil.getUUIDFromTag(players.getCompoundTagAt(i)).equals(uuid)) {
				return;
			}
		}
		players.appendTag(NBTUtil.createUUIDTag(uuid));
		subTag.setTag("players", players);
		list.removeTag(index);
		list.appendTag(subTag);
		main.setTag(type.name().toLowerCase(), list);
		setJourneyClothInfo(instance, main);
	}

	public static void unregisterPlayerWithJourneyCloth(MoreMystcraftSavedDataPerDimension instance, ClothType type,
			BlockPos pos, EntityPlayer player) {
		NBTTagCompound main = getJourneyClothInfo(instance);
		NBTTagList list = main.getTagList(type.name().toLowerCase(), 10);
		int index = indexOfBlockPos(list, pos);
		NBTTagCompound subTag = (NBTTagCompound) list.get(index);
		NBTTagList players = subTag.getTagList("players", 10);
		UUID uuid = player.getUniqueID();
		for (int i = 0; i < players.tagCount(); i++) {
			if (NBTUtil.getUUIDFromTag(players.getCompoundTagAt(i)).equals(uuid)) {
				players.removeTag(i);
			}
		}
		subTag.setTag("players", players);
		list.removeTag(index);
		list.appendTag(subTag);
		main.setTag(type.name().toLowerCase(), list);
		setJourneyClothInfo(instance, main);
	}

	protected static int indexOfBlockPos(NBTTagList list, BlockPos pos) {
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) list.getCompoundTagAt(i);
			if (tag.hasKey("pos")) {
				if (NBTUtil.getPosFromTag((NBTTagCompound) tag.getTag("pos")).equals(pos)) {
					return i;
				}
			}
		}
		return -1;
	}

}
