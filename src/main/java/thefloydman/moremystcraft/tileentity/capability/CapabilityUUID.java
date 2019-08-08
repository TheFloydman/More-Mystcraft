package thefloydman.moremystcraft.tileentity.capability;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;

public class CapabilityUUID implements IUUIDCapability {

	protected UUID uuid = null;

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Nullable
	@Override
	public UUID getUUID() {
		return this.uuid;
	}

}
