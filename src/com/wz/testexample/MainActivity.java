package com.wz.testexample;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button bts=null;
	Button btr=null;
	public static  TextView st=null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bts = (Button) findViewById(R.id.bts);
		btr = (Button) findViewById(R.id.btr);
		st=(TextView) findViewById(R.id.s);
		
		// 设置按钮的点击事件
		bts.setOnClickListener(new View.OnClickListener() {

			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 由主界面跳到输入信息的界面
				Intent i = new Intent(MainActivity.this,InfoActivity.class);
				startActivity(i);
				//当点击设置按钮跳转到另一个页面时也关闭了当前页面
				//finish();
				

			}
		});
btr.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				Intent tin=new Intent(MainActivity.this,TaskActivity.class);
				
				startActivity(tin);
				//杀死了这个页面
				//finish();
				
			
				
				
			}
		});
		

	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	
	
	





	

}
