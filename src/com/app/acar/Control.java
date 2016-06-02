package com.app.acar;

import java.io.IOException;
import android.content.Intent;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class Control extends Activity {
	private TextView shidu,wendu,zhangaiwu,juli,shengminjiance,voiceshow;
	private Button bnfoward,bnback,bnleft,bnright,bstop,tanzhaodeng,voiceshibie;  //小车的前后左右
	private Handler handler;
	private ClientThread clientThread;
	private Boolean isopen =false;
	private MediaPlayer mediaplayer;
	
	public Control() {
		this.mediaplayer = new MediaPlayer();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test1);			
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏代码
		
		initMedia();

		voiceshow = (TextView) findViewById(R.id.voice_order);		

		setButtonAffiars();
		shengminjiance = (TextView) findViewById(R.id.shengmingjiance);
		/**
		 * 更新UI界面控件的数值
		 */
		handler = new Handler() {  
			public void handleMessage(Message msg) { 
				if(msg.what==0x12){
					try {
						String buffer = msg.getData().getString("DATA");
						char[] buff = buffer.toCharArray();
						char[] buff2 = new char[buff.length-1];
						for(int i=0;i<buff.length-1;i++){
							if(buff[i+1]>=48&&buff[i+1]<=58){
								buff2[i] = buff[i+1];
							}else{
								buff2[i] = ' ';
							}
						}
						String buffer2 = String.valueOf(buff2);
						if(buff[0] == 'z'){
							if(buff[1]=='9'){
								zhangaiwu.setText("无障碍物");
							}else{
								zhangaiwu.setText(buffer2+"cm");
							}							
						}else if(buff[0] == 'j'){
							juli.setText(buffer2+"cm");
						}else if(buff[0] == 't'){
							wendu.setText(buffer2+"℃");
							if(buff[1]=='3'){
								Control.this.mediaplayer.start();								
							}else{
								Control.this.mediaplayer.stop();
								try {
									Control.this.mediaplayer.prepare();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}							
							
						}else if(buff[0] == 'r'){
							shidu.setText(buffer2+"%");
						}else if(buff[0] == 'p'){
							shengminjiance.setText("检测到生命");
							shengminjiance.setTextColor(new Color().BLUE);
						}else if(buff[0] == 'n'){
							shengminjiance.setText("没有检测到生命");
							shengminjiance.setTextColor(new Color().WHITE);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}						
				}			
			}  
		}; 
		
		clientThread = new ClientThread(handler);
		new Thread(clientThread).start();
	}
	
	//初始化一个播放器
	public Boolean initMedia(){		
		this.mediaplayer = MediaPlayer.create(this, R.raw.jingbao);
		return true;		
	}

	/**
	 * 配置按钮功能，即初始化
	 */
	private void setButtonAffiars() {
		bnfoward = (Button) findViewById(R.id.forward);
		bnback= (Button) findViewById(R.id.back);
		bnleft= (Button) findViewById(R.id.left);
		bnright= (Button) findViewById(R.id.right);
		bstop = (Button) findViewById(R.id.stop);		
		tanzhaodeng = (Button) findViewById(R.id.tanzhaodeng);		
		shidu = (TextView) findViewById(R.id.shidu);
		wendu = (TextView) findViewById(R.id.wendu);
		zhangaiwu = (TextView) findViewById(R.id.zhangaiwu);
		juli = (TextView) findViewById(R.id.juli);
		voiceshibie = (Button) findViewById(R.id.voiceshibie);
				
		tanzhaodeng.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isopen = !isopen;
				if(isopen){
					ctrOrder("f","开");
					voiceshow.setText("开启探照灯");
					tanzhaodeng.setBackgroundColor(new Color().BLUE);
				}else{
					ctrOrder("h","关");
					voiceshow.setText("关闭探照灯");
					tanzhaodeng.setBackgroundColor(new Color().GREEN);
				}			
			}
		});
		
		bnfoward.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ctrOrder("a", "前");
				voiceshow.setText("小车前进");
			}
		});
		bnback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				voiceshow.setText("小车后退");
				ctrOrder("b", "后");
			}
		});
		bnleft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				voiceshow.setText("小车左转");
				ctrOrder("c", "左");
			}
		});		
		bnright.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				voiceshow.setText("小车右转");
				ctrOrder("d", "右");
			}
		});		
		bstop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				voiceshow.setText("小车停止");
				ctrOrder("e", "停");
			}
		});	
		voiceshibie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Control.this,VoiceCtr.class );
				startActivity(intent);
			}
		});	

	}

	
	//写到Bundler中去
	private void ctrOrder(String orderStr, String tips) {
		try {
			//当用户按下发送按钮时，将用户输入的数据封装成Message
			//然后发送给子线程的handle
			Message msg = new Message();
			msg.what = 0x34;
			Bundle b = new Bundle();   //存放数据
			b.putString("order", orderStr);			
			msg.setData(b);
			clientThread.revhandler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 整个销毁时的处理
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			clientThread.socket.close();
			clientThread.in.close();
			clientThread.printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
