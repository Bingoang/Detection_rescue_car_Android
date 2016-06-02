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
	private Handler handler;		//��UI�̷߳�����Ϣ��Handle����
	public Handler revhandler;	//����UI�̷߳�����Ϣ��handle����
	
	//�߳��������Socket����Ӧ��������
	BufferedReader in = null;	
	PrintWriter  printWriter = null;	// ָ��� ���ݻ���	
	
	//���췽��
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
			// �ڶ����������Ƿ��Զ�flush,printWriter���������ڽ�����,��print("XXX")�����
			printWriter = new PrintWriter(out, true);
			
			//����һ�����̶߳�ȡ����
			new Thread(){
				String content = null;
				public void run(){
					//���϶�ȡ������������
					try {
						while((content=in.readLine())!=null){
							//ÿ��������Ϣʱ��������Ϣ�����̣߳�ҳ����ʾ������
							Message msg = new Message();
							msg.what=0x12;
							Bundle b = new Bundle();  // ���String����
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
			
			//Ϊ��ǰ�̳߳�ʼ��Looper
			Looper.prepare();
			//����revhandler���󣬽���UI�����̵߳���Ϣ
			revhandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					//����UI�̷߳��͹�������Ϣ
					if(msg.what==0x34){
						//���û���Ҫ���͵���Ϣд������
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
			//����Looper
			Looper.loop();			
		}catch(SocketTimeoutException e1){
			System.out.println("�������ӳ�ʱ");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
