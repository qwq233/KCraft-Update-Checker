package net.zbx1425.updatechecker;

import java.io.File;
import java.nio.file.Path;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
@Mod(modid = "moecraftupdatechecker", name = "Update Checker for Moecraft", version = "1.0")
public class Core {

	public static boolean updateCheckComplete;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void openGui(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu && !updateCheckComplete) {
            event.setGui(new Gui());
        }
    }
    
    public static String outboundpath() {
    	String sx = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
    	sx = sx.replace("\\", "/");
    	if (sx.endsWith("/")) sx = sx.substring(0,sx.length()-1);
    	if (sx.endsWith(".")) sx = sx.substring(0,sx.length()-1);
    	if (sx.endsWith("/")) sx = sx.substring(0,sx.length()-1);
    	sx = new File(sx).getParentFile().getParentFile().getAbsolutePath();
    	sx = sx.replace("\\", "/");
    	if (sx.endsWith("/")) sx = sx.substring(0,sx.length()-1);
    	return sx;
    }
}
