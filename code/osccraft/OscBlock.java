package osccraft;

import oscP5.OscMessage;
import oscP5.OscP5;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import netP5.NetAddress;

public class OscBlock extends Block 
{
	final OscP5 thisOscP5= new OscP5((Object)this, 7000);		
	NetAddress blockPlacedRemoteLocation = new NetAddress(osccraft.Osccraft.configIpAddress,osccraft.Osccraft.configOutPort);
	NetAddress blockWalkingRemoteLocation = new NetAddress(osccraft.Osccraft.configIpAddress,osccraft.Osccraft.configOutPort);	        
	NetAddress blockDestroyedRemoteLocation = new NetAddress(osccraft.Osccraft.configIpAddress,osccraft.Osccraft.configOutPort);	
	OscMessage blockPlacedMessage = new OscMessage("/osccraft/block/placed");
	OscMessage blockWalkingMessage = new OscMessage("/osccraft/block/walking");
	OscMessage blockDestroyedMessage = new OscMessage("/osccraft/block/destroyed");

	OscMessage blockHitMessage = new OscMessage("/osccraft/block/hit");
	NetAddress blockHitRemoteLocation = new NetAddress(osccraft.Osccraft.configIpAddress,osccraft.Osccraft.configOutPort);
	
	BlockPos lastOscAddedPosition = new BlockPos(-9999, -9999, -9999);
	
	// set block to be updated on next tick
	Boolean updateNow;	
	
        public OscBlock (Material material) 
        {
                super(material);
                
                // set custom creative tab
                this.setCreativeTab(Osccraft.tabOsccraft);
                
                System.out.println("************** NEW BLOCK");
        }

        
//      public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
//      {
//          return false;
//      }
        
        /**
         * Called when the block is placed in the world. World, int, int, int, EntityLivingBase, ItemStack
         */                      
        //@Override
        //public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
        //@Override
        public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {        
        //public void onBlockPlaced(World, int, int, int, int, float, float, float, int)	
//        	System.out.println("Placed OscBlock: " + p_149689_2_ + ", " + p_149689_3_ + ", " + p_149689_4_);    
        	System.out.println("Placed OscBlock: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
        	
    		EntityPlayer player = (EntityPlayer) placer;

    		// check if event is client side; ignore if server side
	    	if(!player.worldObj.isRemote) {            	            

	    		// only sed to same client (ignore other client's block messages)
	    		if(Minecraft.getMinecraft().thePlayer.getEntityId() == player.getEntityId()) {	

	    			this.blockPlacedMessage.setAddrPattern("/osccraft/block/placed");
	    			this.blockPlacedRemoteLocation = new NetAddress(osccraft.Osccraft.configIpAddress,osccraft.Osccraft.configOutPort);
	    			this.blockPlacedMessage.add((float)pos.getX());
	    			this.blockPlacedMessage.add((float)pos.getZ());
	    			this.blockPlacedMessage.add((float)pos.getY());	    	
	    			this.blockPlacedMessage.add(Minecraft.getMinecraft().thePlayer.getEntityId());
	    			this.blockPlacedMessage.add(player.getEntityId());
    		    	    	
    	    		this.thisOscP5.send(blockPlacedMessage, blockPlacedRemoteLocation);
    	    	    System.out.println("Placing block..");    	    		
	    		}
    	        	    
	    		this.blockPlacedMessage.clear();
	    	}
        }

        
        public void blockAdded(int x, int y, int z) //World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {                	    
        {
        	System.out.println("osc created OscBlock: " + x + ", " + y + ", " + z );        	

        	//if(lastOscAddedPosition.getX() != x && lastOscAddedPosition.getY() != y && lastOscAddedPosition.getZ() != z)
        	//{
        		this.blockPlacedMessage.setAddrPattern("/osccraft/block/oscplaced");
        		this.blockPlacedRemoteLocation = new NetAddress(osccraft.Osccraft.configIpAddress,osccraft.Osccraft.configOutPort);
        		this.blockPlacedMessage.add((float)x);
        		this.blockPlacedMessage.add((float)z);
        		this.blockPlacedMessage.add((float)y);	    	
    		    	    	
        		this.thisOscP5.send(blockPlacedMessage, blockPlacedRemoteLocation);    	    		
	    		this.blockPlacedMessage.clear();
	    		
	    		//System.out.println("@#$@#$@#$@#$@#$@#$@#   Sending OSC MESSAGE FOR BLOCK PLACED..\n");
	    		
	    		BlockPos newOscAddedPosition = new BlockPos(x, y, z);
	    		lastOscAddedPosition = newOscAddedPosition;
        	//}
        }
        
        
        /**
         * Called after a block is placed
         */
        //public void onPostBlockPlaced(World p_149714_1_, int p_149714_2_, int p_149714_3_, int p_149714_4_, int p_149714_5_) {
        public void onPostBlockPlaced(World p_149714_1_, BlockPos pos, int p_149714_5_) {        	
        	System.out.println("@#$@#$@#$@#$@#$@#$@#   After Placing block..\n");
        }

        /**
         * Called when a player hits the block. Args: world, x, y, z, player
         */        
        //public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_, EntityPlayer p_149699_5_) {
        public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)	
        {
        	EntityPlayer player = playerIn;
        	
        	// Client side check
        	//if(!player.worldObj.isRemote) {            	            

        		// only send to same client (ignore other client's block messages)
        		if(Minecraft.getMinecraft().thePlayer.getEntityId() == player.getEntityId()) {	
        			
        			int blockX = pos.getX();
        			int blockY = pos.getY();
        			int blockZ = pos.getZ();
        			//int face = theEvent.face;    	
        			            
        			// trim "tile." from blockName
        			String blockname = this.getUnlocalizedName().substring(5);
        	
        			int blockType;
        			this.blockHitMessage.setAddrPattern("/osccraft/block/hit");
        			this.blockHitRemoteLocation = new NetAddress(osccraft.Osccraft.configIpAddress,osccraft.Osccraft.configOutPort);
        			this.blockHitMessage.add(blockX);
        			this.blockHitMessage.add(blockZ);
        			this.blockHitMessage.add(blockY);
        			//this.blockHitMessage.add(face);
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
        	//}
        	
        	
        	
        }        
        
        /**
         * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
         */
        //public void onBlockDestroyedByPlayer(World p_149664_1_, int p_149664_2_, int p_149664_3_, int p_149664_4_, int p_149664_5_) {}
        public void onBlockDestroyedByPlayer(World p_149664_1_, BlockPos pos, int p_149664_5_) {}
        
        //public void onBlockDestroyedByExplosion(World p_149723_1_, int p_149723_2_, int p_149723_3_, int p_149723_4_, Explosion p_149723_5_) {}
        public void onBlockDestroyedByExplosion(World p_149723_1_, BlockPos pos, Explosion p_149723_5_) {}
        
        /**
         * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
         */        
        //public void onEntityWalking(World p_149724_1_, int p_149724_2_, int p_149724_3_, int p_149724_4_, Entity p_149724_5_) {
        public void onEntityWalking(World p_149724_1_, BlockPos pos, Entity p_149724_5_) {	
        	//System.out.println("Placed OscBlock: " + p_149724_2_ + ", " + p_149724_4_ + ", " + p_149724_3_);    
        	System.out.println("WALKING ON THE BLOCK");
        	
        	//check to make sure type is player: net.minecraft.entity.monster.EntityMob
    		//EntityPlayer player = (EntityPlayer)p_149724_5_;
			
        	//updateNow=true;
        	
    		//if(p_149724_5_ instanceof EntityPlayer)
    		//{
    			EntityPlayer player = (EntityPlayer)p_149724_5_;
    			
    			this.blockWalkingMessage.setAddrPattern("/osccraft/block/walking");
    			this.blockWalkingRemoteLocation = new NetAddress(osccraft.Osccraft.configIpAddress,osccraft.Osccraft.configOutPort);
    			this.blockWalkingMessage.add((float)pos.getX());
    			this.blockWalkingMessage.add((float)pos.getZ());
    			this.blockWalkingMessage.add((float)pos.getY());	    	
    			this.blockWalkingMessage.add(Minecraft.getMinecraft().thePlayer.getEntityId());
    			this.blockWalkingMessage.add(player.getEntityId());
    			this.blockWalkingMessage.add(this.getUnlocalizedName().substring(5));
    	    
    			//if(Minecraft.getMinecraft().thePlayer.getEntityId() == player.getEntityId())
    			//{	    
    				this.thisOscP5.send(blockWalkingMessage, blockWalkingRemoteLocation);	    
    			//}
    	    
    			this.blockWalkingMessage.clear();   
    		//}
    		
        }
        
        // Test for block being added by OSC
        //public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
        public void onBlockAdded(World p_149726_1_, BlockPos pos) {
        	System.out.print("BLOCK WAS ADDED");
        	
        }
        
	    public int tickRate(World p_149738_1_)
	    {
	    	return 1;
	    	//return 10;
//	    	if(updateNow) {
//	    		return 1;
//	    	} else {
//	    		updateNow=false;
//	    		return 10;	
//	    	}
	        
	    }        

}