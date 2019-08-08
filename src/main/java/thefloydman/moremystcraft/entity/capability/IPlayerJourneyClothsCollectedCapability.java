package thefloydman.moremystcraft.entity.capability;

import java.util.List;
import java.util.UUID;

public interface IPlayerJourneyClothsCollectedCapability {

	public boolean addCloth(UUID uuid);

	public void removeCloth(UUID uuid);

	public int clothIndex(UUID uuid);

	public List<UUID> getActivatedCloths();

}