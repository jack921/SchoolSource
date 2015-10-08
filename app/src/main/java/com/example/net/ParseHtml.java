package com.example.net;

import android.util.Log;

import com.example.application.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 2015/6/13.
 */

public class ParseHtml{

    //解析登陆结果
    public static String parseResult(String test){
        int num=test.lastIndexOf("'");
        return test.substring(num-1,num);
    }

    //解析课程
    public static String parseSource(String test){
        //String test="Linux嵌入式系统 II(AND 谭石坚 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17周 [C301])";
        //String test="网页设计基础(YU 王影 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17周 [E103])";
        int end=test.indexOf(" ");
        int start=test.indexOf("(");
        String sourcename=null;

        if(end<start){
            int newEnd=test.indexOf(" ",end+1);
            String source=test.substring(start+1,newEnd);
            int nameEnd=test.indexOf(" ",newEnd+1);
            String name=test.substring(newEnd+1,nameEnd);
            String address=test.substring(test.indexOf("[")+1,test.indexOf("]"));
            sourcename=test.substring(0,end);
            return sourcename+"("+source+name+"["+address+"]"+")";
        }else{
            String source=test.substring(start+1,end);
            int newEnd=test.indexOf(" ",end+1);
            String name=test.substring(end+1,newEnd);
            String address=test.substring(test.indexOf("[")+1,test.indexOf("]"));
            sourcename=test.substring(0,start);
            return sourcename+"("+source+name+"["+address+"]"+")";
        }
    }

    //解析课表html
    public static String[][] parseHtml(String html){
        Document document=null;
        int i=0;
        int j=0;
        String[][] source =new String[9][8];
        try{
            document=Jsoup.parse(html);
            String nbsp=Jsoup.parse("&nbsp;").text().trim();
            Elements elements=document.getElementsByAttributeValue("class","font12");
            for(Element element:elements){
                String temp=element.text().trim();
                if(temp.equals(nbsp)){
                    source[i][j]=null;
                }else{
                    source[i][j]=temp;
                }
                if(j==7){
                    i++;
                    j=0;
                }else{
                    j++;
                }
            }
        }catch(Exception e){
            LogUtil.e("parseError",e.getMessage());
        }
        return source;
    }

    //获取学生基本信息
    public static List<String> parseInfo(String html){
        Document document=null;
        List<String> result=new ArrayList<>();
        try{
            document=Jsoup.parse(html);
            String nbsp=Jsoup.parse("&nbsp;").text().trim();
            Elements elements=document.getElementsByAttributeValue("class","style16");
            for(Element element:elements){
                String temp=element.text().trim();
                if(temp.equals(nbsp)){
                    result.add(null);
                }else{
                    result.add(temp);
                }
            }
            return result;
        }catch(Exception e){
            Log.e("parseInfo",e.getMessage());
            return null;
        }
    }

    //获取名字
    public static String parseName(String temp){
        //String temp="学号: 1240913130 ?姓名: 谢汉杰 ?年级: 2012 ?专业: 电子信息工程(嵌入式应用软件开发)";
        int start=temp.indexOf("名")+3;
        int end=temp.indexOf("年")-2;
        return temp.substring(start, end);
    }

    //获取星期数
    public static String parseWeek(String temp){
        //String temp="教学周: 第17周";
        int start=temp.indexOf("第");
        return temp.substring(start+1,temp.length()-1);
    }

    //判断具体每个星期的情况
    public static boolean decide(String source,int week){
        try{
            int end=source.indexOf("[");
            int result=source.indexOf(String.valueOf(week),-1);

            if(week>=10){
                if(!(source.substring(result-1,result).equals(" ")&&
                        (source.substring(result+2,result+3).equals(" ")
                                ||source.substring(result+2,result+3).equals("周")))){
                    result=-1;
                }
            }else{
                if(!(source.substring(result-1,result).equals(" ")&&
                        (source.substring(result+1,result+2).equals(" ")
                                ||source.substring(result+1,result+2).equals("周")))){
                    result=-1;
                }
            }
            if(result==-1||result>end){
                return false;
            }else{
                return true;
            }
        }catch(Exception e){
            LogUtil.e("parseError",e.getMessage());
            return false;
        }
    }

    //解析课表数据
    public static List<String> parsewidgetSource(String test){
        try{
            List<String> result=new ArrayList<>();
            int nameend=test.indexOf("(");
            int placestart=test.indexOf("[");
            int placeend = test.indexOf("]");
            String name=test.substring(0,nameend);
            String place=test.substring(placestart+1,placeend);
            result.add(name);
            result.add(place);
            return result;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    //选择上课时间
    public static String getSourceTime(String id){
        String time=null;
        switch(id){
            case "1":
                time="09:00 - 10:20";
                break;
            case "2":
                time="10:40 - 12:00";
                break;
            case "3":
                time="12:30 - 13:50";
                break;
            case "4":
                time="14:00 - 15:20";
                break;
            case "5":
                time="15:30 - 16:50";
                break;
            case "6":
                time="17:00 - 18:20";
                break;
            case "7":
                time="19:00 - 20:20";
                break;
            case "8":
                time="20:30 - 21:50";
                break;
        }
        return time;
    }

    //选择上课具体几节
    public static String getSouceAction(String action){

        String source=null;
        switch(action){
            case "1":
                source="1-2";
                break;
            case "2":
                source="3-4";
                break;
            case "3":
                source="5-6";
                break;
            case "4":
                source="7-8";
                break;
            case "5":
                source="9-10";
                break;
            case "6":
                source="11-12";
                break;
            case "7":
                source="13-14";
                break;
            case "8":
                source="15-16";
                break;
        }
        return source;
    }

    //选择上课是星期几
    public static String getSourceDate(String day){
        String date=null;
        switch(day){
            case "1":
                date="星期一";
                break;
            case "2":
                date="星期二";
                break;
            case "3":
                date="星期三";
                break;
            case "4":
                date="星期四";
                break;
            case "5":
                date="星期五";
                break;
        }
        return date;
    }

}
