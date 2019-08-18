package thefloydman.moremystcraft.capability;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;

public class CapabilityPreviousGameMode implements ICapabilityPreviousGameMode {
	
	protected GameType gamemode = GameType.SURVIVAL;

	@Override
	public void setGameMode(GameType type) {
		this.gamemode = type;
	}

	@Override
	public GameType getGameMode() {
		return this.gamemode;
	}

}
