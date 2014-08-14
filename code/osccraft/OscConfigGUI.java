package osccraft;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
 
public class OscConfigGUI extends GuiConfig {
	
    public OscConfigGUI(GuiScreen parent) {
        super(parent,
            new ConfigElement(Osccraft.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
            "Osccraft", false, false, GuiConfig.getAbridgedConfigPath(Osccraft.configFile.toString()));
    }
}
