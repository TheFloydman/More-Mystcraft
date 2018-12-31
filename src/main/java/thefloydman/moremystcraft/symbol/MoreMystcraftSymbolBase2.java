package thefloydman.moremystcraft.symbol;

import com.xcompwiz.mystcraft.api.impl.InternalAPI;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.grammar.GrammarGenerator;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MoreMystcraftSymbolBase2 implements IAgeSymbol {

	public MoreMystcraftSymbolBase2(ResourceLocation registryName) {
		
	}
	
	public void registerLogic(AgeDirector controller, long seed) {
		
	}
	
	public int instabilityModifier(int count) {
		return 0;
	}
	
	public boolean generatesConfigOption() {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public String getLocalizedName() {
		return null;
	}
	
	public String[] getPoem() {
		return null;
	}

	public String getUnlocalizedName() {
		return "moremystcraft.symbol." + getRegistryName().getResourcePath();
	}
}