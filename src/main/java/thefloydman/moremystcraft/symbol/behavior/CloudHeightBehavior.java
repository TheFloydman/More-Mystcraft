package thefloydman.moremystcraft.symbol.behavior;

import com.google.gson.JsonObject;
import com.xcompwiz.mystcraft.api.world.AgeDirector;

import mystlibrary.exception.SymbolBuildException;
import mystlibrary.symbol.SymbolMetadata;
import mystlibrary.symbol.behavior.IAgeSymbolBehavior;
import mystlibrary.symbol.util.SymbolJsonParser;
import net.minecraft.util.ResourceLocation;
import thefloydman.moremystcraft.util.Reference;

public class CloudHeightBehavior implements IAgeSymbolBehavior {

	public static final ResourceLocation NAME = Reference.forMoreMystcraft("cloud_height");
	private final float height;
	private static final String FIELD_NAME_VALUE = "value";

	public static IAgeSymbolBehavior fromJson(JsonObject json, SymbolMetadata metadata) throws SymbolBuildException {
		float height = SymbolJsonParser.parseFloat(json, FIELD_NAME_VALUE, metadata);
		CloudHeightBehavior behavior = new CloudHeightBehavior(height);
		return behavior;
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty(IAgeSymbolBehavior.BEHAVIOR_ID, NAME.toString());
		json.addProperty(FIELD_NAME_VALUE, this.height);
		return json;
	}

	public CloudHeightBehavior(float height) {
		this.height = height;
	}

	@Override
	public void registerLogic(AgeDirector controller, long seed) {
		Float value = this.height;
		final Number prev = controller.popModifier("cloud_height").asNumber();
		if (prev != null) {
			value = (prev.floatValue() + value) / 2;
		}
		controller.setCloudHeight(value);
	}

}
