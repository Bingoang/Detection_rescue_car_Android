package com.app.acar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class VoiceCtr extends Activity implements OnClickListener{
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	
	private Handler handler;
	private ClientThread clientThread;
	private Boolean isopen =false;
	
	private ListView mList;
	private Button button;
	private PackageManager pm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.voice);
		//
		button = (Button) findViewById(R.id.yuyinkongzhi);
		mList = (ListView) findViewById(R.id.list1);
		
		pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		
		if (activities.size() != 0)
		{
			button.setOnClickListener(this);
		}
		else
		{
			button.setEnabled(false);
			button.setText("Recognizer not present");
		}
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0x123){
					Bundle b = msg.getData();
					Log.d("TAG", msg.obj.toString());
				}
			}
		};
		
		clientThread = new ClientThread(handler);
		new Thread(clientThread).start();
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.yuyinkongzhi)	
			startVoiceRecognitionActivity();	
	}
	
	

	private void startVoiceRecognitionActivity() {
		// 用Intent来传递语音识别的模式,并且开启语音模式 
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		// 语言模式和自由形式的语音识别 
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		// 提示语言开始
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"语音开启...");
		try {
			 // 开始语音识别  
			startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		} catch (Exception e) {
			Toast t = Toast.makeText(getApplicationContext(),
                    "唉，找不到语音设备",
                    Toast.LENGTH_SHORT);
			t.show();
		}
		
	}
	
	
	/**
	 * Handle the results from the recognition activity.
	 * 语音结束时的回调函数
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK)
		{
			// Fill the list view with the strings the recognizer thought it
			// could have heard
			// 取得语音的字符 
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			
			
			if(matches.get(0).contains("前进")){
				try {
					//当用户按下发送按钮时，将用户输入的数据封装成Message
					//然后发送给子线程的handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //存放数据
					b.putString("order", "a");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(matches.get(0).contains("后退")){
				try {
					//当用户按下发送按钮时，将用户输入的数据封装成Message
					//然后发送给子线程的handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //存放数据
					b.putString("order", "b");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
			if(matches.get(0).contains("左转")){
				try {
					//当用户按下发送按钮时，将用户输入的数据封装成Message
					//然后发送给子线程的handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //存放数据
					b.putString("order", "c");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(matches.get(0).contains("右转")){
				try {
					//当用户按下发送按钮时，将用户输入的数据封装成Message
					//然后发送给子线程的handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //存放数据
					b.putString("order", "d");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(matches.get(0).contains("停止")){
				try {
					//当用户按下发送按钮时，将用户输入的数据封装成Message
					//然后发送给子线程的handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //存放数据
					b.putString("order", "e");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(matches.get(0).contains("打开")){
				try {
					//当用户按下发送按钮时，将用户输入的数据封装成Message
					//然后发送给子线程的handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //存放数据
					//b.putByte("order", (byte) 'f');
					//b.putByte("order", (byte) 0x66);
					b.putString("order", "f");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
			if(matches.get(0).contains("关闭")){
				try {
					//当用户按下发送按钮时，将用户输入的数据封装成Message
					//然后发送给子线程的handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //存放数据
					//b.putByte("order", (byte) 0x68);
					b.putString("order", "h");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			 // 设置视图的更新
			mList.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, matches));
			
		}		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			clientThread.socket.close();
			clientThread.in.close();
			clientThread.printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
