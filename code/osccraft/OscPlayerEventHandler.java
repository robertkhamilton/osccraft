package osccraft;

import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscProperties;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraft.event.ClickEvent;
import net.minecraft.item.ItemStack;
import netP5.NetAddress;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import osccraft.Osccraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OscPlayerEventHandler {
	
	final OscP5 thisOscP5= new OscP5((Object)this, 7000);
	final OscP5 thisOscP5Listener= new OscP5((Object)this, 6667);
	NetAddress playerRemoteLocation = new NetAddress("localhost",6666);
	NetAddress blockHitRemoteLocation = new NetAddress("127.0.0.1",6666);	
	NetAddress blockDestroyedRemoteLocation = new NetAddress("127.0.0.1",6666);
	NetAddress clickRemoteLocation = new NetAddress("localhost",6666);	
	OscMessage playerMessage = new OscMessage("/osccraft/player");
	OscMessage blockHitMessage = new OscMessage("/osccraft/block/hit");
	OscMessage blockDestroyedMessage = new OscMessage("/osccraft/block/destroyed");
	OscMessage clickMessage = new OscMessage("/osccraft/click");
	//UUID myUUID = Minecraft.getMinecraft().thePlayer.getUniqueID();	
	
    	
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onLivingUpdateEvent(LivingUpdateEvent theEvent)
	{
		// do something to player every update tick:
		if (theEvent.entity instanceof EntityPlayer)
		{			
			sendOSCPlayerPosition(theEvent);
		}
	}

	//@SideOnly(Side.CLIENT)
	public void sendOSCPlayerPosition(LivingUpdateEvent theEvent)
	{   	    		
		EntityPlayer player = (EntityPlayer) theEvent.entity;
		
		//if(!player.worldObj.isRemote) {     
		
		    //if( Minecraft.getMinecraft().thePlayer.getUniqueID().compareTo(player.getUniqueID()) == 0)
		    if(Minecraft.getMinecraft().thePlayer.getEntityId() == player.getEntityId())
		    {	    

		    	this.playerMessage.setAddrPattern("/osccraft/player");
		    	this.playerRemoteLocation = new NetAddress("localhost",6666);
		    	this.playerMessage.add((float)player.posX);
		    	this.playerMessage.add((float)player.posZ);
		    	this.playerMessage.add((float)player.posY);	    	
		    	this.playerMessage.add((float)player.rotationPitch);
		    	this.playerMessage.add((float)player.rotationYaw);
		    	this.playerMessage.add(Minecraft.getMinecraft().thePlayer.getEntityId());
		    	this.playerMessage.add(player.getEntityId());

		    	this.thisOscP5.send(playerMessage, playerRemoteLocation);	    
		    }
	    
		    this.playerMessage.clear();
		//}
	}
	
	
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onPlayerInteractEvent(PlayerInteractEvent theEvent)
	//PlayerInteractEvent(EntityPlayer player, Action action, int x, int y, int z, int face, World world))
	{
		if(theEvent.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
			//sendOSCBlockHit(theEvent);
		}
	}
	
    public void sendOSCBlockHit(PlayerInteractEvent theEvent)
    //PlayerInteractEvent(EntityPlayer player, Action action, int x, int y, int z, int face, World world))
    {
    	EntityPlayer player = (EntityPlayer) theEvent.entityPlayer;
    	
    	if(!player.worldObj.isRemote) {            	            

    		// only sed to same client (ignore other client's block messages)
    		if(Minecraft.getMinecraft().thePlayer.getEntityId() == player.getEntityId()) {	

    			int blockX = theEvent.x;
    			int blockY = theEvent.y;
    			int blockZ = theEvent.z;
    			int face = theEvent.face;    	
    			            
    			// trim "tile." from blockName
    			String blockname = theEvent.world.getBlock(blockX, blockY, blockZ).getUnlocalizedName().substring(5);
    	
    			int blockType;
    			this.blockHitMessage.setAddrPattern("/osccraft/block/hit");
    			this.blockHitRemoteLocation = new NetAddress("127.0.0.1",6666);
    			this.blockHitMessage.add(blockX);
    			this.blockHitMessage.add(blockZ);
    			this.blockHitMessage.add(blockY);
    			this.blockHitMessage.add(face);
    			this.blockHitMessage.add(Minecraft.getMinecraft().thePlayer.getEntityId());
    			this.blockHitMessage.add(player.getEntityId());
	    
    			if(blockname.compareTo("air") == 0) {    		
    				blockType=0; // DESTROYED BLOCK: AIR    	    	
    			} else if(blockname.compareTo("OscDirt") == 0) {    		    		
    				blockType=1; // DIRT
    			} else if(blockname.compareTo("OscStone") == 0) {    		
    				blockType=2; // GRASS
    			} else if(blockname.compareTo("OscKeystone") == 0) {    		    		
    				blockType=3; // STONE    		
    			} else if(blockname.compareTo("log") == 0) {    		    		
    				blockType=4; // LOG
    			} else if(blockname.compareTo("leaves") == 0) {    		    		
    				blockType=5; // LEAVES
    			} else if(blockname.compareTo("sand") == 0) {    		    		
    				blockType=6; // SAND    		    	    		    	    	    		
    			} else {
    				blockType=7; // UNKNOWN TYPE
    				// reeds, 
    			}
    			
    			this.blockHitMessage.add(blockType);
    			this.thisOscP5.send(blockHitMessage, blockHitRemoteLocation);
    		}
    	
    		this.blockHitMessage.clear();    
    	}
    }
    
	@SubscribeEvent		
	public void onBreakEvent(BreakEvent theEvent)
	//int x, int y, int z, World world, Block block, int blockMetadata, EntityPlayer player)
	{
		//System.out.println("test");		
		//System.out.println(theEvent.block.getUnlocalizedName());
		sendOSCBlockDestroyed(theEvent);
	}
	
	//@SideOnly(Side.CLIENT)
    public void sendOSCBlockDestroyed(BreakEvent theEvent)
    //int blockX, int blockZ, int blockY, int sideHit, EntityPlayer player)
    {
    	int blockX = theEvent.x;
    	int blockY = theEvent.y;
    	int blockZ = theEvent.z;
    	
    	EntityPlayer player = (EntityPlayer) theEvent.getPlayer();
    	
    	// trim "tile." from blockName
    	String blockname = theEvent.block.getUnlocalizedName().substring(5); 
    	
    	int blockType;
    	this.blockDestroyedMessage.setAddrPattern("/osccraft/block/destroyed");
    	this.blockDestroyedRemoteLocation = new NetAddress("127.0.0.1",6666);
    	this.blockDestroyedMessage.add(blockX);
    	this.blockDestroyedMessage.add(blockZ);
    	this.blockDestroyedMessage.add(blockY);
    	this.blockDestroyedMessage.add(Minecraft.getMinecraft().thePlayer.getEntityId());
	    this.blockDestroyedMessage.add(player.getEntityId());
    	
    	//myMessage.add(sideHit);
    	
    	if(blockname.compareTo("air") == 0) {    		
    		blockType=0; // DESTROYED BLOCK: AIR    	    	
    	} else if(blockname.compareTo("OscDirt") == 0) {    		    		
    		blockType=1; // DIRT
    	} else if(blockname.compareTo("OscStone") == 0) {    		
    		blockType=2; // GRASS
    	} else if(blockname.compareTo("OscKeystone") == 0) {    		    		
    		blockType=3; // STONE    		
    	} else if(blockname.compareTo("log") == 0) {    		    		
    		blockType=4; // LOG
    	} else if(blockname.compareTo("leaves") == 0) {    		    		
    		blockType=5; // LEAVES
    	} else if(blockname.compareTo("sand") == 0) {    		    		
    		blockType=6; // SAND    		    	    		    	    	    		
    	} else {
    		blockType=7; // UNKNOWN TYPE
    		// reeds, 
    	}
    	this.blockDestroyedMessage.add(blockType);
    	    	
//    	if( Minecraft.getMinecraft().thePlayer.getUniqueID().compareTo(player.getUniqueID()) == 0)
    	if(Minecraft.getMinecraft().thePlayer.getEntityId() == player.getEntityId())
    	{	    
    		this.thisOscP5.send(blockDestroyedMessage, blockDestroyedRemoteLocation);
	    }
    	
    	this.blockDestroyedMessage.clear();    	
    }
    

}