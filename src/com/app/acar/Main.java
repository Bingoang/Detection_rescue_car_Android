package com.app.acar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @function LoginActivity
 * @author Bear
 * @CreatTime 2015.04.20
 */
public class Main extends Activity {
	private Button connect;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		connect = (Button) findViewById(R.id.connect);		
		
		connect.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this,Control.class );
				startActivity(intent);
				Main.this.finish();
			}
		});
			
		//setMainBtnAffairs();			//≈‰÷√∞¥≈•π¶ƒ‹
		
	}
}
