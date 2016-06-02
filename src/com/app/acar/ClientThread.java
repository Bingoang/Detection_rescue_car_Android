package com.app.acar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ClientThread implements Runnable {
	public Socket socket;
	private Handler handler;		//向UI线程发送消息的Handle对象
	public Handler revhandler;	//接收UI线程发送消息的handle对象
	
	//线程所处理的Socket所对应的输入流
	BufferedReader in = null;	
	PrintWriter  printWriter = null;	// 指令发出 数据缓存	
	
	//构造方法
	public ClientThread(Handler handler){
		this.handler=handler;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket("192.168.8.1", 2001);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			
			printWriter = new PrintWriter(socket.getOutputStream(),true);
			OutputStream out = socket.getOutputStream();
			// 第二个参数是是否自动flush,printWriter方法可以在建立后,用print("XXX")来输出
			printWriter = new PrintWriter(out, true);
			
			//启动一个子线程读取数据
			new Thread(){
				String content = null;
				public void run(){
					//不断读取输入流的数据
					try {
						while((content=in.readLine())!=null){
							//每当读到信息时，发送消息给主线程，页面显示该数据
							Message msg = new Message();
							msg.what=0x12;
							Bundle b = new Bundle();  // 存放String数据
							//Bundle c = new Bundle();  //
							b.putString("DATA", content);
							msg.setData(b);
							handler.sendMessage(msg);							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
			
			//为当前线程初始化Looper
			Looper.prepare();
			//创建revhandler对象，接收UI界面线程的消息
			revhandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					//接收UI线程发送过来的信息
					if(msg.what==0x34){
						//将用户需要发送的信息写入网络
						try {
							Bundle b = msg.getData();
							String buffer = b.getString("order");
							printWriter.print(buffer);
							printWriter.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}						
					}
				}
			};
			//启动Looper
			Looper.loop();			
		}catch(SocketTimeoutException e1){
			System.out.println("网络连接超时");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
