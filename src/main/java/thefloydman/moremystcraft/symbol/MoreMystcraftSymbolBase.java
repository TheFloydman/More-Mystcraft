package thefloydman.moremystcraft.symbol;

import com.xcompwiz.mystcraft.api.impl.InternalAPI;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.grammar.GrammarGenerator;
import com.xcompwiz.mystcraft.symbol.SymbolBase;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MoreMystcraftSymbolBase extends SymbolBase {

	public MoreMystcraftSymbolBase(ResourceLocation registryName) {
		super(registryName);
	}

	@Override
	public String getUnlocalizedName() {
		return "moremystcraft.symbol." + getRegistryName().getResourcePath();
	}
}