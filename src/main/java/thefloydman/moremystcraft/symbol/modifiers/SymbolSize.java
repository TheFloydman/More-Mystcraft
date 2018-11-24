package thefloydman.moremystcraft.symbol.modifiers;

import net.minecraft.util.ResourceLocation;

import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.world.AgeDirector;

import thefloydman.moremystcraft.symbol.MoreMystcraftSymbolBase;

public class SymbolSize extends MoreMystcraftSymbolBase
{
    private final double VALUE;
    private final String DISPLAY;
    
    public SymbolSize(final ResourceLocation identifier, final double value, final String display) {
        super(identifier);
        this.VALUE = value;
        this.DISPLAY = display;
    }
    
    @Override
    public boolean generatesConfigOption() {
        return true;
    }
    
    @Override
    public void registerLogic(final AgeDirector controller, final long seed) {
        double value = this.VALUE;
        final Number prev = controller.popModifier("size").asNumber();
        if (prev != null) {
            value = combineDoubles(prev.doubleValue(), value);
        }
        controller.setModifier("size", value);
    }
    
    public String generateLocalizedName() {
        return this.DISPLAY + " Size";
    }
    
    private double combineDoubles(double a, double b) {
    	return ((a + 1) / 2) * b;
    }
}