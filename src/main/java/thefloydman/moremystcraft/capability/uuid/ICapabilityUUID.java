package thefloydman.moremystcraft.capability.uuid;

import java.util.UUID;

import net.minecraft.util.math.BlockPos;

public interface ICapabilityUUID {

	public void setUUID(UUID uuid);

	public UUID getUUID();

}