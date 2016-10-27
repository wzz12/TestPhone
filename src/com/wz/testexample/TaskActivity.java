package com.wz.testexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class TaskActivity extends Activity {
	public static TextView ta;
	 
	 private PowerManager.WakeLock wakeLock; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);
		ta=(TextView) findViewById(R.id.ta);
		//启动服务
		
		Intent intent = new Intent(this, LongRunningService.class);
		startService(intent);
		
		
	}
	//这里写测试用的程序
	
	


	




	//调用返回键
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		//杀死当前进程
		Intent sstint=new Intent(TaskActivity.this,LongRunningService.class);
		stopService(sstint);
		//取消定时
		AlarmManager managerc = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i,0);
		managerc.cancel(pi);
		
		
		android.os.Process.killProcess(android.os.Process.myPid());
		
	}




	 
	
	}