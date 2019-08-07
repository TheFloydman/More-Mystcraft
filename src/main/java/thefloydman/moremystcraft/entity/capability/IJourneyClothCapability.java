package thefloydman.moremystcraft.entity.capability;

import java.util.UUID;

import net.minecraft.util.math.BlockPos;

public interface IJourneyClothCapability {

	public void setUUID(UUID uuid);

	public UUID getUUID();

	public void setCurrentDimension(Integer dim);

	public Integer getCurrentDimension();

	public void setCurrentPos(BlockPos pos);

	public BlockPos getCurrentPos();

}