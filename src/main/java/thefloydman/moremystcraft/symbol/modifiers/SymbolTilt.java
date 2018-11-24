package thefloydman.moremystcraft.symbol.modifiers;

import net.minecraft.util.*;
import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;

import com.xcompwiz.mystcraft.api.world.*;
import com.xcompwiz.mystcraft.api.symbol.*;

public class SymbolTilt extends MoreMystcraftSymbolBase
{
    private final float value;
    private final String display;
    
    public SymbolTilt(final ResourceLocation identifier, final float value, final String display) {
        super(identifier);
        this.value = value;
        this.display = display;
    }
    
    @Override
    public boolean generatesConfigOption() {
        return true;
    }
    
    @Override
    public void registerLogic(final AgeDirector controller, final long seed) {
        Float value = this.value;
        final Number prev = controller.popModifier("tilt").asNumber();
        if (prev != null) {
            value = ModifierUtils.averageAngles(prev.floatValue(), value);
        }
        if (value >= 360.0f) {
            value -= 360.0f;
        }
        controller.setModifier("tilt", value);
    }
    
    public String generateLocalizedName() {
        return this.display + " Tilt";
    }
}
