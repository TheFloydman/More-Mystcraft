package thefloydman.moremystcraft.capability.adventurepanel;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;

public class CapabilityAdventurePanel implements ICapabilityAdventurePanel {
	
	protected GameType gamemode;
	protected boolean linkedToAdventure = false;
	protected int deathDimension = 0;

	@Override
	public void setPreviousGameMode(GameType type) {
		this.gamemode = type;
	}

	@Override
	public GameType getPreviousGameMode() {
		return this.gamemode;
	}

	@Override
	public void setLinkedToAdventure(boolean bool) {
		this.linkedToAdventure = bool;
	}

	@Override
	public boolean getLinkedToAdventure() {
		return this.linkedToAdventure;
	}

	@Override
	public void setDeathDimension(int dim) {
		this.deathDimension = dim;
	}

	@Override
	public int getDeathDimension() {
		return this.deathDimension;
	}

}
