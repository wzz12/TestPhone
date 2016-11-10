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

import org.json.JSONArray;
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
	static TrustManager htX509TrustManager=new X509TrustManager(){

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	
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
	//失败的原因
	String sty="";
	String ssty="";
	String fresu1="";
	//记录成功的
	
	Handler vHandler=new Handler();
	SmsObserver smsObserver;
	public static String  urlPath=null;
	Uri SMS_INBOX = Uri.parse("content://sms");
	public static String codeString=null;
	//NumberInfo是获取电话号码信息的类
	 NumberInfo nin=new NumberInfo();
	
	public String fresu="";
	public String getPath;
	
	//放要Post的数据
	public static String s1,s2,s3,s4,s9,s11,s17,s18,s19,s20=null;
	Long sst,sstf;
	String getToken="";
	//总的，签名字符串
	public static String lzf,qm,cqm,clzf=null;
	String sty1="";
	
	
	Timer mTimer = new Timer();
	
	 public static long ts;
	//"content://sms/inbox"是收件箱
			public void getSmsFromPhone() {
			     
				
			      
		        Cursor cur = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null); 
		        
		        try{
		        	
		        	if (null == cur)
		            	
		        	  return;
		        	//cur.moveToFirst()
		        	
		        	
		            if(cur.moveToNext()) {
		            	
		            	
		                String body = cur.getString(cur.getColumnIndex("body"));
		               
		                //这里我是要获取自己短信服务号码中的验证码，务必记住这里获取的内容需要和projection字符串对应，否则短信数据库可能会找不到内容的。
		                //字母或数字6位
		                Pattern pattern = Pattern.compile("[A-Za-z0-9]{6}");
		               
		                Matcher matcher = pattern.matcher(body);
		                if (matcher.find()) {
		                	
		                   
		                	try{
		                		codeString = matcher.group();
		                	}
		                	catch(Exception e){
		                		 e.printStackTrace();
		                	}
		                	 
		                     Log.i("result","接收到的短信验证码"+codeString);
		                     
		                    
		                    
		                   
		                }
		            }
		          
		        }catch(Exception e){
		        	e.printStackTrace();
		        }finally{
		        	//关闭游标，释放资源
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
     WeixinText.getToken();
   
	postData();
	
	try{
		
	if(TaskActivity.ff==0&&TaskActivity.mb==0){	
		
	setTimerTask();
	
	
	}
	
	
	}catch(Exception e){
		e.printStackTrace();
		Log.i("result","不能定时查询的："+e.toString());
	}
	
	

}








}).start();
 AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
 
//int anHour = 60 * 60 * 1000; // 这是一小时的毫秒数
SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
LongRunningService.s11=sp.getString("tinfo11", "");

long t1;
//判断字符串s11中是否有分钟这两个个字符，若没有则返回-1,就是不含分钟，那就是小时
		if(LongRunningService.s11.indexOf("分钟")==-1){
			String data1="";
			//提取出里面是数字
			if(LongRunningService.s11 != null && !"".equals(LongRunningService.s11)){
				for(int i=0;i<LongRunningService.s11.length();i++){
					if(LongRunningService.s11.charAt(i)>=48 && LongRunningService.s11.charAt(i)<=57){
						data1+=LongRunningService.s11.charAt(i);
					}
				}
			}
			t1=Long.parseLong(data1);
			LongRunningService.ts=t1*60*60*1000;
			
			
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
			
		}
		
		long triggerAtTime =SystemClock.elapsedRealtime()+LongRunningService.ts ;
		


Intent i = new Intent(this, AlarmReceiver.class);

//pi代表闹钟需要执行的动作
PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,0);
//4.4以上才有setExact方法按照准确的时间
manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);


return super.onStartCommand(intent, flags, startId);
}



//创建任务
private String postData() {
	 
	 SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
	 //s9是地址，s1,s2,s3,s4是手机号，密码，姓名，身份证号
		s9=sp.getString("tinfo9", "");
		s1=sp.getString("tinfo1", "");
		s2=sp.getString("tinfo2", "");
		s3=sp.getString("tinfo3", "");
		s4=sp.getString("tinfo4", "");
		
		//s17,s18是appid和appsecret的内容，s19和s20是他们的TextView
		s17=sp.getString("tinfo17", "");
		s18=sp.getString("tinfo18", "");
		s19=sp.getString("tinfo19", "");
		s20=sp.getString("tinfo20", "");
		
		String result="";
		
		//从1970年1月1日到现在的时间秒数
		sst=System.currentTimeMillis()/1000;
		lzf=s18+s19+s17+"time"+sst+s18;
		Log.i("twolog","开始post数据");
		
		try {
			qm=sha1(lzf);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//urlPath是创建任务时的地址
		urlPath=s9+"?"+s19+"="+s17+"&time="+sst+"&signature="+qm;
		
	
	
	try {
		url = new URL(urlPath);
		Log.i("twolog","此时的地址"+urlPath);
		JSONObject cliKey = new JSONObject();
		// 封装Json数据,就是POST要提交的内容，参数直接写在url里
		cliKey.put("type", "mobile");
		cliKey.put("phoneNo",s1 );
		
		
		cliKey.put("password", s2);
		cliKey.put("userName", s3);
		cliKey.put("userID", s4);

		String contentString = String.valueOf(cliKey);
		try {
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();
			SSLContext hpsslcontext = SSLContext.getInstance("TLS");
			hpsslcontext.init(null, new TrustManager[]{htX509TrustManager}, null);
			connection.setRequestMethod("POST");
			// 设置连接网络超时为2秒
			connection.setConnectTimeout(2000);
			//设置套接工厂 
			connection.setSSLSocketFactory(hpsslcontext.getSocketFactory());
			// 设置允许输出到服务器
			// setDoOutput(boolean)参数值为true时决定着当前链接可以进行数据提交工作
			connection.setDoOutput(true);
			
			// setDoInput(boolean)参数值为true决定着当前链接可以进行数据读取
			connection.setDoInput(true);
			
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
			Log.i("twolog","code"+code);
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
					
					TaskActivity.ff=0;
					TaskActivity.mb=0;
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			else {
				//post数据时，数据有错误会到这来
				
				try{
					TaskActivity.succ=0;
					TaskActivity.ff++;
					
					//取消定时
					cancleAlarm();
					
					BufferedReader ssbufferedReader = new BufferedReader(
							new InputStreamReader(
									connection.getErrorStream()));

					String ssreadlineString = null;
					while ((ssreadlineString = ssbufferedReader.readLine()) != null) {
						result += ssreadlineString;
						
					}
					//此号码暂时不可用
					
					fresu=result;
					JSONObject json2=new JSONObject(fresu);
					Log.i("twolog","错误原因 "+fresu);
					 fresu1=json2.getString("error");
					if(TaskActivity.ff==1){
						
						
							
						WeixinText.postText(nin.getNumber(s1)+"\n"+"第一次："+fresu1);
						
						
						
						vHandler.post(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								TaskActivity.ta.setText(fresu1);
							}
							
						});
						
						startAlarm();
						
					}
					else if(TaskActivity.ff!=1&&TaskActivity.ff!=3){
						//大于3次以后就继续查而不报警了
						
						
						vHandler.post(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								TaskActivity.ta.setText(fresu1);
							}
							
						});
						
						startAlarm();
						
					}
					else if(TaskActivity.ff==3){
						//nin.getNumber(s1)是获取的运营商信息
						WeixinText.postText(nin.getNumber(s1)+"\n"+"第三次："+fresu1);
						
						vHandler.post(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								TaskActivity.ta.setText(fresu1);
							}
							
						});
						
						startAlarm();
						
					}
					
					
					
				
					
					ssbufferedReader.close();
					
					connection.disconnect();
					
					
				
				
				
				}catch(Exception e){
					e.printStackTrace();
				}
				
				
			}
				
			

			
		} catch (IOException e) {
			TaskActivity.succ=0;
			TaskActivity.mb++;
			
			
			
			
			
			//取消定时
			cancleAlarm();
			
			
			
			
		
			
			if(TaskActivity.mb==1){
				
				WeixinText.postText(nin.getNumber(s1)+"\n"+"第一次："+"目标服务器不存在或已关机");
				
				//用Handler进行消息的传递
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("目标服务器不存在或已关机");
					}
					
				});
				
			}
			else if(TaskActivity.mb!=1&&TaskActivity.mb!=3){
				//大于3次以后就继续查而不报警了
				
			
				//用Handler进行消息的传递
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("目标服务器不存在或已关机");
					}
					
				});
				
			}
			else if(TaskActivity.mb==3){
				//nin.getNumber(s1)是获取的运营商信息
				WeixinText.postText(nin.getNumber(s1)+"\n"+"第三次："+"目标服务器不存在或已关机");
				
				//用Handler进行消息的传递
				vHandler.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskActivity.ta.setText("目标服务器不存在或已关机");
					}
					
				});
				
			}
			//重新启动这个service
			startAlarm();
			
			
			
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (KeyManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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

public void setTimerTask(){
	
	
	mTimer.schedule(new TimerTask() {
		// 想要定期执行的任务

		@Override
		public void run() {
			
			sstf=System.currentTimeMillis()/1000;
			
			
			clzf=s18+s19+s17+"tid"+tid+"time"+sstf+s18;
			
			
			try {
				cqm=sha1(clzf);
				
				getPath=s9+"?"+"tid="+tid+"&"+s19+"="+s17+"&time="+sstf+"&signature="+cqm;
				
				
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JSONObject jObject = null;
			
			
			
			
			
			try {
				
				URL sUrl = new URL(getPath);
				SSLContext thsslcontext = SSLContext.getInstance("TLS");
				thsslcontext.init(null, new TrustManager[]{htX509TrustManager}, null);
				// 打开网络连接
					HttpsURLConnection conn = (HttpsURLConnection) sUrl
						.openConnection();
				
						
					conn.setConnectTimeout(30000);
					conn.setRequestMethod("GET");
					//设置套接工厂 
					conn.setSSLSocketFactory(thsslcontext.getSocketFactory());
					try{
					Log.i("result","能执行这一句"+conn.getResponseCode());
				}catch(Exception e){
					TaskActivity.succ=0;
					//这个是已经连接上服务器，但服务器繁忙，导致网络连接超时
				TaskActivity.mc++;
					this.cancel();
					//取消定时
					cancleAlarm();
					
					
					
					
					
					
					if(TaskActivity.mc==1){
						
						WeixinText.postText(nin.getNumber(s1)+"\n"+"第一次："+"已经连接上服务器但超时");
						
						vHandler.post(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								//在主线程访问网路要加StrictMode，要更改TextView的内容
								TaskActivity.ta.setText("已经连接上服务器但超时");
							}
							
						});
						
					}
					else if(TaskActivity.mc!=1&&TaskActivity.mc!=3){
						//大于3次以后就继续查而不报警了
						
						
						vHandler.post(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								//在主线程访问网路要加StrictMode，要更改TextView的内容
								TaskActivity.ta.setText("已经连接上服务器但超时");
							}
							
						});
						
					}
					else if(TaskActivity.mc==3){
						//nin.getNumber(s1)是获取的运营商信息
						WeixinText.postText(nin.getNumber(s1)+"\n"+"第三次："+"已经连接上服务器但超时");
						
						vHandler.post(new Runnable(){

							@Override
							public void run() {
								
								TaskActivity.ta.setText("已经连接上服务器但超时");
							}
							
						});
						
					}
					//重新启动这个service
					startAlarm();
					
					
					
					
					
					}
				
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
							

							
							break;
						case "failed":
							 
							//一旦失败succ就置为0
							TaskActivity.succ=0;
							// 如果任务失败，就要重新创建任务查询3到5次看是哪的原因，有的可能是目标服务器网络错误
							
						TaskActivity.count++;
						
							//失败了之后，只是这个任务取消，Timer并没有取消
							this.cancel();
							
							//取消定时
							cancleAlarm();
							
							
							
							JSONObject json2=new JSONObject(getResult);
							sty1=json2.getString("reason");
							if(sty1.indexOf("空号")!=-1){
								//为空号时停止查询
								mTimer.cancel();
								cancleAlarm();
                                WeixinText.postText(nin.getNumber(s1)+"\n"+sty1);
								
								vHandler.post(new Runnable(){
									//空号的时候会失败

									@Override
									public void run() {
										
										
									
										TaskActivity.ta.setText(sty1);
										
										
									}
									
								});
								
							}
							else if(sty1.indexOf("锁")!=-1){
								//被锁
								mTimer.cancel();
								cancleAlarm();
								WeixinText.postText(nin.getNumber(s1)+"\n"+sty1);
								
								vHandler.post(new Runnable(){
									//空号的时候会失败

									@Override
									public void run() {
										
										
									
										TaskActivity.ta.setText(sty1);
										
										
									}
									
								});
							}
							else{
								//不是空号和被锁的情况
								
								if(TaskActivity.count==1){
									
									WeixinText.postText(nin.getNumber(s1)+"\n"+"第一次 :"+sty1);
									
									vHandler.post(new Runnable(){
										//空号的时候会失败

										@Override
										public void run() {
											
											
										
											TaskActivity.ta.setText(sty1);
											
											
										}
										
									});
									
									//报警成功后还有继续查询
									startAlarm();
									
								}
								else if(TaskActivity.count!=1&&TaskActivity.count!=3){
									
								
								vHandler.post(new Runnable(){
									//空号的时候会失败

									@Override
									public void run() {
										
										
									
										TaskActivity.ta.setText(sty1);
										
										
									}
									
								});
								
								
								startAlarm();
								
								}
								else if(TaskActivity.count==3) {
									//连续失败了3次报警，报警成功后再继续查
									
									WeixinText.postText(nin.getNumber(s1)+"\n"+"第3次:"+sty1);
									
									
									vHandler.post(new Runnable(){
										//空号的时候会失败

										@Override
										public void run() {
											
											
										
											TaskActivity.ta.setText(sty1);
											
											
										}
										
									});
									//报警成功后还有继续查询
									startAlarm();
									
								}
								
								
								
								
								
							}
							
							 
							
							
							
							
							 
							  
							
							break;
						

						case "done":
							//一旦成功ff就置为0
							TaskActivity.ff=0;
							TaskActivity.mb=0;
							TaskActivity.mc=0;
							TaskActivity.rq=0;
							//一旦成功将count置为0，为下一次失败做准备
							TaskActivity.count=0;
							String th0=null;
							String th1=null;
							String th2=null;
							String th3=null;
							String th4=null;
							String th5=null;
							//短信记录
							String sm0=null;
							String sm1=null;
							String sm2=null;
							String sm3=null;
							String sm4=null;
							String sm5=null;
							
							
							JSONObject json4=new JSONObject(getResult);
							String res1=json4.getString("result");
							JSONObject json5=new JSONObject(res1);
							
						
							try{
							
							  

				                JSONArray deatailList = json5.getJSONArray("callHistory");
				             
						
							int length = deatailList.length();
							Log.i("result","此时length为"+length);
							
							
							for(int i=0;i<length;i++){
								JSONObject temp = (JSONObject) deatailList.get(i);
								// JSONObject oj = deatailList.getJSONObject(i);
								 
								 	
								 switch(i){
								 case 0:
									 th0 = temp.getString("details");
									
									 Log.i("result","此时th0的值为"+th0);
									break;
								 case 1:
									 th1=temp.getString("details");
									 break;
								 case 2:
									 th2=temp.getString("details");
									 break;
								 case 3:
									 th3=temp.getString("details");
									 break;
								 case 4:
									 th4=temp.getString("details");
									 break;
								 case 5:
									 th5=temp.getString("details");
									 break;
								
									 
								 }
								
								 


							}
							
							
							
							
							
							
							}
							catch(Exception e ){
								Log.i("result","不能执行原因"+e.toString());
							}
							
							try{
								//获取短信记录
								 JSONArray smsList = json5.getJSONArray("smsHistory");
								 int slength = smsList.length();
								 for(int j=0;j<slength;j++){
										JSONObject stemp = (JSONObject) smsList.get(j);
										// JSONObject oj = deatailList.getJSONObject(i);
										 
										 	
										 switch(j){
										 case 0:
											 sm0 = stemp.getString("details");
											
											
											break;
										 case 1:
											 sm1=stemp.getString("details");
											 break;
										 case 2:
											 sm2=stemp.getString("details");
											 break;
										 case 3:
											 sm3=stemp.getString("details");
											 break;
										 case 4:
											 sm4=stemp.getString("details");
											 break;
										 case 5:
											 sm5=stemp.getString("details");
											 break;
										
											 
										 }
										
										 


									}
									
								
							}catch(Exception e){
								e.printStackTrace();
								
							}
							
							
							 Log.i("twolog","第一个月份的短信记录"+sm5);
							 Log.i("twolog","第二个月份的短信记录"+sm4);
							 Log.i("twolog","第三个月份的短信记录"+sm3);
							 Log.i("twolog","第四个月份的短信记录"+sm2);
							 Log.i("twolog","第五个月份的短信记录"+sm1);
							 Log.i("twolog","第六个月份的短信记录"+sm0);
							 //通话记录
							 Log.i("twoult","第一个月份的通话记录"+th5);
							 Log.i("twoult","第二个月份的通话记录"+th4);
							 Log.i("twoult","第三个月份的通话记录"+th3);
							 Log.i("twoult","第四个月份的通话记录"+th2);
							 Log.i("twoult","第五个月份的通话记录"+th1);
							 Log.i("twoult","第六个月份的通话记录"+th0);
							 
							 Log.i("result","判断是否为空"+th0.length()+" ,"+th1.length()+" ,"+th2.length()+" ,"+th3.length()+" ,"+th4.length()+" ,"+th5.length());
							 
							 Log.i("result","判断是否为空"+sm0.length()+" ,"+sm1.length()+" ,"+sm2.length()+" ,"+sm3.length()+" ,"+sm4.length()+" ,"+sm5.length());
						
							
							//判断短信或通话记录是否为空，为空时长度为2
							 if(th0.length()==2&&th1.length()==2&&th2.length()==2&&th3.length()==2&&th4.length()==2&&
									 th5.length()==2&&sm0.length()==2&&sm1.length()==2&&sm2.length()==2&&sm3.length()==2&&sm4.length()==2&&sm5.length()==2){
								 TaskActivity.jl++;
								 if(TaskActivity.jl==1){
									 Log.i("result","第一次：查询出的详单记录都为空");
										WeixinText.postText(nin.getNumber(s1)+"\n"+"第一次：查询出的详单记录都为空");
										 
										 
										 this.cancel();
										 cancleAlarm();
										 vHandler.post(new Runnable(){

												@Override
												public void run() {
													// TODO Auto-generated method stub
													TaskActivity.ta.setText("第一次：查询出的详单记录都为空");
													
												}
												
											});
										 startAlarm();
										 break;
										
									 
								 }
								 
								 
								 else if(TaskActivity.jl==3){
									 
									WeixinText.postText(nin.getNumber(s1)+"\n"+"第三次：查询出的详单记录都为空");
									 
									 
									 this.cancel();
									 cancleAlarm();
									 vHandler.post(new Runnable(){

											@Override
											public void run() {
												// TODO Auto-generated method stub
												TaskActivity.ta.setText("第三次：查询出的详单记录都为空");
												
											}
											
										});
									 
									 startAlarm();
									 
									 break;
									 
									 
								 }
								 else{
									 this.cancel();
									 cancleAlarm();
									 vHandler.post(new Runnable(){

											@Override
											public void run() {
												// TODO Auto-generated method stub
												TaskActivity.ta.setText("超过3次：查询出的详单记录都为空");
												
											}
											
										});
									 startAlarm();
									 
									 break;
									
									 
									
								 }
								 
								 
								
								 
								
								 
								 
							 }
							 
							 
							 
							 
							//一旦失败succ置为0，初始succ=1,所以succ等于0时，在状态为success的里面时，就是刚从不正常到正常
							 else{
								 
								 if(TaskActivity.succ==0||TaskActivity.jl!=0){
										//一旦成功，就将succ置为1，它的初值
										TaskActivity.succ++;
										TaskActivity.jl=0;
									
										WeixinText.postText(nin.getNumber(s1)+"\n"+"恢复正常,查询成功了");
										vHandler.post(new Runnable(){

											@Override
											public void run() {
												// TODO Auto-generated method stub
												TaskActivity.ta.setText("恢复正常,查询成功了");
												
											}
											
										});
										
									}
								 else if(TaskActivity.succ!=0){
									 vHandler.post(new Runnable(){

											@Override
											public void run() {
												// TODO Auto-generated method stub
												TaskActivity.ta.setText("查询成功");
												
											}
											
										});
								 }
								 
								//这次任务销毁
									this.cancel();
									
									break;
								 
								 
							 }
							
							 
							
							
						case "suspended":
							try{
								
								 JSONObject json3=new JSONObject(getResult);
								 ssty=json3.getString("reason");
								 Log.i("twolog","此时ssty的值"+ssty);
								 vHandler.post(new Runnable(){

										@Override
										public void run() {
											// TODO Auto-generated method stub
											TaskActivity.ta.setText(ssty);
											
										}
										
									});
								 
								
								Log.i("result","在suspended内部");
							if(codeString!=null){
								
								
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
								
							}else{
								//不等于-1，就是包含字符串
								
								 if(ssty.indexOf("密码")!=-1){
										mTimer.cancel();
										cancleAlarm();
										//ssty含有密码两个字,就是指密码输错
									WeixinText.postText(nin.getNumber(s1)+"\n"+ssty);
										Log.i("twolog","密码错误时"+ssty+nin.getNumber(s1));
										
										
									}
								 
								 break;
							}
							
							
							
					
							}catch(Exception e){
								e.printStackTrace();
								
							}
							
						
							
							

						}//switch的后一个花括号
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					} 

				} else {
					//这个是定时查询的签名认证失败时
					//取消此次定时查询
					
					try{
						TaskActivity.succ =0;
						TaskActivity.rq++;
						this.cancel();
						//取消定时
						cancleAlarm();
						
						BufferedReader cssbufferedReader = new BufferedReader(
								new InputStreamReader(
										conn.getErrorStream()));

						String cssreadlineString = null;
						while ((cssreadlineString = cssbufferedReader.readLine()) != null) {
							getResult += cssreadlineString;
							
						}
						
						
						cssbufferedReader.close();
						
						conn.disconnect();
					
						
						
						
						if(TaskActivity.rq==1){
							//未提供任务号TID
							//因为当此号码暂时不可用时，我们还在查就不会创建任务，所以也就tid为空了
							
							WeixinText.postText(nin.getNumber(s1)+"\n"+"第一次："+getResult);
							
							
							vHandler.post(new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									TaskActivity.ta.setText(getResult);
								}
								
							});
							
						}
						else if(TaskActivity.rq!=1&&TaskActivity.rq!=3){
							//大于3次以后就继续查而不报警了
							
							
							vHandler.post(new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									TaskActivity.ta.setText(getResult);
								}
								
							});
							
						}
						else if(TaskActivity.rq==3){
							//nin.getNumber(s1)是获取的运营商信息
						
							WeixinText.postText(nin.getNumber(s1)+"\n"+"第三次："+getResult);
							
							vHandler.post(new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									TaskActivity.ta.setText(getResult);
								}
								
							});
							
						}
						
						startAlarm();
						
						
						
						
					
					
					
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (KeyManagementException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
					HttpsURLConnection postConnection = (HttpsURLConnection) pUrl
							.openConnection();
					SSLContext flcontext = SSLContext.getInstance("TLS");
					flcontext.init(null, new TrustManager[]{htX509TrustManager}, null);
					postConnection.setRequestMethod("POST");

					// 设置连接网络超时为2秒
					postConnection.setConnectTimeout(2000);
					postConnection.setSSLSocketFactory(flcontext.getSocketFactory());
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
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (KeyManagementException e) {
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

//启动AlarmReceiver,进而可以重新启动这个service
public void startAlarm(){
	if(TaskActivity.gd==0){
	
		Log.i("twoult","此时GD"+TaskActivity.gd);
	Intent si = new Intent("android.intent.action.ALARMRECEIVER" );
	 sendBroadcast(si); 
	 }else if(TaskActivity.gd != 0){
		 Log.i("twoult","此时GD"+TaskActivity.gd);
			Log.i("twoult","此时调用这个");
	 }
	

	
}


//取消定时的方法
public void cancleAlarm(){
	
	
	//取消定时
	AlarmManager managerc = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	Intent i = new Intent(LongRunningService.this, AlarmReceiver.class);
	PendingIntent pi = PendingIntent.getBroadcast(LongRunningService.this, 0, i,0);
	managerc.cancel(pi);
	
}





}