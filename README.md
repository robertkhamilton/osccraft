osccraft
========

Minecraft modded with osc output
- implements Open Sound Control via OscP5

--- --- --- --- --- --- --- --- --- ---
  v0.0.4 (tag: "v0.0.4")
   
        - Networking updates to correctly pass events to non-server networked machines
   

  v0.0.3 (tag: no tag)
  
        - Added new "OSCKeystone" block with (nearly) infinite Block.hardness and "ice" skin
        - renamed OSCRock to OSCStone
        - reset OSC parameters for block type to start with OSCDirt (1), OSCStone (2), OSCKeystone (3)
        - no code update or tag for v0.0.3, just compiled mod .jar as v0.0.4 is coming right behind
        
        
  v0.0.2 (tag: "v0.0.2")

        - added 2 custom OSC blocks (dirt, stone) capable of outputting osc messages when created, hit or destroyed
           => /osccraft/block/destroyed int32:178, int32:275, int32:70, int32:272, int32:272, int32:7
           => /osccraft/block/placed float32:17, float32:278, float32:64, int32:292, int32:292
           => /osccraft/block/hit int32:17, int32:278, int32:64, int32:3, int32:292, int32:292, int32:7

        - removed duplicate block event occurances by checking for client-side only: if(!player.worldObj.isRemote)
        - added custom OscCreativeTab for OSC blocks


  v0.0.1 (tag: "v0.0.1")

	- initial release of osccraft
	- outputs player position data and block hit/destroyed events
	- built into the Minecraft Forge 1.7.2 framework and can be loaded as a standard mod
	- output hardcoded to "localhost:6666"
	- output namespaces:
	   => /osccraft/player <x> <y> <z> <pitch> <yaw> <player-id> <event-id>
	   => /osccraft/block/hit <x> <y> <z> <block-side> <player-id> <event-id> <block-type>  
	   => /osccraft/block/destroyed <x> <y> <z> <player-id> <event-id> <block-type>  
	   => /osccraft/block/create [NOT WORKING YET]


  Installation:

	- install Minecraft, eclipse, recent JDK
	- install Forge 1.7.2 (release 1.7.2-Forge10.12.2.1147)
	- Create Minecraft profile in Launcher pointing to "release 1.7.2-Forge10.12.2.1147"	
	- To use multiple users on LAN with just 1 Minecraft Acct:
	   => edit /Users/<your-user>/Library/Application Support/minecraft/versions/1.7.2-Forge10.12.2.1147/1.7.2-Forge10.12.2.1147.json
	   => change "minecraftArguments" line: set "--username [custom-local-username] and "-uuid [custom-local-uuid]"

  Compilation with Gradle:

        - ./gradlew build

  Java:
	* On OS X machines, it is necessary to use a recent non-default version of the Java JDK to use OSC.
	* Download Java 8 SDK at: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html


  Notes:

	* In Minecraft Launcher > profile it was necessary to manually set the Java version (on OS X) to make OSC/UDP output work
	  - Under "Java Settings (Advanced)" set "Executable" to be:
	    e.g. "/Library/Java/JavaVirtualMachines/jdk1.8.0_05.jdk/Contents/Home/bin/java"
	* Can increase memory under "JVM Arguments":
	    e.g. "-Xmx2048M -Xms2048M"
		
--- --- --- --- --- --- --- --- --- ---
