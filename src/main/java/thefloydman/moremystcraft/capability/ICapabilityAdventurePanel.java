package thefloydman.moremystcraft.capability;

import java.util.UUID;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;

public interface ICapabilityAdventurePanel {

	public void setPreviousGameMode(GameType type);

	public GameType getPreviousGameMode();
	
	public void setLinkedToAdventure(boolean bool);
	
	public boolean getLinkedToAdventure();
	
	public void setDeathDimension(int dim);
	
	public int getDeathDimension();

}