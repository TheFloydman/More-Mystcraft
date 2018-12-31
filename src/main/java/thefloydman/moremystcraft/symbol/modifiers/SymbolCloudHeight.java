package thefloydman.moremystcraft.symbol.modifiers;

import net.minecraft.util.*;
import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;

import com.xcompwiz.mystcraft.api.world.*;
import com.xcompwiz.mystcraft.api.symbol.*;

public class SymbolCloudHeight extends MoreMystcraftSymbolBase
{
    private final float value;
    private final String display;
    
    public SymbolCloudHeight(final ResourceLocation identifier, final float value, final String display) {
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
        final Number prev = controller.popModifier("cloud_height").asNumber();
        if (prev != null) {
            value = (prev.floatValue() + value) / 2;
        }
        controller.setModifier("cloud_height", value);
    }
    
    public String generateLocalizedName() {
        return this.display + " Cloud Height";
    }
}
