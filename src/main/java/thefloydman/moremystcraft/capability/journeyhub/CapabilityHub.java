package thefloydman.moremystcraft.capability.journeyhub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thefloydman.moremystcraft.data.worldsaveddata.MoreMystcraftSavedDataPerSave;

public class CapabilityHub implements ICapabilityHub {

	protected List<UUID> idList = new ArrayList<UUID>();
	protected int timeLimit = 100;
	protected boolean perPlayer = false;
	protected UUID lastActivatedBy = null;
	Map<UUID, Integer> clothDimensions = new HashMap<UUID, Integer>();
	Map<UUID, BlockPos> clothPositions = new HashMap<UUID, BlockPos>();
	protected UUID owner = UUID.randomUUID();

	public void addCloth(UUID uuid, int dim, BlockPos pos) {
		this.addUUID(uuid);
		this.setClothDimension(uuid, dim);
		this.setClothPos(uuid, pos);
	}

	public void removeCloth(UUID uuid) {
		while (idList.contains(uuid)) {
			idList.remove(uuid);
		}
		while (clothDimensions.containsKey(uuid)) {
			clothDimensions.remove(uuid);
		}
		while (clothPositions.containsKey(uuid)) {
			clothPositions.remove(uuid);
		}
	}

	public void clearAllCloths() {
		idList = new ArrayList<UUID>();
		clothDimensions = new HashMap<UUID, Integer>();
		clothPositions = new HashMap<UUID, BlockPos>();
	}

	@Override
	public void addUUID(UUID uuid) {
		if (!idList.contains(uuid)) {
			idList.add(uuid);
		}
	}

	@Override
	public UUID getUUID(int index) {
		if (index >= idList.size()) {
			return null;
		}
		return idList.get(index);
	}

	@Override
	public List<UUID> getUUIDs() {
		return idList;
	}

	@Override
	public void clearUUIDs() {
		idList = new ArrayList<UUID>();
	}

	@Override
	public void setTimeLimit(int ticks) {
		timeLimit = ticks;
	}

	@Override
	public int getTimeLimit() {
		return timeLimit;
	}

	@Override
	public void setPerPlayer(boolean bool) {
		perPlayer = bool;
	}

	@Override
	public boolean getPerPlayer() {
		return perPlayer;
	}

	@Override
	public UUID getLastActivatedBy() {
		return this.lastActivatedBy;
	}

	@Override
	public void setLastActivatedBy(UUID uuid) {
		this.lastActivatedBy = uuid;
	}

	@Override
	public int getClothDimension(UUID uuid) {
		if (this.clothDimensions.containsKey(uuid)) {
			return this.clothDimensions.get(uuid);
		}
		return 0;
	}

	@Override
	public void setClothDimension(UUID uuid, int dim) {
		this.clothDimensions.put(uuid, dim);
	}

	@Override
	public BlockPos getClothPos(UUID uuid) {
		if (this.clothPositions.containsKey(uuid)) {
			return this.clothPositions.get(uuid);
		}
		return new BlockPos(0, 0, 0);
	}

	@Override
	public void setClothPos(UUID uuid, BlockPos pos) {
		this.clothPositions.put(uuid, pos);
	}

	@Override
	public void updateClothInfo(World world) {
		MoreMystcraftSavedDataPerSave data = MoreMystcraftSavedDataPerSave.get(world);
		NBTTagList clothList = data.getAllJourneyClothInfo();
		for (NBTBase base : clothList) {
			NBTTagCompound cmp = (NBTTagCompound) base;
			if (cmp.hasKey("uuid")) {
				UUID uuid = NBTUtil.getUUIDFromTag(cmp.getCompoundTag("uuid"));
				if (this.idList.contains(uuid)) {
					if (cmp.hasKey("dim")) {
						setClothDimension(uuid, cmp.getInteger("dim"));
					}
					if (cmp.hasKey("pos")) {
						setClothPos(uuid, NBTUtil.getPosFromTag(cmp.getCompoundTag("pos")));
					}
				}
			}
		}
	}

	@Override
	public void setOwner(UUID uuid) {
		this.owner = uuid;
	}

	@Override
	public UUID getOwner() {
		return this.owner;
	}

}
