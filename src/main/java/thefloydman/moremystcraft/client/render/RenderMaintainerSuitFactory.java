package thefloydman.moremystcraft.client.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

import net.minecraftforge.fml.client.registry.IRenderFactory;

import thefloydman.moremystcraft.client.render.RenderMaintainerSuit;

public class RenderMaintainerSuitFactory implements IRenderFactory<Entity> {

	public static final RenderMaintainerSuitFactory INSTANCE = new RenderMaintainerSuitFactory();

	@Override
	public Render<? super Entity> createRenderFor(RenderManager manager) {
		return new RenderMaintainerSuit(manager);
	}

}