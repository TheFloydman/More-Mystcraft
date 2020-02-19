package thefloydman.moremystcraft.capability.journeyhub;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

	public int getClothDimension(UUID uuid);

	public void setClothDimension(UUID uuid, int dim);

	public BlockPos getClothPos(UUID uuid);

	public void setClothPos(UUID uuid, BlockPos pos);

	public void updateClothInfo(World world);
	
	public void removeCloth(UUID uuid);
	
	public void setOwner(UUID uuid);
	
	public UUID getOwner();

}