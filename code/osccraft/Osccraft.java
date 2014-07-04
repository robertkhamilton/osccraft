package osccraft;

//import oscP5.OscP5;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler; // used in 1.6.2
//import cpw.mods.fml.common.Mod.PreInit;    // used in 1.5.2
//import cpw.mods.fml.common.Mod.Init;       // used in 1.5.2
//import cpw.mods.fml.common.Mod.PostInit;   // used in 1.5.2
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
//import cpw.mods.fml.common.network.NetworkMod; // not used in 1.7

@Mod(modid="OsccraftModID", name="Osccraft", version="0.0.1")
//@NetworkMod(clientSideRequired=true) // not used in 1.7
public class Osccraft {

        // The instance of your mod that Forge uses.
        @Instance(value = "OsccraftModID")
        public static Osccraft instance;
        
        // Says where the client and server 'proxy' code is loaded.
        @SidedProxy(clientSide="osccraft.client.ClientProxy", serverSide="osccraft.CommonProxy")
        public static CommonProxy proxy;
        
        @EventHandler // used in 1.6.2
        //@PreInit    // used in 1.5.2
        public void preInit(FMLPreInitializationEvent event) {
                // Stub Method        	        	
        }
        
        @EventHandler // used in 1.6.2
        //@Init       // used in 1.5.2
        public void load(FMLInitializationEvent event) {
                proxy.registerRenderers();
        }
        
        @EventHandler // used in 1.6.2
        //@PostInit   // used in 1.5.2
        public void postInit(FMLPostInitializationEvent event) {

        	// the majority of events use the MinecraftForge event bus:
        	MinecraftForge.EVENT_BUS.register(new OscPlayerEventHandler());

        	// but some are on the FML bus:
        	//FMLCommonHandler.instance().bus().register(new OscPlayerEventHandler());
        	
        }
}