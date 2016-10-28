package com.wz.testexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class WeixinText extends Activity {
	public static  String getToken="";
	
	static TrustManager myX509TrustManager=new X509TrustManager(){

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
	}
	
	
	//获取Access_Token,调用接口的凭证
	public static  void getToken() {
		// TODO Auto-generated method stub
		String getul=null;
		String gurl="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx19cd4ba1b5e0868a&corpsecret=MG3UtXP_yQuaEKU4IH7wspE4bXwBt6YnFP9UhsoP2IQeT30IFr43ov-0l7URpe6S";
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[]{myX509TrustManager}, null);
		    URL geturl=new URL(gurl);
		    HttpsURLConnection httpsConn = (HttpsURLConnection)geturl.openConnection();
			httpsConn.setRequestMethod("GET");
			httpsConn.setConnectTimeout(30000);
			//设置套接工厂 
	        httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());
	        httpsConn.setReadTimeout(30000);
	        int gcode = httpsConn.getResponseCode();
	        if(gcode==200){
	        	// 获得响应的输入流对象
				InputStreamReader giStreamReader = new InputStreamReader(
						httpsConn.getInputStream());
				BufferedReader gbuffer = new BufferedReader(
						giStreamReader);
				StringBuffer gsBf = new StringBuffer();
				String gline = null;
				// 读取服务器返回的信息
				while ((gline = gbuffer.readLine()) != null) {
					gsBf.append(gline);
					getul=gsBf.toString();
					Log.i("result","此时的Access_Token和有效期"+getul);

				}
				
				Log.i("result","此时的Access_Token和有效期"+getul);
				try {
					JSONObject gjson=new JSONObject(getul);
					getToken=gjson.getString("access_token");
					Log.i("result","取得的access_token为"+getToken);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
	        }
			
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	
	 //发送错误信息给微信企业号
	 public static String postText(String pdata){
		 String pres="";
		 String purl="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
		 purl=purl.replace("ACCESS_TOKEN", getToken);
		 Log.i("result","getToken的值为"+getToken);
			Log.i("result","此时post的地址为"+purl);
			StringBuffer psb = new StringBuffer();
			psb.append("{");
			psb.append("\"touser\":" + "\"" + "@all" + "\",");
			psb.append("\"toparty\":" + "\"" + "@all" + "\",");
			psb.append("\"totag\":" + "\"" + "@all" + "\",");
	 
			psb.append("\"msgtype\":" + "\"" + "text" + "\",");
			psb.append("\"text\":" + "{");
			psb.append("\"content\":" + "\"" + pdata + "\"");
			psb.append("}");
	 
			psb.append(",\"safe\":" + "\"" + "0" + "\",");
			psb.append("\"agentid\":" + "\"" + "2" + "\",");
	       
			psb.append("}");
	        String pjson = psb.toString();
	        Log.i("result","pjson的数据从"+pjson);
	        try {
				URL curls = new URL(purl);
				HttpsURLConnection connec = (HttpsURLConnection)curls.openConnection();
				SSLContext sslcontext = SSLContext.getInstance("TLS");
				sslcontext.init(null, new TrustManager[]{myX509TrustManager}, null);
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	        
		 
		 
		 
		 return null;
	 }

	
	
	
	
	
	

}
