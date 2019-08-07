package thefloydman.moremystcraft.entity.capability;

import java.util.UUID;

import net.minecraft.util.math.BlockPos;

public class CapabilityJourneyCloth implements IJourneyClothCapability {

	protected UUID uuid = null;
	protected Integer dimension = null;
	protected BlockPos blockPos = null;

	@Override
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public UUID getUUID() {
		return this.uuid;
	}

	@Override
	public void setCurrentDimension(Integer dim) {
		this.dimension = dim;
	}

	@Override
	public Integer getCurrentDimension() {
		return this.dimension;
	}

	@Override
	public void setCurrentPos(BlockPos pos) {
		this.blockPos = pos;
	}

	@Override
	public BlockPos getCurrentPos() {
		return this.blockPos;
	}

}
