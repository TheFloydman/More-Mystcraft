package thefloydman.moremystcraft.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CapabilityHub implements ICapabilityHub {

	protected List<UUID> idList = new ArrayList<UUID>();

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

}
