package com.wz.testexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class NumberInfo extends Activity{
	
	String ns1;
	JsonNode root;
	static TrustManager numX509TrustManager=new X509TrustManager(){

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





	//获取电话号码信息
	public  String getNumber(String num){
		
		String nurl="https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel="+num;
		
		
		
		
		String getnum="";
		try {
			SSLContext nsslcontext = SSLContext.getInstance("TLS");
			nsslcontext.init(null, new TrustManager[]{numX509TrustManager}, null);
			URL ngeturl=new URL(nurl);
			 HttpsURLConnection nhttpsConn = (HttpsURLConnection)ngeturl.openConnection();
			 nhttpsConn.setRequestMethod("GET");
			 nhttpsConn.setRequestProperty("Content-Type",
					 "application/javascript;charset=GBK");
			 
			
			 nhttpsConn.setConnectTimeout(30000);
			//设置套接工厂 
		        nhttpsConn.setSSLSocketFactory(nsslcontext.getSocketFactory());
		        nhttpsConn.setReadTimeout(30000);
		        int ncode = nhttpsConn.getResponseCode();
		        if(ncode==200){
		        	
		        	
		        	
		        	
		        	
		        	// 获得响应的输入流对象
					InputStreamReader niStreamReader = new InputStreamReader(
							nhttpsConn.getInputStream(),"GBK");
					BufferedReader nbuffer = new BufferedReader(
							niStreamReader);
					StringBuffer nsBf = new StringBuffer();
					String nline = null;
					 
					
					// 读取服务器返回的信息
					while ((nline = nbuffer.readLine()) != null) {
						nsBf.append(nline);
						
						
						
						
						

					}
					
					
					String jsonS=nsBf.toString().split("=")[1].trim();  //处理=号前的非json字符串
					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);//设置可用单引号
					objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//设置字段可以不用双引号包括
					 root = objectMapper.readTree(jsonS);
					
					
						
					
					 
					
					
					
					
					
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		//Log.i("result","返回来的数据     " +root.path("carrier").asText()+" "+root.path("telString").asText());
		//root.path("catName").asText() + root.path("carrier").asText()
		return root.path("carrier").asText()+" "+root.path("telString").asText();
		
	}
	
	

}