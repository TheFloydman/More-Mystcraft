package thefloydman.moremystcraft.research;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;

public class Knowledge {

	protected Object knowledge;

	public Knowledge() {
		this.knowledge = null;
	}

	public Knowledge(Biome biome) {
		this.knowledge = biome;
	}

	public Knowledge(IBlockState state) {
		this.knowledge = state;
	}

	public void set(Biome biome) {
		this.knowledge = biome;
	}

	public void set(IBlockState state) {
		this.knowledge = state;
	}

	public Biome asBiome() {
		if (this.knowledge != null) {
			if (this.knowledge instanceof Biome) {
				return (Biome) knowledge;
			}
		}
		return null;
	}

	public IBlockState asBlockState() {
		if (this.knowledge != null) {
			if (this.knowledge instanceof IBlockState) {
				return (IBlockState) knowledge;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		if (this.knowledge != null) {
			return this.knowledge.toString();
		}
		return "";
	}

}
