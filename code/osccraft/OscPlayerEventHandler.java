package osccraft;

import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscProperties;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import netP5.NetAddress;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import osccraft.Osccraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//sendOSCBlockHit(var2, var4, var3, this.objectMouseOver.sideHit, this.theWorld.getBlock(var2, var3, var4).getUnlocalizedName() );


public class OscPlayerEventHandler {
	
	final OscP5 thisOscP5= new OscP5((Object)this, 7000);	
	NetAddress playerRemoteLocation = new NetAddress("localhost",6666);
	NetAddress blockHitRemoteLocation = new NetAddress("127.0.0.1",6666);	
	NetAddress blockDestroyedRemoteLocation = new NetAddress("127.0.0.1",6666);	
	OscMessage playerMessage = new OscMessage("/osccraft/player");
	OscMessage blockHitMessage = new OscMessage("/osccraft/block/hit");
	OscMessage blockDestroyedMessage = new OscMessage("/osccraft/block/destroyed");
	//UUID myUUID = Minecraft.getMinecraft().thePlayer.getUniqueID();
			
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onLivingUpdateEvent(LivingUpdateEvent theEvent)
	{
		// do something to player every update tick:
		if (theEvent.entity instanceof EntityPlayer)
		{			
			sendOSCPlayerPosition(theEvent);
		}
	}

	@SideOnly(Side.CLIENT)
	public void sendOSCPlayerPosition(LivingUpdateEvent theEvent)
	{   	    		
		EntityPlayer player = (EntityPlayer) theEvent.entity;
						
		this.playerMessage.setAddrPattern("/osccraft/player");
		this.playerRemoteLocation = new NetAddress("localhost",6666);
		this.playerMessage.add((float)player.posX);
	    this.playerMessage.add((float)player.posZ);
	    this.playerMessage.add((float)player.posY);	    	
	    this.playerMessage.add((float)player.rotationPitch);
	    this.playerMessage.add((float)player.rotationYaw);
	    this.playerMessage.add(Minecraft.getMinecraft().thePlayer.getEntityId());
	    this.playerMessage.add(player.getEntityId());

	    //if( Minecraft.getMinecraft().thePlayer.getUniqueID().compareTo(player.getUniqueID()) == 0)
	    if(Minecraft.getMinecraft().thePlayer.getEntityId() == player.getEntityId())
	    {	    
	    	this.thisOscP5.send(playerMessage, playerRemoteLocation);	    
	    }
	    
	    this.playerMessage.clear();
	}
	
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onPlayerInteractEvent(PlayerInteractEvent theEvent)
	//PlayerInteractEvent(EntityPlayer player, Action action, int x, int y, int z, int face, World world))
	{
		sendOSCBlockHit(theEvent);	
	}
	
	//@SideOnly(Side.CLIENT)
    public void sendOSCBlockHit(PlayerInteractEvent theEvent)
    //PlayerInteractEvent(EntityPlayer player, Action action, int x, int y, int z, int face, World world))
    {
    	int blockX = theEvent.x;
    	int blockY = theEvent.y;
    	int blockZ = theEvent.z;
    	int face = theEvent.face;

    	EntityPlayer player = (EntityPlayer) theEvent.entityPlayer;
    	
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
    	} else if(blockname.compareTo("dirt") == 0) {    		    		
    		blockType=1; // DIRT
    	} else if(blockname.compareTo("grass") == 0) {    		
    		blockType=2; // GRASS
    	} else if(blockname.compareTo("stone") == 0) {    		    		
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

//    	if( Minecraft.getMinecraft().thePlayer.getUniqueID().compareTo(player.getUniqueID()) == 0)	    
    	if(Minecraft.getMinecraft().thePlayer.getEntityId() == player.getEntityId())
    	{
    		this.thisOscP5.send(blockHitMessage, blockHitRemoteLocation);
	    }
    	
    	this.blockHitMessage.clear();    	
    }
    
	@SubscribeEvent	
	//@SideOnly(Side.CLIENT)
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
    	} else if(blockname.compareTo("dirt") == 0) {    		    		
    		blockType=1; // DIRT
    	} else if(blockname.compareTo("grass") == 0) {    		
    		blockType=2; // GRASS
    	} else if(blockname.compareTo("stone") == 0) {    		    		
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