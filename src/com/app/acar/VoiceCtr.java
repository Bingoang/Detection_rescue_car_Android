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
		// ��Intent����������ʶ���ģʽ,���ҿ�������ģʽ 
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		// ����ģʽ��������ʽ������ʶ�� 
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		// ��ʾ���Կ�ʼ
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"��������...");
		try {
			 // ��ʼ����ʶ��  
			startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		} catch (Exception e) {
			Toast t = Toast.makeText(getApplicationContext(),
                    "�����Ҳ��������豸",
                    Toast.LENGTH_SHORT);
			t.show();
		}
		
	}
	
	
	/**
	 * Handle the results from the recognition activity.
	 * ��������ʱ�Ļص�����
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK)
		{
			// Fill the list view with the strings the recognizer thought it
			// could have heard
			// ȡ���������ַ� 
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			
			
			if(matches.get(0).contains("ǰ��")){
				try {
					//���û����·��Ͱ�ťʱ�����û���������ݷ�װ��Message
					//Ȼ���͸����̵߳�handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //�������
					b.putString("order", "a");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(matches.get(0).contains("����")){
				try {
					//���û����·��Ͱ�ťʱ�����û���������ݷ�װ��Message
					//Ȼ���͸����̵߳�handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //�������
					b.putString("order", "b");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
			if(matches.get(0).contains("��ת")){
				try {
					//���û����·��Ͱ�ťʱ�����û���������ݷ�װ��Message
					//Ȼ���͸����̵߳�handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //�������
					b.putString("order", "c");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(matches.get(0).contains("��ת")){
				try {
					//���û����·��Ͱ�ťʱ�����û���������ݷ�װ��Message
					//Ȼ���͸����̵߳�handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //�������
					b.putString("order", "d");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(matches.get(0).contains("ֹͣ")){
				try {
					//���û����·��Ͱ�ťʱ�����û���������ݷ�װ��Message
					//Ȼ���͸����̵߳�handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //�������
					b.putString("order", "e");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(matches.get(0).contains("��")){
				try {
					//���û����·��Ͱ�ťʱ�����û���������ݷ�װ��Message
					//Ȼ���͸����̵߳�handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //�������
					//b.putByte("order", (byte) 'f');
					//b.putByte("order", (byte) 0x66);
					b.putString("order", "f");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
			if(matches.get(0).contains("�ر�")){
				try {
					//���û����·��Ͱ�ťʱ�����û���������ݷ�װ��Message
					//Ȼ���͸����̵߳�handle
					Message msg = new Message();
					msg.what = 0x34;
					Bundle b = new Bundle();   //�������
					//b.putByte("order", (byte) 0x68);
					b.putString("order", "h");			
					msg.setData(b);
					clientThread.revhandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			 // ������ͼ�ĸ���
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
