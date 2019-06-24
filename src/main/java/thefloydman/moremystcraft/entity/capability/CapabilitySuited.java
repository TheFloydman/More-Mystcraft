package thefloydman.moremystcraft.entity.capability;

import java.util.UUID;

import thefloydman.moremystcraft.entity.EntityMaintainerSuit;

public class CapabilitySuited implements ISuited {

	boolean suited = false;

	@Override
	public void setSuited(boolean suited) {
		this.suited = suited;
	}

	@Override
	public boolean isSuited() {
		return this.suited;
	}

}
