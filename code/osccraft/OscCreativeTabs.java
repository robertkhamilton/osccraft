package osccraft;

//import cpw.mods.fml.relauncher.Side;			// removed for 1.8
//import cpw.mods.fml.relauncher.SideOnly;		// removed for 1.8
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OscCreativeTabs extends CreativeTabs {

public OscCreativeTabs(String tabLabel)
{
	super(tabLabel);
}

@Override
@SideOnly(Side.CLIENT)
public Item getTabIconItem()
{
	return Item.getItemFromBlock(Blocks.dirt);
}

}