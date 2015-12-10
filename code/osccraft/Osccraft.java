package osccraft;

import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscP5;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
//import cpw.mods.fml.client.event.ConfigChangedEvent;				// removed for 1.8
//import cpw.mods.fml.common.registry.GameRegistry;					// removed for 1.8
//import cpw.mods.fml.common.registry.LanguageRegistry;				// removed for 1.8

//import oscP5.OscP5;
import net.minecraftforge.common.MinecraftForge;
//import cpw.mods.fml.common.FMLCommonHandler;						// removed for 1.8
//import cpw.mods.fml.common.Mod;									// removed for 1.8
//import cpw.mods.fml.common.Mod.EventHandler; // used in 1.6.2
//import cpw.mods.fml.common.Mod.PreInit;    // used in 1.5.2
//import cpw.mods.fml.common.Mod.Init;       // used in 1.5.2
//import cpw.mods.fml.common.Mod.PostInit;   // used in 1.5.2
//import cpw.mods.fml.common.Mod.Instance;							// removed for 1.8
//import cpw.mods.fml.common.SidedProxy;							// removed for 1.8
//import cpw.mods.fml.common.event.FMLInitializationEvent;			// removed for 1.8
//import cpw.mods.fml.common.event.FMLPostInitializationEvent;		// removed for 1.8
//import cpw.mods.fml.common.event.FMLPreInitializationEvent;		// removed for 1.8
//import cpw.mods.fml.common.network.NetworkMod; // not used in 1.7

//import cpw.mods.fml.common.eventhandler.SubscribeEvent;			// removed for 1.8
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
// 1.8
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import netP5.NetAddress;

@Mod(modid="Osccraft", name="Osccraft", version="0.0.6", guiFactory = "osccraft.OscGuiFactory")
//@NetworkMod(clientSideRequired=true) // not used in 1.7
public class Osccraft {
	
		final OscP5 thisOscP5= new OscP5((Object)this, osccraft.Osccraft.configInPort);	
		public World myWorld;
		public IWorldAccess myIWorld;
		
		// Configuration file variables
		public static Configuration configFile;	
		//public static int myConfigInteger = 32;
		//public static String myConfigString = "Hello!";
		//public static boolean myConfigBool = false;						
		public static String configIpAddress = "127.0.0.1";
		public static int configOutPort = 6666;
		public static int configInPort = 6667;
		
        // The instance of your mod that Forge uses.
        @Instance(value = "Osccraft")
        public static Osccraft instance;
        
        // Custom OSC block class
        public static Block oscDirt, oscRock, oscKeystone;
        public static CreativeTabs tabOsccraft = new OscCreativeTabs("OSCCraft");
        
        // Says where the client and server 'proxy' code is loaded.
        @SidedProxy(clientSide="osccraft.client.ClientProxy", serverSide="osccraft.CommonProxy")
        public static CommonProxy proxy;
        
        @EventHandler // used in 1.6.2
        //@PreInit    // used in 1.5.2
        public void preInit(FMLPreInitializationEvent event) {

        	// init custom blocks for this mod
        	this.initBlocks();
        	
        	configFile = new Configuration(event.getSuggestedConfigurationFile());
        	 
            syncConfig();
                	        	     
        }
        
        public static void syncConfig() {
            //myConfigInteger = configFile.getInt("My Config Integer", Configuration.CATEGORY_GENERAL, myConfigInteger, 0, Integer.MAX_VALUE, "An Integer!");
            //myConfigString = configFile.getString("My Config String", Configuration.CATEGORY_GENERAL, myConfigString, "A String!");
            //myConfigBool = configFile.getBoolean("My Config Bool", Configuration.CATEGORY_GENERAL, myConfigBool, "A Boolean!");
            
            configIpAddress = configFile.getString("Target IP", Configuration.CATEGORY_GENERAL, configIpAddress, "Target IP");
            configOutPort = configFile.getInt("Target Port (output)", Configuration.CATEGORY_GENERAL, configOutPort, 0, 65535, "Target Port (output)");
            configInPort = configFile.getInt("Receive Port (input)", Configuration.CATEGORY_GENERAL, configInPort, 0, 65535, "Receive Port (input)");
            
            if(configFile.hasChanged())
                configFile.save();
        }
        
        @EventHandler // used in 1.6.2
        //@Init       // used in 1.5.2
        public void load(FMLInitializationEvent event) {
        	proxy.registerRenderers();
        	
        	// Configuration file: register changes
        	FMLCommonHandler.instance().bus().register(instance);                
        }
        
        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        	if(eventArgs.modID.equals("Osccraft"))
        		syncConfig();
        }

        @EventHandler // used in 1.6.2
        //@PostInit   // used in 1.5.2
        public void postInit(FMLPostInitializationEvent event) {

        	// the majority of events use the MinecraftForge event bus:
        	MinecraftForge.EVENT_BUS.register(new OscPlayerEventHandler());

        	// but some are on the FML bus:
        	//FMLCommonHandler.instance().bus().register(new OscPlayerEventHandler());
        	
        }
        

        /* incoming osc message are forwarded to the oscEvent method. */
        void oscEvent(OscMessage theOscMessage) {
   
        	/* print the address pattern and the typetag of the received OscMessage */
        	System.out.println("#############> received an osc message: \n");
        	theOscMessage.print();
        	
        	switch (theOscMessage.addrPattern()) {
        	
        		case "/osccraft/block/add":
        			//addBlock(theOscMessage);
        			addOscBlock(theOscMessage);
        			break;
        		case "/osccraft/block/remove":
        			removeBlock(theOscMessage);
        			break;
        		case "/osccraft/block/opacity":			// not working
        			setBlockOpacity(theOscMessage);
        			break;
        		case "/osccraft/block/light":			// not working
        			setBlockLight(theOscMessage);
        			break;        		
        	}

        }
        
        private void setBlockOpacity(OscMessage theOscMessage){

        	int opacity, x, y, z;
        	
        	opacity = theOscMessage.get(0).intValue();
        	x = theOscMessage.get(1).intValue();
        	y = theOscMessage.get(2).intValue();
        	z = theOscMessage.get(3).intValue();
        	
        	//Block block = myWorld.getBlock(x, y, z);
        	//block.setLightOpacity(opacity);     
        	BlockPos location = new BlockPos(x, y, z);
        	myWorld.getBlockState(location).getBlock().setLightOpacity(opacity);        	
        	Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(location);
        	myWorld.markBlockForUpdate(location);
        	
        	
        	// //myIWorld.markBlockForRenderUpdate(x,y,z);
        	// //myWorld.markBlockRangeForRenderUpdate(x,y,z,x,y,z);
        }
        
        private void setBlockLight(OscMessage theOscMessage){

        	float level;
        	int x, y, z;
        	
        	level = theOscMessage.get(0).floatValue();
        	x = theOscMessage.get(1).intValue();
        	y = theOscMessage.get(2).intValue();
        	z = theOscMessage.get(3).intValue();
        	
        	//Block block = myWorld.getBlock(x, y, z);
        	
        	//block.setLightLevel(level);

        	BlockPos location = new BlockPos(x, y, z);
        	myWorld.getBlockState(location).getBlock().setLightLevel(level);        	
        	Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(location);
        	myWorld.markBlockForUpdate(location);
        	
        	//myWorld.getBlock(x, y, z).setLightLevel(level);
        	
        	            
//        	Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(x, y, z);
//        	myWorld.markBlockForUpdate(x, y, z);

        	//myIWorld.markBlockForRenderUpdate(x, y, z);
        	//myWorld.markBlockRangeForRenderUpdate(x,y,z,x,y,z);
        	
        	//myWorld.getBlock(x, y, z).UpdateTick()
        	//myWorld.updateAllLightTypes(x, y, z);
        	
        }        
        // Add block of specified type at OSC coordinates
        private void addBlock(OscMessage theOscMessage)
        {
        	
        	System.out.print("osccraft message received\n");
    		System.out.println(" addrpattern: "+theOscMessage.addrPattern());
        	System.out.println(" typetag: "+theOscMessage.typetag());
        	System.out.println(" length: "+theOscMessage.arguments().length);
        
        	// gets client world only
        	myWorld = Minecraft.getMinecraft().theWorld;
        	            	            	
        	// Get all arguments
        	for (int i=0; i < theOscMessage.arguments().length; i++) {
        		
        		//System.out.println("Argument " + i + ": " + theOscMessage.get(i).intValue() );    
        		
        		// Create block at xyz coordinates of specified type
        		Block thisBlock;
        		String blockType = theOscMessage.get(3).stringValue();
        		
        		switch (blockType) {
        			case "oscDirt": 	
        				thisBlock = oscDirt;
        				break;
        			case "oscRock": 
        				thisBlock = oscRock;
        				break;
        			case "oscKeystone": 
        				thisBlock = oscKeystone;
        				break;
        			default: 
        				thisBlock = Blocks.melon_block;
        				break;            		
        		}
        		
        		//myWorld.setBlock(theOscMessage.get(0).intValue(), theOscMessage.get(1).intValue(), theOscMessage.get(2).intValue(), thisBlock);
        		
        		// 1.8 - this may have to run on the server (?) http://www.minecraftforge.net/forum/index.php?topic=33665.0       		
        		BlockPos location = new BlockPos(theOscMessage.get(0).intValue(), theOscMessage.get(1).intValue(), theOscMessage.get(2).intValue()); 
        		IBlockState blockState = thisBlock.getDefaultState(); 
        		myWorld.setBlockState(location, blockState);      
        		 		        		
        	}        	
        	
        }        

        private void addOscBlock(OscMessage theOscMessage)
        {
        	
        	System.out.print("addOscBlock: message received\n");
    		System.out.println(" addrpattern: "+theOscMessage.addrPattern());
        	System.out.println(" typetag: "+theOscMessage.typetag());
        	System.out.println(" length: "+theOscMessage.arguments().length);
        
        	// gets client world only
        	myWorld = Minecraft.getMinecraft().theWorld;
        	            	            	
        	// Get all arguments
        	//for (int i=0; i < theOscMessage.arguments().length; i++) {
        		
        		//System.out.println("Argument " + i + ": " + theOscMessage.get(i).intValue() );    
        		
        		// Create block at xyz coordinates of specified type
        		OscBlock thisBlock;
        		String blockType = theOscMessage.get(3).stringValue();
        		
        		switch (blockType) {
        			case "oscDirt": 	
        				thisBlock = (OscBlock) oscDirt;
        				break;
        			case "oscRock": 
        				thisBlock = (OscBlock) oscRock;
        				break;
        			case "oscKeystone": 
        				thisBlock = (OscBlock) oscKeystone;
        				break;
        			default: 
        				thisBlock = (OscBlock) Blocks.melon_block;
        				break;            		
        		}
        	
        		//myWorld.setBlock(theOscMessage.get(0).intValue(), theOscMessage.get(1).intValue(), theOscMessage.get(2).intValue(), thisBlock);
        		
        		// 1.8 - this may have to run on the server (?) http://www.minecraftforge.net/forum/index.php?topic=33665.0       		
        		BlockPos location = new BlockPos(theOscMessage.get(0).intValue(), theOscMessage.get(1).intValue(), theOscMessage.get(2).intValue()); 
        		IBlockState blockState = thisBlock.getDefaultState(); 
        		myWorld.setBlockState(location, blockState);      
        		System.out.println(" HERE: Osccraft > addOscBlock");
        		// send OSC output now for newly created block
        		thisBlock.blockAdded(theOscMessage.get(0).intValue(), theOscMessage.get(1).intValue(), theOscMessage.get(2).intValue());
        		 		        		
        	//}     	
        	
        }        
        
        
        
        private void oscAddedBlockOutput(Block thisBlock)
        {
        	
        	
        }
        
        // Remove block at OSC coordinates
        private void removeBlock(OscMessage theOscMessage) { 
        	
        	// 1.8 - this may have to run on the server (?) http://www.minecraftforge.net/forum/index.php?topic=33665.0
        	BlockPos location = new BlockPos(theOscMessage.get(0).intValue(), theOscMessage.get(1).intValue(), theOscMessage.get(2).intValue());
        	myWorld.setBlockToAir(location);
        	
        	//myWorld.setBlockToAir(theOscMessage.get(0).intValue(), theOscMessage.get(1).intValue(), theOscMessage.get(2).intValue());        
        }
        
        private void initBlocks()
        {
        	initBlockOscDirt();
        	initBlockOscRock();
        	initBlockOscKeystone();
        	
        	proxy.registerRenderers();       
        }
        
        private void initBlockOscDirt()
        {
        	final String blockName = "OscDirt";
        	
        	// initialize osc-emitting "dirt" block
            oscDirt = new OscBlock(Material.ground)
            		.setHardness(0.5F)
            		.setStepSound(Block.soundTypeGravel)
            		.setUnlocalizedName(blockName);	//1.8
            		//.setBlockName("OscDirt")            		           
            		//.setBlockTextureName("dirt");            		            	
      
            oscDirt.setHarvestLevel("shovel", 0);                                 
            //oscDirt.getUnlocalizedName()
            GameRegistry.registerBlock(	oscDirt, oscDirt.getUnlocalizedName().substring(5));                    	
        }
        
        private void initBlockOscRock()
        {
        	final String blockName = "OscStone";
        	
        	// initialize osc-emitting "stone" block
        	oscRock = new OscBlock(Material.rock)
            		.setHardness(0.5F)
            		.setStepSound(Block.soundTypeStone)
            		.setUnlocalizedName(blockName);	//1.8
            		//.setBlockName("OscStone")            		            		
            		//.setBlockTextureName("stone");            		            	
        	
        	oscRock.setHarvestLevel("shovel", 0);                                 
                    	        	
            GameRegistry.registerBlock(	oscRock, oscRock.getUnlocalizedName().substring(5));                    	        	        	
        }
        
        private void initBlockOscKeystone()
        {
        	final String blockName = "OscKeystone";
        	
        	// initialize osc-emitting "dirt" block
            oscKeystone = new OscBlock(Material.ice)
            		.setHardness(100.0F)
            		.setStepSound(Block.soundTypeGravel)
            		.setUnlocalizedName(blockName);	//1.8             		
            		//.setBlockName("OscKeystone")
            		//.setBlockTextureName("ice");            		            	
      
            oscKeystone.setHarvestLevel("shovel", 0);                                 
            
            GameRegistry.registerBlock(	oscKeystone, oscKeystone.getUnlocalizedName().substring(5));                    	
        }
                
}