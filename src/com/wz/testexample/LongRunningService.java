package com.wz.testexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class LongRunningService extends Service {
	
	
	//SHA1运算,计算签名
	public static String sha1(String data) throws NoSuchAlgorithmException {
		
			        MessageDigest md = MessageDigest.getInstance("SHA1");
		
			        md.update(data.getBytes());
		
			        StringBuffer buf = new StringBuffer();
		
			        byte[] bits = md.digest();
		
			        for(int i=0;i<bits.length;i++){
		
			            int a = bits[i];
		
			            if(a<0) a+=256;
		
			            if(a<16) buf.append("0");
		
			            buf.append(Integer.toHexString(a));
		
			        }
		
			        return buf.toString();
		
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

	 @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		
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
	
	 NumberInfo nin=new NumberInfo();
	
	public String fresu="";
	public String getPath;
	private PowerManager.WakeLock wakeLock;
	//放要Post的数据
	public static String s1,s2,s3,s4,s9,s10,s11,s17,s18,s19,s20=null;
	Long sst,sstf;
	String getToken="";
	//总的，签名字符串
	public static String lzf,qm,cqm,clzf=null;
	
	
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
	
	 smsObserver = new SmsObserver(smsHandler);
     getContentResolver().registerContentObserver(SMS_INBOX, true,
             smsObserver);
    //获取Access_Token它的有效期为2个小时，重复取时值是一样的
    // WeixinText.getToken();
   
	postData();
	
	try{Log.i("twoult","此时这个号码的电话号码为"+s1);
		Log.i("twoult","此时这个号码的电话信息为"+nin.getNumber(s1));
		
	setTimerTask();}catch(Exception e){
		e.printStackTrace();
		Log.i("result","不能定时查询的："+e.toString());
	}
	
	

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



//创建任务
private String postData() {
	 
	 SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
		s9=sp.getString("tinfo9", "");
		s1=sp.getString("tinfo1", "");
		s2=sp.getString("tinfo2", "");
		s3=sp.getString("tinfo3", "");
		s4=sp.getString("tinfo4", "");
		s10=sp.getString("tinfo10", "");
		//s17,s18是appid和appsecret的内容，s19和s20是他们的TextView
		s17=sp.getString("tinfo17", "");
		s18=sp.getString("tinfo18", "");
		s19=sp.getString("tinfo19", "");
		s20=sp.getString("tinfo20", "");
		
		String result="";
		
		//从1970年1月1日到现在的时间秒数
		sst=System.currentTimeMillis()/1000;
		lzf=s18+s19+s17+"time"+sst+s18;
		Log.i("result","连接后的字符串是"+lzf);
		
		try {
			qm=sha1(lzf);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Log.i("result","运算后的签名是"+qm);
		//urlPath是创建任务时的地址
		urlPath=s9+"?"+s19+"="+s17+"&time="+sst+"&signature="+qm;
		Log.i("result","创建任务的地址为"+urlPath);
	
	
	try {
		url = new URL(urlPath);
		
		JSONObject cliKey = new JSONObject();
		// 封装Json数据,就是POST要提交的内容，参数直接写在url里
		cliKey.put("type", "mobile");
		cliKey.put("phoneNo",s1 );
		
		
		cliKey.put("password", s2);
		cliKey.put("userName", s3);
		cliKey.put("userID", s4);
Log.i("result","已经post好数据了");
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
			int  code = connection.getResponseCode();
			Log.i("result","此时的postData的code为"+code);
			//客户发出的api请求不是200时的各种情况都包括了
			if(code==200){
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
			}
			else {
				Log.i("result","到了postData的400这里");
				try{
					mTimer.cancel();
					//取消定时
					AlarmManager managerc = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
					Intent i = new Intent(LongRunningService.this, AlarmReceiver.class);
					PendingIntent pi = PendingIntent.getBroadcast(LongRunningService.this, 0, i,0);
					managerc.cancel(pi);
					
					BufferedReader ssbufferedReader = new BufferedReader(
							new InputStreamReader(
									connection.getErrorStream()));

					String ssreadlineString = null;
					while ((ssreadlineString = ssbufferedReader.readLine()) != null) {
						result += ssreadlineString;
					}
					Log.i("result","打印出此时的日志"+result);
					fresu=result;
					//nin.getNumber(s1)是获取的运营商信息
					//WeixinText.postText(fresu+"\n"+nin.getNumber(s1));
					
					ssbufferedReader.close();
					
					connection.disconnect();
					
					vHandler.post(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							TaskActivity.ta.setText(fresu);
						}
						
					});
				
				
				
				}catch(Exception e){
					Log.i("result","错误原因410"+e.toString());
				}
				
				
			}
				
			

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("result","到了异常394"+e.toString());
			//e.printStackTrace();
			
			Log.i("result","323行异常出现原因"+e.getMessage());
			mTimer.cancel();
			//取消定时
			AlarmManager managerc = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent(LongRunningService.this, AlarmReceiver.class);
			PendingIntent pi = PendingIntent.getBroadcast(LongRunningService.this, 0, i,0);
			managerc.cancel(pi);
			Log.i("result","目标服务器不存在或已关机");
			//WeixinText.postText("目标服务器不存在或已关机"+"\n"+nin.getNumber(s1));
			
			
			//用Handler进行消息的传递
			vHandler.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					TaskActivity.ta.setText("目标服务器不存在或已关机");
				}
				
			});
			
			
			
			
		}
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.i("result","到了451");
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.i("result","到了455");
	}

	return result;
}

public void setTimerTask(){
	
	
	mTimer.schedule(new TimerTask() {
		// 想要定期执行的任务

		@Override
		public void run() {
			
			sstf=System.currentTimeMillis()/1000;
			
			
			clzf=s18+s19+s17+"tid"+tid+"time"+sst+s18;
			Log.i("result","此时的时间为"+sst);
			
			try {
				cqm=sha1(clzf);
				
				getPath=s9+"?"+"tid="+tid+"&"+s19+"="+s17+"&time="+sst+"&signature="+cqm;
				
				
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JSONObject jObject = null;
			Log.i("result","执行到540");
			
			
			Log.i("result","执行到543");
			
			try {
				Log.i("result","执行到546");
				URL sUrl = new URL(getPath);
				Log.i("result","定时查询的地址为"+getPath);
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
					
					//WeixinText.postText("已经连接上服务器但超时"+"\n"+nin.getNumber(s1));
					
					
					vHandler.post(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							//在主线程访问网路要加StrictMode，要更改TextView的内容
							TaskActivity.ta.setText("已经连接上服务器但超时");
						}
						
					});
					
					
					}
				Log.i("result","执行到581行");
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
						Log.i("result","执行到604行");
						jObject = new JSONObject(getResult);
						statusString = jObject.getString("status");
						Log.i("result","查询出的状态为"+statusString);
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
							

							
							break;
						case "failed":
							// 如果任务失败，就要重新创建任务查询3到5次看是哪的原因，有的可能是目标服务器网络错误
							Log.i("result", "失败");
						count++;
						Log.i("twolog","此时的count的值"+count);
							//失败了之后，只是这个任务取消，Timer并没有取消
							this.cancel();
							//postData();
							//取消定时
							AlarmManager managerc = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
							Intent i = new Intent(LongRunningService.this, AlarmReceiver.class);
							PendingIntent pi = PendingIntent.getBroadcast(LongRunningService.this, 0, i,0);
							managerc.cancel(pi);
							
							Log.i("result","失败的结果显示为"+getResult);
							//提取全部中文
							//Pattern cpattern = Pattern.compile("[\u4E00-\u9FA5]+");
							//小数点可以匹配除换行符号以外的任何符号
							Pattern cpattern = Pattern.compile("[\u4E00-\u9FA5].+");
							
							 final Matcher cmatcher = cpattern.matcher(getResult);
							 
							 
							if(cmatcher.find()){
								vHandler.post(new Runnable(){
									//空号的时候会失败

									@Override
									public void run() {
										String sty=cmatcher.group();
										
									//	WeixinText.postText(sty+"\n"+nin.getNumber(s1));
										// TODO Auto-generated method stub
										TaskActivity.ta.setText(sty);
										Log.i("result","失败原因 ："+sty);
										
									}
									
								});
							}
							
							
							if(count==1){
								Log.i("twolog","此时count的值应该为1实际是："+count);
								//WeixinText.postText(sty+"\n"+"第一次失败"+\n"+nin.getNumber(s1));
								Log.i("twolog","第一次失败报警成功");
								//报警成功后还有继续查询
								startAlarm();
								
							}
							else if(count!=1&&count!=3){
								Log.i("twolog","此时count的值，在这个条件里不希望count的值为1或3实际是："+count);
							Log.i("twolog","开始调用startAlarm");
							startAlarm();
							Log.i("twolog","调用startAlarm结束");
							}
							else if(count==3) {
								//连续失败了3次报警，报警成功后再继续查
								Log.i("twolog","此时count的值应该为3实际是："+count);
								//WeixinText.postText(sty+"\n"+"已经连续失败3次"+\n"+nin.getNumber(s1));
								Log.i("twolog","报警成功");
								//报警成功后还有继续查询
								startAlarm();
								
							}
							
							
							 
							  
							
							break;
						

						case "done":
							if(BuildConfig.DEBUG){
							Log.i("result", "成功");}
							
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
							try{
							
							while(codeString!=null){
								
								
								vHandler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TaskActivity.ta.setText("获取验证码中...");
										
									}
									
								});
								
								Log.i("result","到了while内部");
								flushPostSmsCode();
								if(BuildConfig.DEBUG){
								Log.i("result","已发送好个短信");
								
								
								Log.i("result","验证码="+codeString);
								codeString=null;
								}
								
								break;
								
							}
							
							Log.i("result","在suspended内部");
							//提取全部中文
							Pattern spattern = Pattern.compile("[\u4E00-\u9FA5].+");
							 final Matcher smatcher = spattern.matcher(getResult);
							 if(smatcher.find()){
								 //密码错误时提示
									vHandler.post(new Runnable(){

										@Override
										public void run() {
											String ssty=smatcher.group();
											Log.i("twoult",ssty);
										//	WeixinText.postText(sty+"\n"+nin.getNumber(s1));
											// TODO Auto-generated method stub
											TaskActivity.ta.setText(ssty);
											
											
										}
										
									});
									
									break;
								}
							 
							
							 
							
							
								
								
								
								
								
								
								
							}catch(Exception e){
								e.printStackTrace();
								Log.i("twoult","异常"+e.toString()+"580hang");
							}
							
						
							
							

						}//switch的后一个花括号
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					} 

				} else {
					//这个是定时查询的签名认证失败时
					//取消此次定时查询
					Log.i("result","到了定时查询的400这里");
					try{
						mTimer.cancel();
						//取消定时
						AlarmManager managerc = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						Intent i = new Intent(LongRunningService.this, AlarmReceiver.class);
						PendingIntent pi = PendingIntent.getBroadcast(LongRunningService.this, 0, i,0);
						managerc.cancel(pi);
						
						BufferedReader cssbufferedReader = new BufferedReader(
								new InputStreamReader(
										conn.getErrorStream()));

						String cssreadlineString = null;
						while ((cssreadlineString = cssbufferedReader.readLine()) != null) {
							getResult += cssreadlineString;
							Log.i("result","755行的错误信息"+getResult);
						}
						Log.i("result","打印出此时的日志"+getResult);
						
						cssbufferedReader.close();
						
						conn.disconnect();
					//	WeixinText.postText(getResult+"\n"+nin.getNumber(s1));
						
						vHandler.post(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								TaskActivity.ta.setText(getResult);
							}
							
						});
					
					
					
					}catch(Exception e){
						Log.i("result","错误原因350"+e.toString());
					}
					
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


public void startAlarm(){
	Log.i("twoult","失败之后又执行了这句");
	/*//这几句是启动AlarmReceiver
	AlarmManager smanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	Intent si = new Intent(this, AlarmReceiver.class);
	long striggerAtTime =SystemClock.elapsedRealtime()+LongRunningService.ts ;
	PendingIntent spi = PendingIntent.getBroadcast(this, 0, si,0);
	smanager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, striggerAtTime, spi);
	Log.i("twoult","重新启动service完毕");*/
	
	Intent si = new Intent("android.intent.action.ALARMRECEIVER" );
	 sendBroadcast(si);  
	 Log.i("twoult","重新启动service完毕");

	
}

}
