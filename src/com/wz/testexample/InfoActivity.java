package com.wz.testexample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class InfoActivity extends Activity {
	String phNum, phPsw, name, indeNum, eadd,
			tb = null;
	EditText etphnum, etphpsw, etname, etidennum, 
			 etad ,tap,tas= null;
	TextView tvappid,tvappsecret;
	
	Button bt = null;
	public static String selectname,data = null;
	public static int info13;
	private Spinner spinner;
	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);
		spinner = (Spinner) findViewById(R.id.spinner);
		// 数据
		data_list = new ArrayList<String>();
		
		data_list.add("5分钟");
		data_list.add("10分钟");
		
		data_list.add("15分钟");
		data_list.add("20分钟");
		data_list.add("25分钟");
		data_list.add("30分钟");
		data_list.add("40分钟");
		data_list.add("1小时");
		data_list.add("2小时");
		data_list.add("3小时");
		//这个SharedPreferences是保存Spinner选择选项后的
		SharedPreferences settings;
		 final Editor editorsettings;
		 settings=getSharedPreferences("preferences_settings", MODE_PRIVATE);
		 editorsettings=settings.edit();
		// 适配器
		arr_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data_list);
		// 设置样式
		arr_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 加载适配器
		spinner.setAdapter(arr_adapter);
		bt = (Button) findViewById(R.id.bt);
		
		initial();
		getEditText();
	
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
			//position是选择的下拉列表所在位置
				 data = (String)spinner.getItemAtPosition(position);
				
				//从spinner中获取被选择的数据
				
				 arg0.setVisibility(View.VISIBLE);
				 //点击对应位置时的值：arr_adapter.getItem(position);
				 
				 //保存点击item后的对应内容
				 selectname=arg0.getItemAtPosition(position).toString();
				 editorsettings.putInt("SelectedPosition", position);
				 editorsettings.putString("SelectedName", selectname);
				 editorsettings.commit();
				
				
				
			}
			//未选中下拉项（即空项）触发的事件

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				arg0.setVisibility(View.VISIBLE);
			}
			
		});
		

		// 保存按钮的点击事件
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 取得输入框中的信息
				phNum = etphnum.getText().toString();
				phPsw = etphpsw.getText().toString();
				name = etname.getText().toString();
				indeNum = etidennum.getText().toString();
				
				eadd = etad.getText().toString();
				
				// TODO Auto-generated method stub

				// SharedPreferences用来保存EditText输入框中输入的信息

				SharedPreferences userInfo = getSharedPreferences("user_info",
						0);

				userInfo.edit()
						.putString("tinfo1", etphnum.getText().toString())
						.commit();
				userInfo.edit()
						.putString("tinfo2", etphpsw.getText().toString())
						.commit();

				
				userInfo.edit()
						.putString("tinfo3", etname.getText().toString())
						.commit();
				userInfo.edit()
						.putString("tinfo4", etidennum.getText().toString())
						.commit();
				
				userInfo.edit().putString("tinfo9", etad.getText().toString())
						.commit();
				
				//存选中的时间
				userInfo.edit().putString("tinfo11", data).commit();
				
				//添加appid和appsecret
				userInfo.edit().putString("tinfo17", tap.getText().toString())
				.commit();
				userInfo.edit().putString("tinfo18", tas.getText().toString())
				.commit();
				//添加保存appid和appsecret的TextView的值
				userInfo.edit().putString("tinfo19", tvappid.getText().toString())
				.commit();
				userInfo.edit().putString("tinfo20", tvappsecret.getText().toString())
				.commit();
				
				Intent intent = new Intent(InfoActivity.this,
						MainActivity.class);

				startActivity(intent);
			}
		});
		
	
		

	}

	private void getEditText() {
		// TODO Auto-generated method stub
		//保存输入的用户信息
		SharedPreferences userInfo = getSharedPreferences("user_info", 0);

		String info1 = userInfo.getString("tinfo1", "");
		String info2 = userInfo.getString("tinfo2", "");

		String info3 = userInfo.getString("tinfo3", "");
		String info4 = userInfo.getString("tinfo4", "");
		
		String info9 = userInfo.getString("tinfo9", "");
		String info10 = userInfo.getString("tinfo10", "");
		String info17 = userInfo.getString("tinfo17", "");
		String info18 = userInfo.getString("tinfo18", "");
		etphnum.setText(info1);
		etphpsw.setText(info2);

		etname.setText(info3);
		etidennum.setText(info4);
		
		etad.setText(info9);
		
		tap.setText(info17);
		tas.setText(info18);
		//当界面跳转后能更新已保存的Spinner的选项
		SharedPreferences settings = getSharedPreferences("preferences_settings",0); 
		String name = settings.getString("SelectedName", ""); 
		int position = settings.getInt("SelectedPosition", 0 );
		spinner.setSelection(position);

		
	}

	private void initial() {
		// TODO Auto-generated method stub
		etphnum = (EditText) findViewById(R.id.phN);
		etphpsw = (EditText) findViewById(R.id.phPas);
		etname = (EditText) findViewById(R.id.etName);
		etidennum = (EditText) findViewById(R.id.etid);
		
		etad = (EditText) findViewById(R.id.etadd);
		
		tap = (EditText) findViewById(R.id.appid);
		tas = (EditText) findViewById(R.id.tsap);
		tvappid=(TextView) findViewById(R.id.tap);
		tvappsecret=(TextView) findViewById(R.id.tse);
		

	}

	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Intent ii=new Intent(InfoActivity.this,MainActivity.class);
		startActivity(ii);
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		
	}
	
	
	

}