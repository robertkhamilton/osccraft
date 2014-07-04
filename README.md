osccraft
========

Minecraft modded with osc output
- implements Open Sound Control via OscP5

--- --- --- --- --- --- --- --- --- ---

  v0.0.1 

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

  Notes:

	* In Minecraft Launcher > profile it was necessary to manually set the Java version (on OS X) to make OSC/UDP output work
	  - Under "Java Settings (Advanced)" set "Executable" to be:
	    e.g. "/Library/Java/JavaVirtualMachines/jdk1.8.0_05.jdk/Contents/Home/bin/java"
	* Can increase memory under "JVM Arguments":
	    e.g. "-Xmx2048M -Xms2048M"
		
--- --- --- --- --- --- --- --- --- ---