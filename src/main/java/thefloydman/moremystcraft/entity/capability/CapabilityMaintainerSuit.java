package thefloydman.moremystcraft.entity.capability;

import java.util.UUID;

import thefloydman.moremystcraft.entity.EntityMaintainerSuit;

public class CapabilityMaintainerSuit implements IMaintainerSuit {

	UUID uuid = UUID.randomUUID();

	@Override
	public void setSuit(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void clearSuit() {
		this.uuid = UUID.randomUUID();
	}

	@Override
	public UUID getSuit() {
		return this.uuid;
	}

}
