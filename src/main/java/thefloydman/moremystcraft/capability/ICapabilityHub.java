package thefloydman.moremystcraft.capability;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

public interface ICapabilityHub {

	public void addUUID(UUID uuid);

	@Nullable
	public UUID getUUID(int index);

	public List<UUID> getUUIDs();

}