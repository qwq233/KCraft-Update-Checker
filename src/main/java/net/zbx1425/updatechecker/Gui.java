package net.zbx1425.updatechecker;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.zbx1425.updatechecker.Work.checkerCallback;

@SideOnly(Side.CLIENT)
public class Gui extends GuiScreen {
	String text2 = "请稍等……";
	int operstp = 0;
	String seq = "iamanidiot";
	int seqpos = 0;
	
	public Gui() {
		super();
		Keyboard.enableRepeatEvents(false);
		Work.runtask(new checkerCallback(){

			@Override
			public void success() {
				Core.updateCheckComplete = true;
				Minecraft.getMinecraft().addScheduledTask(()->{
					mc.displayGuiScreen((GuiScreen)null);
				});
			}
			
			@Override
			public void fail(int osp, String str) {
				Core.updateCheckComplete = true;
				operstp = osp;
				text2 = str;
				updateScreen();
			}

			@Override
			public void nwoverride() {
				Core.updateCheckComplete = true;
				Minecraft.getMinecraft().addScheduledTask(()->{
					mc.displayGuiScreen((GuiScreen)null);
				});
			}
			
		});
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawWorldBackground(0);
		this.fontRenderer.drawStringWithShadow("MoeCraft 更新检测程序", 40, 60, -1);
		GlStateManager.scale(2, 2, 2);
		this.fontRenderer.drawStringWithShadow(new String[] {"操作进行中","请更新游戏","请更新更新器","错误"}[operstp],
				20, 35, ((operstp>0) ? 0x00FF0000 : -1));
		GlStateManager.scale(.5, .5, .5);
		dms(text2, 40, 100);
		switch (operstp) {
		case 1:
			dms("按 [Enter] 前往更新器\n输入 [I am an idiot] 继续启动(^_^)", 40, 140);
			break;
		case 2:
			dms("按 [Enter] 前往官网下载\n输入 [I am an idiot] 继续启动(^_^)", 40, 140);
			break;
		case 3:
			dms("按 [Enter] 关闭游戏\n输入 [I am an idiot] 继续启动(^_^)", 40, 140);
			break;
		}
    }
	
	private void dms(String str,int x, int y) {
		String[] se = str.split("\n");
		for (int i = 0;i<se.length;i++) {
			this.fontRenderer.drawStringWithShadow(se[i], x, y+9*i, -1);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
		if (operstp>0) {
			if (Character.toLowerCase(typedChar) == seq.charAt(seqpos)) {
				seqpos++;
				if (seqpos >= seq.length()) {
					mc.displayGuiScreen((GuiScreen)null);
					return;
				}
			}
		}
		if (operstp>0 && (keyCode == 28 || keyCode == 156)) {
			switch (operstp) {
			case 1:
				Runtime.getRuntime().exec(new String[]{"java","-jar",Core.outboundpath()+"/MoeCraft-Toolbox.jar"},new String[]{}, new File(Core.outboundpath()));
				break;
			case 2:
				try{
		            java.awt.Desktop.getDesktop().browse(new URI("https://accounts.moecraft.net/Doc/Protected/download"));
		        }
		        catch (Throwable throwable1) {
		        }
				break;
			}
			this.mc.shutdown();
		}
        this.updateScreen();
    }
	
}
