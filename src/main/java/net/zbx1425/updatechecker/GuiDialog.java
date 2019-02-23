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
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

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
		contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] {200, 0, 0};
		gbl_contentPanel.rowHeights = new int[] {40, 30, 80, 20, 30};
		gbl_contentPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		contentPanel.setLayout(gbl_contentPanel);
		{
			okButton.setForeground(Color.GRAY);
			okButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
			okButton.setHorizontalAlignment(SwingConstants.LEFT);
			okButton.setVisible(false);
			{
				lblTitle.setVerticalAlignment(SwingConstants.BOTTOM);
				lblTitle.setFont(new Font("微软雅黑", Font.BOLD, 27));
				GridBagConstraints gbc_lblTitle = new GridBagConstraints();
				gbc_lblTitle.gridwidth = 2;
				gbc_lblTitle.fill = GridBagConstraints.BOTH;
				gbc_lblTitle.insets = new Insets(0, 0, 5, 0);
				gbc_lblTitle.gridx = 0;
				gbc_lblTitle.gridy = 0;
				contentPanel.add(lblTitle, gbc_lblTitle);
			}
			{
				lblProgress.setFont(new Font("微软雅黑", Font.PLAIN, 20));
				lblProgress.setVerticalAlignment(SwingConstants.TOP);
				GridBagConstraints gbc_lblProgress = new GridBagConstraints();
				gbc_lblProgress.gridwidth = 2;
				gbc_lblProgress.fill = GridBagConstraints.BOTH;
				gbc_lblProgress.insets = new Insets(0, 0, 5, 0);
				gbc_lblProgress.gridx = 0;
				gbc_lblProgress.gridy = 1;
				contentPanel.add(lblProgress, gbc_lblProgress);
			}
			{
				lblDetail.setFont(lblDetail.getFont().deriveFont(lblDetail.getFont().getSize() + 4f));
				lblDetail.setVerticalAlignment(SwingConstants.TOP);
				GridBagConstraints gbc_lblDetail = new GridBagConstraints();
				gbc_lblDetail.gridwidth = 2;
				gbc_lblDetail.fill = GridBagConstraints.BOTH;
				gbc_lblDetail.insets = new Insets(0, 0, 5, 0);
				gbc_lblDetail.gridx = 0;
				gbc_lblDetail.gridy = 2;
				contentPanel.add(lblDetail, gbc_lblDetail);
			}
			okButton.setFocusPainted(false);
			GridBagConstraints gbc_okButton = new GridBagConstraints();
			gbc_okButton.fill = GridBagConstraints.BOTH;
			gbc_okButton.insets = new Insets(0, 0, 5, 0);
			gbc_okButton.gridx = 0;
			gbc_okButton.gridy = 3;
			contentPanel.add(okButton, gbc_okButton);
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
			GridBagConstraints gbc_cancelButton = new GridBagConstraints();
			gbc_cancelButton.fill = GridBagConstraints.BOTH;
			gbc_cancelButton.gridx = 0;
			gbc_cancelButton.gridy = 4;
			contentPanel.add(cancelButton, gbc_cancelButton);
			cancelButton.setFont(new Font("微软雅黑", Font.BOLD, 25));
			cancelButton.setActionCommand("Cancel");
			cancelButton.addActionListener(new ActionListener(){
	            @Override
	            public void actionPerformed(ActionEvent arg0) {
	            	Core.updateCheckComplete = true;
	            	try {
		            	if (operstp==1) {
		            		System.out.println(Core.outboundpath()+"/MoeCraft-Toolbox.jar");
		            		Runtime.getRuntime().exec(new String[]{"java","-jar",Core.outboundpath()+"/MoeCraft-Toolbox.jar"},
		            				new String[]{}, new File(Core.outboundpath()));
						} else if (operstp==2) {
							java.awt.Desktop.getDesktop().browse(new URI("https://accounts.moecraft.net/Doc/Protected/download"));
						}
		            	if (operstp < 3) {
		            		lblProgress.setText("正在启动外部程序");
							lblDetail.setText("<html>将中断游戏启动以便进行更新。<br>请先关闭Minecraft，再开始更新！<br>您可能会收到一些错误提示，请您直接忽略并关闭游戏。这是正常现象，并非软件问题。</html>");
							cancelButton.setVisible(false);
							okButton.setVisible(false);
							repaint();
		            	}
	            	} catch (IOException | URISyntaxException ex) {

					} finally {
						 Thread tipthread=new Thread(new Runnable() {
					            @Override
					            public void run() {
									try {
										Thread.sleep(4000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									Minecraft.getMinecraft().shutdown();
									Core.forceExit = true;
					            	dispose();
								}
						 });
						 tipthread.start();
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
