package thefloydman.moremystcraft.symbol.symbols;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.xcompwiz.mystcraft.api.symbol.BlockCategory;
import com.xcompwiz.mystcraft.api.symbol.BlockDescriptor;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.api.world.logic.IPopulate;
import com.xcompwiz.mystcraft.world.gen.feature.WorldGeneratorAdv;

import thefloydman.moremystcraft.symbol.SymbolBase;
import thefloydman.moremystcraft.world.gen.structure.feature.WorldGenPyramids;

public class SymbolPyramids extends SymbolBase
{
    public SymbolPyramids(final ResourceLocation identifier) {
        super(identifier);
    }
    
    @Override
    public void registerLogic(final AgeDirector controller, final long seed) {
        final BlockDescriptor block = ModifierUtils.popBlockMatching(controller, BlockCategory.STRUCTURE);
        WorldGeneratorAdv generator;
        if (block != null) {
            generator = new WorldGenPyramids(block.blockstate);
        }
        else {
            generator = new WorldGenPyramids(Blocks.SANDSTONE);
        }
        controller.registerInterface(new Populator(generator));
    }
    
    @Override
    public boolean generatesConfigOption() {
        return true;
    }
    
    private static class Populator implements IPopulate
    {
        private WorldGeneratorAdv generator;
        
        public Populator(final WorldGeneratorAdv generator) {
            this.generator = generator;
        }
        
        @Override
        public boolean populate(final World worldObj, final Random rand, final int k, final int l, final boolean flag) {
            if (!flag && rand.nextInt(18) == 0) {
                final int x = k + rand.nextInt(16);
                final int z = l + rand.nextInt(16);
                final int y = worldObj.getHeight(x, z);
                this.generator.generate(worldObj, rand, new BlockPos(x, y, z));
            }
            else {
                this.generator.noGen();
            }
            return false;
        }
    }

	@Override
	public IAgeSymbol setRegistryName(ResourceLocation name) {
		return null;
	}
}
