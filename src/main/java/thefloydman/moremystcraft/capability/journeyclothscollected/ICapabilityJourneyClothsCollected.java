package thefloydman.moremystcraft.capability.journeyclothscollected;

import java.util.List;
import java.util.UUID;

public interface ICapabilityJourneyClothsCollected {

	public boolean addCloth(UUID uuid);

	public void removeCloth(UUID uuid);

	public int clothIndex(UUID uuid);

	public List<UUID> getActivatedCloths();
	
	public void setActivatedCloths(List<UUID> list);

}