package osccraft.client;

//import cpw.mods.fml.common.Mod.EventHandler;					//removed for 1.8
//import cpw.mods.fml.common.event.FMLPostInitializationEvent;  //removed for 1.8
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import osccraft.CommonProxy;
import osccraft.OscPlayerEventHandler;

public class ClientProxy extends CommonProxy {
        
        @Override
        public void registerRenderers() {
                // This is for rendering entities and so forth later on
        }       
        
}