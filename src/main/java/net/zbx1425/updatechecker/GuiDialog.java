package net.zbx1425.updatechecker;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.crash.CrashReport;
import net.zbx1425.updatechecker.Work.checkerCallback;

import java.awt.Font;
import java.awt.Dialog.ModalityType;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;
import java.awt.Color;

public class GuiDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	int operstp = 0;

	JLabel lblTitle = new JLabel("MoeCraft 更新检测程序");
	JLabel lblProgress = new JLabel("请稍等片刻……");
	JLabel lblDetail = new JLabel("");
	JButton okButton = new JButton("继续启动（可能造成无法登陆或资源错误）");
	JButton cancelButton = new JButton("前往更新");

	public GuiDialog() {
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setAlwaysOnTop(true);
		setTitle("MoeCraft 更新检测程序");
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 20, 20, 100));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(5, 1, 0, 10));
		{
			lblTitle.setVerticalAlignment(SwingConstants.BOTTOM);
			lblTitle.setFont(new Font("微软雅黑", Font.BOLD, 27));
			contentPanel.add(lblTitle);
		}
		{
			lblProgress.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			lblProgress.setVerticalAlignment(SwingConstants.TOP);
			contentPanel.add(lblProgress);
		}
		{
			lblDetail.setFont(lblDetail.getFont().deriveFont(lblDetail.getFont().getSize() + 4f));
			lblDetail.setVerticalAlignment(SwingConstants.TOP);
			contentPanel.add(lblDetail);
		}
		{
			okButton.setForeground(Color.GRAY);
			okButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
			okButton.setHorizontalAlignment(SwingConstants.LEFT);
			okButton.setVisible(false);
			okButton.setFocusPainted(false);
			contentPanel.add(okButton);
			okButton.setActionCommand("OK");
			okButton.addActionListener(new ActionListener(){
	            @Override
	            public void actionPerformed(ActionEvent arg0) {
	            	Core.updateCheckComplete = true;
	            	dispose();
	            }           
	        });
		}
		{
			cancelButton.setHorizontalAlignment(SwingConstants.LEFT);
			cancelButton.setVisible(false);
			cancelButton.setFocusPainted(false);
			contentPanel.add(cancelButton);
			cancelButton.setFont(new Font("微软雅黑", Font.BOLD, 25));
			cancelButton.setActionCommand("Cancel");
			cancelButton.addActionListener(new ActionListener(){
	            @Override
	            public void actionPerformed(ActionEvent arg0) {
	            	Core.updateCheckComplete = true;
	            	try {
		            	if (operstp==1) {
		            		Runtime.getRuntime().exec(new String[]{"java","-jar",Core.outboundpath()+"/MoeCraft-Toolbox.jar"},
		            				new String[]{}, new File(Core.outboundpath()));
						} else if (operstp==2) {
							java.awt.Desktop.getDesktop().browse(new URI("https://accounts.moecraft.net/Doc/Protected/download"));
						}
	            	} catch (IOException | URISyntaxException ex) {

					} finally {
						Minecraft.getMinecraft().shutdown();
						Core.forceExit = true;
		            	dispose();
					}
	            }           
	        });
			getRootPane().setDefaultButton(cancelButton);
		}
	}
	
	public void startTask() {
		Work.runtask(new checkerCallback(){

			@Override
			public void success() {
				Core.updateCheckComplete = true;
				lblProgress.setText("版本已是最新");
				lblProgress.setForeground(new Color(0x22,0x8b,0x22));
				lblDetail.setText("<html>您的游戏毫无问题。<br>更新检测程序现在自动关闭。</html>");
				repaint();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					
				} finally {
					dispose();
				}
			}
			
			@Override
			public void fail(int osp, String str) {
				operstp = osp;
				final String pgtxt,cbtxt;
				final Color lpfgc;
				if (operstp==1) {
					pgtxt = "版本过时，请您更新！";
					cbtxt = "立刻更新";
					lpfgc = new Color(0xff,0x45,0x00);
				} else if (operstp==2) {
					pgtxt = "更新器过期，请重新下载！";
					cbtxt = "前往官网下载";
					lpfgc = Color.RED;
				} else if (operstp==2) {
					pgtxt = "程序出现错误！";
					cbtxt = "退出游戏";
					lpfgc = Color.RED;
				} else {
					pgtxt = cbtxt = "NULL";
					lpfgc = Color.RED;
				}
				SwingUtilities.invokeLater(
		                new Runnable() {
							@Override
							public void run() {
								lblProgress.setText(pgtxt);
								cancelButton.setText(cbtxt);
								lblProgress.setForeground(lpfgc);
								lblDetail.setText("<html>"+str.replace("\n", "<br>")+"</html>");
								okButton.setVisible(true);
								cancelButton.setVisible(true);
								repaint();
							}
		                }
		    	);
			}
			
		});
	}

}
