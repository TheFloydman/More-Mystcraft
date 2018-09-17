package thefloydman.moremystcraft.symbol;

import com.xcompwiz.mystcraft.api.impl.InternalAPI;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.api.world.AgeDirector;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class SymbolBase implements IAgeSymbol {

	protected final ResourceLocation registryName;
	private String[] words;
	@SideOnly(Side.CLIENT)
	private String localizedName;

	public SymbolBase(ResourceLocation registryName) {
		this.registryName = registryName;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return this.registryName;
	}

	@Override
	public abstract void registerLogic(AgeDirector controller, long seed);

	@Override
	public int instabilityModifier(int count) {
		return 0;
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getLocalizedName() {
		if (this.localizedName == null) {
			this.localizedName = generateLocalizedName();
		}
		return this.localizedName;
	}

	private String generateLocalizedName() {
		return I18n.format(getUnlocalizedName() + ".name", new Object[0]);
	}

	private String getUnlocalizedName() {
		return "moremystcraft.symbol." + getRegistryName().getResourcePath();
	}

	@Override
	public String[] getPoem() {
		return this.words;
	}

	public void setWords(String[] words) {
		this.words = words;
	}

	public IAgeSymbol setCardRank(Integer cardrank) {
		if (cardrank == null) {
			return this;
		}
		InternalAPI.symbolValues.setSymbolCardRank(this, cardrank.intValue());
		return this;
	}
}
