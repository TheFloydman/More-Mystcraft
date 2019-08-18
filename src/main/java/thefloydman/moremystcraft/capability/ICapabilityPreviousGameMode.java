package thefloydman.moremystcraft.capability;

import java.util.UUID;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;

public interface ICapabilityPreviousGameMode {

	public void setGameMode(GameType type);

	public GameType getGameMode();

}