package thefloydman.moremystcraft.symbol.behavior;

import com.google.gson.JsonObject;
import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.world.AgeDirector;

import mystlibrary.exception.SymbolBuildException;
import mystlibrary.symbol.SymbolMetadata;
import mystlibrary.symbol.behavior.IAgeSymbolBehavior;
import mystlibrary.symbol.util.SymbolJsonParser;
import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.util.Reference;

public class TiltBehavior implements IAgeSymbolBehavior {

	public static final ResourceLocation NAME = Reference.forMoreMystcraft("tilt");
	private final float tilt;
	private static final String FIELD_NAME_VALUE = "value";

	public static IAgeSymbolBehavior fromJson(JsonObject json, SymbolMetadata metadata) throws SymbolBuildException {
		float tilt = SymbolJsonParser.parseFloat(json, FIELD_NAME_VALUE, metadata);
		TiltBehavior behavior = new TiltBehavior(tilt);
		return behavior;
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty(IAgeSymbolBehavior.BEHAVIOR_ID, NAME.toString());
		json.addProperty(FIELD_NAME_VALUE, this.tilt);
		return json;
	}

	public TiltBehavior(float tilt) {
		this.tilt = tilt;
	}

	@Override
	public void registerLogic(AgeDirector controller, long seed) {

		Float value = this.tilt;
		final Number prev = controller.popModifier("tilt").asNumber();
		if (prev != null) {
			value = ModifierUtils.averageAngles(prev.floatValue(), value) % 360.0F;
		}
		controller.setModifier("tilt", value);
	}

}
