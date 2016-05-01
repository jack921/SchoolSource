package com.example.net;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;
import com.example.application.LogUtil;
import com.example.application.SourceApplication;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jack on 2015/6/13.
 */

public class NetWork {

    public static String logincheck="http://class.sise.com.cn:7001/sise/login_check.jsp";
    public static String sourcehost="http://class.sise.com.cn:7001/sise/module/student_schedular/student_schedular.jsp";
    public static String loginhost="http://class.sise.com.cn:7001/sise/login.jsp";

    //获取隐藏的input
    public static List<Map<String,String>> getCode(){
                try{
                    Document doc = Jsoup.connect(loginhost).get();
                    List<Map<String,String>> param=new ArrayList<Map<String,String>>();
                    Map<String,String> map=null;
                    Elements ListInput=doc.getElementsByAttributeValue("type","hidden");
                    for(Element element:ListInput){
                        map=new HashMap<String, String>();
                        map.put("name",element.attr("name"));
                        map.put("value",element.attr("value"));
                        param.add(map);
                    }
                    return param;
                    }catch(Exception e) {
                    return null;
                }
    }

    //登陆
    public static String login(final String username,final String password,final List<Map<String,String>> param){
        DefaultHttpClient httpClient=new DefaultHttpClient();
        HttpPost httpPost=new HttpPost(logincheck);
        List<NameValuePair> params=new ArrayList<>();
        params.add(new BasicNameValuePair("username",username));
        params.add(new BasicNameValuePair("password",password));
        for(int i=0;i<param.size()-1;i++){
            params.add(new BasicNameValuePair(param.get(i).get("name"),param.get(i).get("value")));
        }
        try {
            UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse=httpClient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()==200) {
                CookieStore cookieStore = httpClient.getCookieStore();
                List<Cookie> cookies = cookieStore.getCookies();
                for (int i = 0; i < cookies.size(); i++) {
                    if ("JSESSIONID".equals(cookies.get(i).getName())) {
                        SharedPreferences.Editor editor=SourceApplication.getContext().getSharedPreferences
                                ("data", SourceApplication.getContext().MODE_PRIVATE).edit();
                        editor.putString("sessionid",cookies.get(i).getValue());
                        editor.commit();
                    }
                }
                HttpEntity entity1=httpResponse.getEntity();
                String test=EntityUtils.toString(entity1);
                //String result=ParseHtml.parseResult(EntityUtils.toString(entity1));
                return "p";
            }else{
                return null;
            }
        } catch (Exception e) {
            LogUtil.e("error", e.getMessage());
            return null;
        }
    }

    //获取课表
    public static String GetSource() {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(sourcehost);
        try {
            httpGet.setHeader("Cookie", "JSESSIONID=" + getSessionId());
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                //String content =
                return EntityUtils.toString(entity, "gb2312");
                //return ParseHtml.parseHtml(content);
            } else {
               return null;
            }
        } catch (Exception e) {
            LogUtil.e("error", e.getMessage());
            return null;
        }
    }

    public void getUserInfo(){

    }

    public static String getSessionId(){
        SharedPreferences preferences=SourceApplication.getContext().getSharedPreferences("data",
                SourceApplication.getContext().MODE_PRIVATE);
        return preferences.getString("sessionid","");
    }

}
