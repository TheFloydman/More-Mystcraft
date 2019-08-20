package thefloydman.moremystcraft.symbol.modifiers;

import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.util.ColorGradient;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.api.world.logic.Modifier;

import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;

public class SymbolSunColor extends MoreMystcraftSymbolBase {

	public SymbolSunColor(ResourceLocation identifier) {
		super(identifier);
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	@Override
	public void registerLogic(AgeDirector controller, long seed) {
		ColorGradient sunColor = controller.popModifier("sun_color").asGradient();
		if (sunColor == null) {
			sunColor = new ColorGradient();
		}
		ColorGradient gradient = ModifierUtils.popGradient(controller);
		sunColor.appendGradient(gradient);
		controller.setModifier("sun_color", sunColor);
	}

}