package thefloydman.moremystcraft.symbol.behavior;

import com.google.gson.JsonObject;
import com.xcompwiz.mystcraft.api.world.AgeDirector;

import mystlibrary.exception.SymbolBuildException;
import mystlibrary.symbol.SymbolMetadata;
import mystlibrary.symbol.behavior.IAgeSymbolBehavior;
import mystlibrary.symbol.util.SymbolJsonParser;
import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.util.Reference;

public class SizeBehavior implements IAgeSymbolBehavior {

	public static final ResourceLocation NAME = Reference.forMoreMystcraft("size");
	private final float size;
	private static final String FIELD_NAME_VALUE = "value";

	public static IAgeSymbolBehavior fromJson(JsonObject json, SymbolMetadata metadata) throws SymbolBuildException {
		float size = SymbolJsonParser.parseFloat(json, FIELD_NAME_VALUE, metadata);
		SizeBehavior behavior = new SizeBehavior(size);
		return behavior;
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty(IAgeSymbolBehavior.BEHAVIOR_ID, NAME.toString());
		json.addProperty(FIELD_NAME_VALUE, this.size);
		return json;
	}

	public SizeBehavior(float size) {
		this.size = size;
	}

	@Override
	public void registerLogic(AgeDirector controller, long seed) {

		float value = this.size;
		final Number prev = controller.popModifier("size").asNumber();
		if (prev != null) {
			value = combineFloats(prev.floatValue(), value);
		}
		controller.setModifier("size", value);
	}

	private float combineFloats(float a, float b) {
		return ((a + 1) / 2) * b;
	}

}
