package osccraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

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
        
        // Custom OSC block class
        public static Block oscDirt;
        public static Block oscRock;
        public static CreativeTabs tabOsccraft = new OscCreativeTabs("OSCCraft");
        
        // Says where the client and server 'proxy' code is loaded.
        @SidedProxy(clientSide="osccraft.client.ClientProxy", serverSide="osccraft.CommonProxy")
        public static CommonProxy proxy;
        
        @EventHandler // used in 1.6.2
        //@PreInit    // used in 1.5.2
        public void preInit(FMLPreInitializationEvent event) {

        	// init custom blocks for this mod
        	this.initBlocks();
        	
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
        
        private void initBlocks()
        {
        	
        	initBlockOscDirt();
        	initBlockOscRock();
        	
        	proxy.registerRenderers();       
        }
        
        private void initBlockOscDirt()
        {
        	// initialize osc-emitting "dirt" block
            oscDirt = new OscBlock(Material.ground)
            		.setHardness(0.5F)
            		.setStepSound(Block.soundTypeGravel)
            		.setBlockName("OscDirt")
            		.setBlockTextureName("dirt");            		            	
      
            oscDirt.setHarvestLevel("shovel", 0);                                 
            
            GameRegistry.registerBlock(	oscDirt, oscDirt.getUnlocalizedName().substring(5));

                    	
        }
        
        private void initBlockOscRock()
        {
        	// initialize osc-emitting "dirt" block
        	oscRock = new OscBlock(Material.rock)
            		.setHardness(0.5F)
            		.setStepSound(Block.soundTypeStone)
            		.setBlockName("OscRock")
            		.setBlockTextureName("stone");            		            	
      
        	oscRock.setHarvestLevel("shovel", 0);                                 
            
            GameRegistry.registerBlock(	oscRock, oscRock.getUnlocalizedName().substring(5));

                    	        	
        	
        }
}