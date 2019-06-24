package thefloydman.moremystcraft.entity.capability;

import java.util.UUID;

public interface IMaintainerSuit {

	public void setSuit(UUID uuid);

	public void clearSuit();

	public UUID getSuit();

}