package thefloydman.moremystcraft.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CapabilityHub implements ICapabilityHub {

	protected List<UUID> idList = new ArrayList<UUID>();
	protected int timeLimit = 100;
	protected boolean perPlayer = false;
	protected UUID lastActivatedBy = null;

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

}
