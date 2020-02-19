package thefloydman.moremystcraft.capability.uuid;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;

public class CapabilityUUID implements ICapabilityUUID {

	protected UUID uuid = null;

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	@Nullable
	public UUID getUUID() {
		return this.uuid;
	}

}
