package thefloydman.moremystcraft.capability;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

public interface ICapabilityHub {

	public void addUUID(UUID uuid);

	@Nullable
	public UUID getUUID(int index);

	public List<UUID> getUUIDs();
	
	public void clearUUIDs();
	
	public void setTimeLimit(int ticks);
	
	public int getTimeLimit();
	
	public void setPerPlayer(boolean bool);
	
	public boolean getPerPlayer();
	
	public UUID getLastActivatedBy();
	
	public void setLastActivatedBy(UUID uuid);

}