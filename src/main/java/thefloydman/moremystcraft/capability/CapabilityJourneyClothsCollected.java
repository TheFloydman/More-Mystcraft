package thefloydman.moremystcraft.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CapabilityJourneyClothsCollected implements ICapabilityPlayerJourneyClothsCollected {

	protected List<UUID> clothList = new ArrayList<UUID>();

	@Override
	public boolean addCloth(UUID uuid) {
		if (clothIndex(uuid) < 0) {
			clothList.add(uuid);
			return true;
		}
		return false;
	}

	@Override
	public void removeCloth(UUID uuid) {
		while (clothIndex(uuid) >= 0) {
			clothList.remove(clothIndex(uuid));
		}
	}

	@Override
	public int clothIndex(UUID uuid) {
		return clothList.indexOf(uuid);
	}

	@Override
	public List<UUID> getActivatedCloths() {
		return this.clothList;
	}

}
