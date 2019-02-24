package net.zbx1425.updatechecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;

public class Work {
	public interface checkerCallback {
		public void success();
		public void nwoverride();
		public void fail(int osp, String str);
	}
    public static void runtask(final checkerCallback cb){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
            	try {
            		File metadataFile = Paths.get(Core.outboundpath(),"Updater","metadata.json").toFile();
            		JsonParser parser=new JsonParser();
            		InputStreamReader in = new InputStreamReader(new FileInputStream(metadataFile),"UTF-8");
            		JsonObject metadataJson = (JsonObject) parser.parse(in);
            		String nodeurl = metadataJson.get("node_url").getAsString();
            		Date clientdate = new Date(metadataJson.get("updated_time").getAsLong()*1000);
            		
            		URL u = new URL(nodeurl+"moecraft.json");
            		URLConnection yc = u.openConnection();
            		BufferedReader rin = new BufferedReader(new InputStreamReader(yc.getInputStream(),"UTF-8"));
            		JsonObject remoteJson = (JsonObject) parser.parse(rin);
            		Date remotedate = new Date(remoteJson.get("updated_time").getAsLong()*1000);
            		
            		if (clientdate.before(remotedate)) {
            			cb.fail(1, "请先使用更新器更新您的游戏，再重新启动。\n最新版本发布时间: "
            					+new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(remotedate)
            					+"\n请务必在更新后再进入服务器，否则可能产生资源失效、显示错乱、无法登入等各种错误。");
            		} else {
            			cb.success();
            		}
            	} catch (FileNotFoundException | NullPointerException e) {
            		e.printStackTrace();
            		cb.fail(2, "您的更新器版本过于老旧！\n请至官网更新。");
            	} catch (UnknownHostException e) {
            		e.printStackTrace();
					cb.nwoverride();
            	} catch (Exception e) {
            		e.printStackTrace();
					cb.fail(3, e.toString()
					+"\n请检查磁盘和网络连接后重试。如果故障反复出现，请联系管理员。");
				}
            }
        });
        thread.start();
    }
}
