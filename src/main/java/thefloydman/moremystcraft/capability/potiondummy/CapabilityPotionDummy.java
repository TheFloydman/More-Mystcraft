package thefloydman.moremystcraft.capability.potiondummy;

import java.util.UUID;

import thefloydman.moremystcraft.entity.EntityMaintainerSuit;

public class CapabilityPotionDummy implements ICapabilityPotionDummy {

	UUID uuid = UUID.randomUUID();

	@Override
	public void setParent(UUID uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public UUID getParent() {
		return this.uuid;
	}

}
