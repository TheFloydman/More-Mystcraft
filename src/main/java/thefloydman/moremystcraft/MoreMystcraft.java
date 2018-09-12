package thefloydman.moremystcraft;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thefloydman.moremystcraft.proxy.CommonProxy;
import thefloydman.moremystcraft.util.Reference;
import thefloydman.moremystcraft.world.gen.structure.feature.ModWorldGenerator;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class MoreMystcraft
{
	@Instance
	public static MoreMystcraft instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit (FMLPreInitializationEvent event)
	{
		GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 0);  
	}
	
	@EventHandler
	public static void init (FMLInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public static void postInit (FMLPostInitializationEvent event)
	{
		
	}

}
