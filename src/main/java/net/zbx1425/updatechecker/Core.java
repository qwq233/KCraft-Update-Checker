package net.zbx1425.updatechecker;

import java.io.File;
import java.nio.file.Path;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

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
@Mod(modid="moecraftupdatechecker", clientSideOnly = true, useMetadata = true)
public class Core {

	public static boolean updateCheckComplete;
	public static boolean forceExit = false;
	
	public static GuiDialog gdinstance;

    /*@SubscribeEvent
    public static void openGui(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu && !updateCheckComplete) {
        	gdinstance.dispose();
            event.setGui(new Gui());
        }
    }*/
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) throws InterruptedException
    {
		if (System.getProperty("moecraft.skipUpdateCheck") == null) {
			gdinstance = new GuiDialog();
			gdinstance.startTask();
			gdinstance.setVisible(true);
			if (forceExit) {
				throw new InterruptedException("游戏需要更新，更新检测程序已中断游戏启动。这是正常现象。\n"
						+ "The game needs update, so the update checker interrupted the startup progress. THIS IS NORMAL.");
			}
		} else {
			event.getModLog().info("KCraft update check skipped because of VM property given");
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
