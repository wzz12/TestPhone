package com.wz.testexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class LongRunningService extends Service {
	
	//发送短信
	private void sendSMS(String phoneNumber, String message) {
		
		// 获取短信管理器
		android.telephony.SmsManager smsManager = android.telephony.SmsManager
				.getDefault();
		// 拆分短信内容（手机短信长度限制）
		List<String> divideContents = smsManager.divideMessage(message);
		for (String text : divideContents) {
			smsManager.sendTextMessage(phoneNumber, null, text, null, null);
			Log.i("result","发送错误短信成功");
		}
	}
	
	 @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		
	}


	//这个类用来监听短信数据库当有新的短信来时会调用onChange方法
	 class SmsObserver extends ContentObserver {
	        public SmsObserver( Handler handler) {
	            super(handler);
	        }
	        @Override
	        public void onChange(boolean selfChange) {
	            super.onChange(selfChange);
	            //每当有新短信到来时，使用我们获取短消息的方法
	            if(BuildConfig.DEBUG){
	            Log.i("result","监听短信数据库有新的短信来");}
	          
	            getSmsFromPhone();
	            
	           
	            
	        }
	    }
	
	URL url;
	String tid = "";
	String getResult = "";
	String statusString = "";
	int count = 0;
	Handler vHandler=new Handler();
	SmsObserver smsObserver;
	public static String  urlPath=null;
	Uri SMS_INBOX = Uri.parse("content://sms");
	public static String codeString=null;
	//放要Post的数据
	public static String s1,s2,s3,s4,s9,s10,s11=null;
	 
	 Timer mTimer = new Timer();
	 
	 public static long ts;
	//"content://sms/inbox"是收件箱
			public void getSmsFromPhone() {
			     
				
			      
		        Cursor cur = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null); 
		        Log.i("result",cur.toString());
		        try{
		        	
		        	if (null == cur)
		            	
		        	  return;
		        	//cur.moveToFirst()
		        	
		        	
		            if(cur.moveToNext()) {
		            	
		            	
		                String body = cur.getString(cur.getColumnIndex("body"));
		                if(BuildConfig.DEBUG){
		                Log.i("result",body);
		               }
		                //这里我是要获取自己短信服务号码中的验证码，务必记住这里获取的内容需要和projection字符串对应，否则短信数据库可能会找不到内容的。
		                Pattern pattern = Pattern.compile("[A-Za-z0-9]{6}");
		               
		                
		                //我获取的验证码是4位阿拉伯数字，正则匹配规则您可能需要修改
		                Matcher matcher = pattern.matcher(body);
		                if (matcher.find()) {
		                	
		                    // codeString = matcher.group();
		                	try{
		                		codeString = matcher.group();
		                	}
		                	catch(Exception e){
		                		 Log.i("result","部执行："+e.getMessage());
		                	}
		                	 
		                     Log.i("result","接收到的短信验证码"+codeString);
		                     
		                    
		                    
		                   
		                }
		            }
		          
		        }catch(Exception e){
		        	e.printStackTrace();
		        }finally{
		        	cur.close();
		        //	codeString=null;
		        }
		        
		    	
		    	
		         
		    }
			
			 public Handler smsHandler = new Handler() {
			        //这里可以进行回调的操作
			       
			    };
			   
			    
			   
	 
	 
	 
@Override
public IBinder onBind(Intent intent) {
return null;
}
@SuppressLint("NewApi") @Override
public int onStartCommand(Intent intent, int flags, int startId) {
new Thread(new Runnable() {
@Override
public void run() {
	//acquireWakeLock();
	 smsObserver = new SmsObserver(smsHandler);
     getContentResolver().registerContentObserver(SMS_INBOX, true,
             smsObserver);
    
	postData();
	try{
	setTimerTask();}catch(Exception e){
		e.printStackTrace();
		Log.i("result","不能定时查询的："+e.toString());
	}
	
	

}

private String postData() {
	
	 SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
		s9=sp.getString("tinfo9", "");
		s1=sp.getString("tinfo1", "");
		s2=sp.getString("tinfo2", "");
		s3=sp.getString("tinfo3", "");
		s4=sp.getString("tinfo4", "");
		s10=sp.getString("tinfo10", "");
		
		urlPath=s9;
		
		
		
	// TODO Auto-generated method stub
	String result = "";
	
	
	try {
		url = new URL(urlPath);
		
		JSONObject cliKey = new JSONObject();
		// 封装Json数据,就是POST要提交的内容，参数直接写在url里
		cliKey.put("type", "mobile");
		cliKey.put("phoneNo",s1 );
		
		
		cliKey.put("password", s2);
		cliKey.put("userName", s3);
		cliKey.put("userID", s4);

		String contentString = String.valueOf(cliKey);
		try {
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// 设置连接网络超时为2秒
			connection.setConnectTimeout(2000);
			// 设置允许输出到服务器
			// setDoOutput(boolean)参数值为true时决定着当前链接可以进行数据提交工作
			connection.setDoOutput(true);
			
			// setDoInput(boolean)参数值为true决定着当前链接可以进行数据读取
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			// 内容提交码
			connection.setRequestProperty("Content-Type",
					"application/json;charset=UTF-8");
			connection.connect();
			// connection.getOutputStream()获取数据流从而进行数据写操作，为将数据提交到服务器作准备
			OutputStream osOutputStream = connection.getOutputStream();
			// 将数据写到服务器
			osOutputStream.write(contentString.getBytes());
			
			osOutputStream.flush();
			// 执行完osOutputStream.close()之后，POST请求结束

			osOutputStream.close();

			// 服务器返回的响应码
			int code = connection.getResponseCode();
			//客户发出的api请求不是200时的各种情况都包括了
			switch(code){
			case 200:
				// 我们就可以接收服务器返回来的数据了

				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(
								connection.getInputStream()));

				String readlineString = null;
				while ((readlineString = bufferedReader.readLine()) != null) {
					result += readlineString;
				}
				bufferedReader.close();

				connection.disconnect();
				if(BuildConfig.DEBUG){

				Log.i("testlog", result);

				Log.i("testlog", "数据传递成功");}
				// 从json字符串里取出某个数据

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(result);
					tid = jsonObject.getString("tid");
					// 打印它的tid
					
					String status = jsonObject.getString("status");
					if(BuildConfig.DEBUG){
					Log.i("testlog", "tid=" + tid);
					// 打印此时的状态
					Log.i("testlog", "status=" + status);}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 1000:
				//sendSMS(s10, "未提供appid");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("未提供appid");
					}
					
				});
				break;
			case 1001:
				//sendSMS(s10, "非法的appid");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("非法的appid");
					}
					
				});
				break;
			case 1002:
				//sendSMS(s10, "未提供签名认证");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("未提供签名认证");
					}
					
				});
				break;
			case 1003:
				//sendSMS(s10, "签名验证失败");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("签名验证失败");
					}
					
				});
				break;
			case 1004:
				//sendSMS(s10, "时间参数错误");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("时间参数错误");
					}
					
				});
				break;
			case 1005:
				//sendSMS(s10, "账户锁定，无法访问");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("账户锁定，无法访问");
					}
					
				});
				break;
			case 1006:
				//sendSMS(s10, "账户锁定，无法访问");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("账户锁定，无法访问");
					}
					
				});
				break;
			case 1007:
				//sendSMS(s10, "电话号码不正确");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("电话号码不正确");
					}
					
				});
				break;
			case 1008:
				//sendSMS(s10, "未提供客服密码");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("未提供客服密码");
					}
					
				});
				break;
			case 1009:
				//sendSMS(s10, "未提供机主姓名");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("未提供机主姓名");
					}
					
				});
				break;
			case 1010:
				//sendSMS(s10, "未提供机主身份证");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("未提供机主身份证");
					}
					
				});
				break;
			case 1011:
				//sendSMS(s10, "身份证号码格式错误");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("身份证号码格式错误");
					}
					
				});
				break;
			case 1012:
				//sendSMS(s10, "IP被禁止访问");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("IP被禁止访问");
					}
					
				});
				break;
			case 1013:
				//sendSMS(s10, "未提供任务号（TID）");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("未提供任务号（TID）");
					}
					
				});
				break;
			case 1014:
				//sendSMS(s10, "无此任务");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("无此任务");
					}
					
				});
				break;
			case 1015:
				//sendSMS(s10, "此任务处于非可执行状态");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("此任务处于非可执行状态");
					}
					
				});
				break;
			case 1016:
				//sendSMS(s10, "需要任务类型或TID才能执行");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("需要任务类型或TID才能执行");
					}
					
				});
				break;
			case 1017:
				//sendSMS(s10, "不支持此任务类型");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("不支持此任务类型");
					}
					
				});
				break;
			case 1018:
				//sendSMS(s10, "此号码暂时不可用");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("此号码暂时不可用");
					}
					
				});
				break;
			case 5000:
				//sendSMS(s10, "内部错误");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("内部错误");
					}
					
				});
				break;
			case 9000:
				//sendSMS(s10, "不支持该请求");
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("不支持该请求");
					}
					
				});
				break;



			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("result","323行异常出现原因"+e.getMessage());
			mTimer.cancel();
			//取消定时
			AlarmManager managerc = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent(LongRunningService.this, AlarmReceiver.class);
			PendingIntent pi = PendingIntent.getBroadcast(LongRunningService.this, 0, i,0);
			managerc.cancel(pi);
			Log.i("result","目标服务器不存在或已关机");
			//sendSMS(s10, "目标服务器不存在或已关机");
			//用Handler进行消息的传递
			vHandler.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					TaskActivity.ta.setText("查询出现错误，目标服务器不存在或已关机");
				}
				
			});
			
			
			
		}
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return result;
}
}).start();
 AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

//int anHour = 60 * 60 * 1000; // 这是一小时的毫秒数
SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
LongRunningService.s11=sp.getString("tinfo11", "");
Log.i("result","s11的值为"+LongRunningService.s11);
long t1;
//判断字符串s11中是否有分钟这两个个字符，若没有则返回-1,就是不含分钟，那就是小时
		if(LongRunningService.s11.indexOf("分钟")==-1){
			String data1="";
			if(LongRunningService.s11 != null && !"".equals(LongRunningService.s11)){
				for(int i=0;i<LongRunningService.s11.length();i++){
					if(LongRunningService.s11.charAt(i)>=48 && LongRunningService.s11.charAt(i)<=57){
						data1+=LongRunningService.s11.charAt(i);
					}
				}
			}
			t1=Long.parseLong(data1);
			LongRunningService.ts=t1*60*60*1000;
			if(BuildConfig.DEBUG){
			Log.i("result","截取到的时间是data1"+data1);
			Log.i("result","394行的时间"+LongRunningService.ts);
			}
			
		}
		else{
			String data2="";
			if(LongRunningService.s11 != null && !"".equals(LongRunningService.s11)){
				for(int i=0;i<LongRunningService.s11.length();i++){
					if(LongRunningService.s11.charAt(i)>=48 && LongRunningService.s11.charAt(i)<=57){
						data2+=LongRunningService.s11.charAt(i);
					}
				}
			}
			t1=Long.parseLong(data2);
			LongRunningService.ts=t1*60*1000;
			if(BuildConfig.DEBUG){
			Log.i("result","截取到的时间是data2:"+data2);
			Log.i("result","411行的时间"+LongRunningService.ts);
			}
		}
		
		long triggerAtTime =SystemClock.elapsedRealtime()+LongRunningService.ts ;
		Log.i("result","此时的定时时间为"+triggerAtTime);


Intent i = new Intent(this, AlarmReceiver.class);
//i.setAction("repeating");
//pi代表闹钟需要执行的动作
PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,0);
//4.4以上才有setExact方法按照准确的时间
manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
//manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10*1000*60, pi);
Log.i("result","两次相隔时间424行"+LongRunningService.ts);

return super.onStartCommand(intent, flags, startId);
}

public void setTimerTask(){
	
	
	mTimer.schedule(new TimerTask() {
		// 想要定期执行的任务

		@Override
		public void run() {
			String getPath = urlPath + "&tid=" + tid;
			JSONObject jObject = null;
			
			
			
			try {
				URL sUrl = new URL(getPath);
				// 打开网络连接
				HttpURLConnection conn = (HttpURLConnection) sUrl
						.openConnection();
				conn.setConnectTimeout(30000);
				try{
					Log.i("result","能执行这一句"+conn.getResponseCode());
				}catch(Exception e){
					//这个是已经连接上服务器，但服务器繁忙，导致网络连接超时
				
					mTimer.cancel();
					//取消定时
					AlarmManager managerc = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
					Intent i = new Intent(LongRunningService.this, AlarmReceiver.class);
					PendingIntent pi = PendingIntent.getBroadcast(LongRunningService.this, 0, i,0);
					managerc.cancel(pi);
					
					//sendSMS(s10, "已经连接上服务器但超时");
					vHandler.post(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							TaskActivity.ta.setText("已经连接上服务器但超时");
						}
						
					});
					
					
					}
				Log.i("result","执行到451行");
				// 读取数据之前获取当前服务器返回的响应码
				int getCodeString = conn.getResponseCode();
				if (getCodeString == 200) {
					// 获得响应的输入流对象
					InputStreamReader iStreamReader = new InputStreamReader(
							conn.getInputStream());
					BufferedReader buffer = new BufferedReader(
							iStreamReader);
					StringBuffer sBf = new StringBuffer();
					String line = null;
					// 读取服务器返回的信息
					while ((line = buffer.readLine()) != null) {
						sBf.append(line);

					}
					getResult = sBf.toString();
					// 关闭InputStream,关闭http连接
					iStreamReader.close();
					conn.disconnect();
					if(BuildConfig.DEBUG){
					Log.i("testlog", getResult);}
					try {
						Log.i("result","执行到475行");
						jObject = new JSONObject(getResult);
						statusString = jObject.getString("status");
						switch (statusString) {
						
						case "processing":
							//用Handler进行消息的传递
							vHandler.post(new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									TaskActivity.ta.setText("正在查询...");
								}
								
							});
							

							count = 0;
							break;
						case "failed":
							// 如果任务失败，就要重新创建任务查询3到5次看是哪的原因
							Log.i("result", "失败");
							count++;
							//当查询错误时的各种错误提示
							switch(getCodeString){
							case 2000:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("账号暂时锁定，请稍后再试");
									}
									
								});
								break;
							case 2001:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("号码是空号");
									}
									
								});
								break;
							case 3000:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("目标服务器发送短信随机码失败");
									}
									
								});
								break;
							case 3001:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("短信随机码发送太多，请稍后再试");
									}
									
								});
								break;
							case 3002:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("短信随机码验证失败");
									}
									
								});
								break;
							case 4000:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("验证用户信息失败");
									}
									
								});
								break;
							case 4001:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("非实名认证用户，目标服务器不提供查询");
									}
									
								});
								break;
							case 5000:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("目标服务器网络超时");
									}
									
								});
								break;
							case 5001:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("目标服务器网络错误");
									}
									
								});
								break;
							case 6000:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("目标服务器繁忙");
									}
									
								});
								break;
							case 6001:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("目标服务器错误");
									}
									
								});
								break;
							case 9999:
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("未知错误");
									}
									
								});
								break;
							}
							if (count == 5) {

								mTimer.cancel();
								//取消定时
								AlarmManager managerc = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
								Intent i = new Intent(LongRunningService.this, AlarmReceiver.class);
								PendingIntent pi = PendingIntent.getBroadcast(LongRunningService.this, 0, i,0);
								managerc.cancel(pi);
								
								//sendSMS(s1, s1+"查询出现了错误");
								
							} else {

								break;
							}

						case "done":
							if(BuildConfig.DEBUG){
							Log.i("result", "成功");}
							count = 0;
							vHandler.post(new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									TaskActivity.ta.setText("查询成功");
									
								}
								
							});
							//这次任务销毁
							this.cancel();
							
							break;
						case "suspended":
							
							
							Log.i("result","在suspended内部");
							
							try{
								
								count = 0;
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("获取验证码中...");
										
									}
									
								});
								
								while(codeString!=null){
									
									Log.i("result","到了while内部");
									flushPostSmsCode();
									if(BuildConfig.DEBUG){
									Log.i("result","已发送好个短信");
									
									
									Log.i("result","验证码="+codeString);
									codeString=null;
									}
									
									break;
									
								}
								
								
								
							}catch(Exception e){
								e.printStackTrace();
								Log.i("twoult","异常"+e.toString()+"580hang");
							}
							
							

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					} 

				} else {
					vHandler.post(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							//TaskActivity.ta.setText("请求有错");
							Log.i("result","请求有错");
						}
						
					});
					
					this.cancel();
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		private String flushPostSmsCode() {
			// TODO Auto-generated method stub
			String postPathString = urlPath;
			String postResulString = "";
			try {
				URL pUrl = new URL(postPathString);
				// 将数据封装在jso里
				JSONObject jso = new JSONObject();
				jso.put("smsCode", codeString);
				jso.put("userName", s3);
				jso.put("userID", s4);
				jso.put("tid", tid);
				String jsoString = String.valueOf(jso);
				try {
					HttpURLConnection postConnection = (HttpURLConnection) pUrl
							.openConnection();

					// 设置连接网络超时为2秒
					postConnection.setConnectTimeout(2000);
					// 设置允许输出到服务器
					// setDoOutput(boolean)参数值为true时决定着当前链接可以进行数据提交工作
					postConnection.setDoOutput(true);
					// setDoInput(boolean)参数值为true决定着当前链接可以进行数据读取
					postConnection.setDoInput(true);
					postConnection.setRequestMethod("POST");
					// 内容提交码
					postConnection.setRequestProperty("Content-Type",
							"application/json");
					postConnection.connect();
					// connection.getOutputStream()获取数据流从而进行数据写操作，为将数据提交到服务器作准备
					OutputStream outputStream = postConnection
							.getOutputStream();
					// 将数据写到服务器
					outputStream.write(jsoString.getBytes());
					if(BuildConfig.DEBUG){
					Log.i("result","将验证码写到了服务器上");}
					outputStream.flush();
					if(BuildConfig.DEBUG){
					// 执行完osOutputStream.close()之后，POST请求结束
					Log.i("result","写好了关闭");}

					outputStream.close();
					// 服务器返回的响应码
					int reCode = postConnection.getResponseCode();
					if (reCode == 200) {
						// 我们就可以接收服务器返回来的数据了

						BufferedReader postBufferedReader = new BufferedReader(
								new InputStreamReader(
										postConnection.getInputStream()));
						String postReadlineString = null;
						while ((postReadlineString = postBufferedReader
								.readLine()) != null) {
							postResulString += postReadlineString;
							Log.i("result","接收服务器返回俩的数据");
						}
						postBufferedReader.close();
						Log.i("result","发送好短信给服务器了");
						Log.i("twoult", "发送短信给服务器成功"+postResulString);

						postConnection.disconnect();
						

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (MalformedURLException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return postResulString;

			
		}

		
	}, 4000, 9000);// 表示4秒以后，每隔10秒执行一次，知道Timer被cancle()
}
}

