package com.example.jack.schoolsource;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import com.example.DateUtil.CalendarUtil;
import com.example.dao.DaoImpl;
import com.example.net.ParseHtml;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jack on 2015/6/22.
 */
public class SourceDesktopShow extends AppWidgetProvider{

    private static int weekday;
    private static Map<String,String> daySource=null;
    private static int showCursor;
    private static List<String> keys=null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appshow);
        //上一节
        Intent intent = new Intent("android.jack.wedgitprev");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.prev, pendingIntent);
        //下一节
        Intent intent2=new Intent("android.jack.wedgitnext");
        PendingIntent pendingIntent2=PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.next,pendingIntent2);
        //前一天
        Intent intent3=new Intent("android.jack.wedgitleft");
        PendingIntent pendingIntent3=PendingIntent.getBroadcast(context, 0, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.left,pendingIntent3);
        //后一天
        Intent intent4=new Intent("android.jack.wedgitright");
        PendingIntent pendingIntent4=PendingIntent.getBroadcast(context, 0, intent4, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.right,pendingIntent4);
        //更新今天的数据
        Intent intent5=new Intent();
        intent5.setAction("android.appwidget.update");
        context.sendBroadcast(intent5);
        //更新appwidget
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch(intent.getAction()){
            case "android.appwidget.update":
                DayUpdate(context);
                break;
            case "android.jack.wedgitprev":
                prev(context);
                break;
            case "android.jack.wedgitnext":
                next(context);
                break;
            case "android.jack.wedgitleft":
                left(context);
                break;
            case "android.jack.wedgitright":
                right(context);
                break;
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context,appWidgetIds);
    }

    //更新每天的source
    public void DayUpdate(Context context){
        weekday=CalendarUtil.getDayOfWeek()-1;
        if(weekday==0){
            weekday=7;
        }
        saveTempWeekday(context,weekday);
        switch(weekday){
            case 1://星期一
                initwedgit(context,"1");
                break;
            case 2://星期二
                initwedgit(context,"2");
                break;
            case 3://星期三
                initwedgit(context,"3");
                break;
            case 4://星期四
                initwedgit(context,"4");
                break;
            case 5://星期五
                initwedgit(context,"5");
                break;
            case 6://星期六
                initwedgit(context,"6");
                break;
            case 7://星期天
                initwedgit(context,"7");
                break;

        }
    }

    //初始化界面视图
    public void initwedgit(Context context,String day){
        cleanSourceWidget(context);
        RemoteViews views=new RemoteViews(context.getPackageName(), R.layout.appshow);
        if(day.equals("6")){
            views.setTextViewText(R.id.date,context.getString(R.string.sixday));
            views.setTextViewText(R.id.source1,null);
            views.setTextViewText(R.id.source2,null);
            views.setTextViewText(R.id.source_show1,null);
            views.setTextViewText(R.id.source_show2,null);
        }else if(day.equals("7")){
            views.setTextViewText(R.id.date,context.getString(R.string.sevenday));
            views.setTextViewText(R.id.source1,null);
            views.setTextViewText(R.id.source2,null);
            views.setTextViewText(R.id.source_show1,null);
            views.setTextViewText(R.id.source_show2,null);
        }else{
            daySource=DaoImpl.getDayOfSource(day);
            views.setTextViewText(R.id.date,ParseHtml.getSourceDate(day));
            if(daySource!=null){
                if(daySource.size()<2){
                    keys=new ArrayList<>();
                    for(String key:daySource.keySet()){
                        keys.add(key);
                    }
                    if(keys.size()==0){
                        cleanSourceWidget(context);
                    }else{
                        List<String> params= ParseHtml.parsewidgetSource(daySource.get(keys.get(0)));
                        //课程内容
                        if(ParseHtml.decide(daySource.get(keys.get(0)), DecideWeek(context))){
                            views.setTextViewText(R.id.source_show1,params.get(0));
                        }else{
                            views.setTextViewText(R.id.source_show1,params.get(0));
                            views.setTextViewText(R.id.decideSouce1,context.getString(R.string.nodosource));
                        }
                        views.setTextViewText(R.id.sourceplace1,params.get(1));
                        views.setTextViewText(R.id.sourcetime1,ParseHtml.getSourceTime(keys.get(0)));
                        views.setTextViewText(R.id.source1,ParseHtml.getSouceAction(keys.get(0)));
                        //设定课程游标
                        showCursor=daySource.size()-1;
                        saveTempDay(context,day,showCursor);
                    }
                }else{
                    keys=new ArrayList<>();
                    for(String key:daySource.keySet()){
                        keys.add(key);
                    }
                    List<String> params1=ParseHtml.parsewidgetSource(daySource.get(keys.get(keys.size()-1)));
                    //课程内容
                    if(ParseHtml.decide(daySource.get(keys.get(keys.size()-1)),DecideWeek(context))){
                        views.setTextViewText(R.id.source_show1,params1.get(0));
                    }else{
                        views.setTextViewText(R.id.source_show1,params1.get(0));
                        views.setTextViewText(R.id.decideSouce1,context.getString(R.string.nodosource));
                    }
                    views.setTextViewText(R.id.sourceplace1,params1.get(1));
                    views.setTextViewText(R.id.sourcetime1,ParseHtml.getSourceTime(keys.get(keys.size()-1)));
                    views.setTextViewText(R.id.source1,ParseHtml.getSouceAction(keys.get(keys.size()-1)));

                    List<String> params2=ParseHtml.parsewidgetSource(daySource.get(keys.get(keys.size()-2)));
                    //课程内容
                    if(ParseHtml.decide(daySource.get(keys.get(keys.size()-2)),DecideWeek(context))){
                        views.setTextViewText(R.id.source_show2,params2.get(0));
                    }else{
                        views.setTextViewText(R.id.source_show2,params2.get(0));
                        views.setTextViewText(R.id.decideSouce2,context.getString(R.string.nodosource));
                    }
                    views.setTextViewText(R.id.sourceplace2,params2.get(1));
                    views.setTextViewText(R.id.sourcetime2,ParseHtml.getSourceTime(keys.get(keys.size()-2)));
                    views.setTextViewText(R.id.source2,ParseHtml.getSouceAction(keys.get(keys.size() - 2)));
                    //设定课程游标
                    showCursor=daySource.size()-2;
                    saveTempDay(context,day,showCursor);
                }
            }
        }
        ComponentName thisWidget=new ComponentName(context,SourceDesktopShow.class);
        AppWidgetManager manager=AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget,views);
    }

    //上查看课程
    private void prev(Context context){
        if(daySource==null) {
            List<String> param=getTempDay(context);
            daySource=DaoImpl.getDayOfSource(param.get(0));
            showCursor=Integer.valueOf(param.get(1));
            weekday=Integer.valueOf(param.get(2));
            keys=new ArrayList<>();
            for(String key:daySource.keySet()){
                keys.add(key);
            }
        }
        if(daySource.size()>2){
            if(showCursor<(daySource.size()-2)){
                showCursor+=1;
                cleanSourceWidget(context);
                initNextAndPrevView(context, showCursor);
            }
        }
    }

    //下查看课程
    private void next(Context context){
        if(daySource==null) {
            List<String> param=getTempDay(context);
            daySource=DaoImpl.getDayOfSource(param.get(0));
            showCursor=Integer.valueOf(param.get(1));
            weekday=Integer.valueOf(param.get(2));
            keys=new ArrayList<>();
            for(String key:daySource.keySet()){
                keys.add(key);
            }
        }
        if(daySource.size()>2){
            if(showCursor>0){
                showCursor-=1;
                cleanSourceWidget(context);
                initNextAndPrevView(context, showCursor);
            }else{
                showCursor=0;
            }
        }
    }

    //左查看前一天的课程
    private void left(Context context){
        if(weekday==0){
            weekday=getTempWeekday(context);
        }
        int leftday=weekday-1;

        if(leftday==6){
            weekday=leftday;
            initwedgit(context,String.valueOf(weekday));
        }else if(leftday==7){
            weekday=leftday;
            initwedgit(context,String.valueOf(weekday));
        }else if(leftday>=1){
            weekday=leftday;
            initwedgit(context,String.valueOf(weekday));
        }

        saveTempWeekday(context,weekday);
    }

    //右查看后一天的课程
    private void right(Context context){
        if(weekday==0){
            weekday=getTempWeekday(context);
        }
        int rightday=weekday+1;

        if(rightday<=5){
            weekday=rightday;
            initwedgit(context,String.valueOf(weekday));
        }else if(rightday==6){
            weekday=rightday;
            initwedgit(context,String.valueOf(weekday));
        }else if(rightday==7){
            weekday=rightday;
            initwedgit(context,String.valueOf(weekday));
        }

        saveTempWeekday(context,weekday);
    }

    //保存暂时的日期
    public void saveTempDay(Context context,String day,int cursor){
        SharedPreferences.Editor editor=context.getSharedPreferences("AppDay",Context.MODE_PRIVATE).edit();
        editor.putString("day",day);
        editor.putString("cursor", String.valueOf(cursor));
        editor.commit();
    }

    //保存暂时星期数
    public void saveTempWeekday(Context context,int weekday){
        SharedPreferences.Editor editor=context.getSharedPreferences("AppDay",Context.MODE_PRIVATE).edit();
        editor.putString("weekday", String.valueOf(weekday));
        editor.commit();
    }

    //获取暂时星期数
    public int getTempWeekday(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("AppDay",Context.MODE_PRIVATE);
        return Integer.valueOf(sharedPreferences.getString("weekday",null));
    }

    //获取暂存的日期
    public List<String> getTempDay(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("AppDay",Context.MODE_PRIVATE);
        List<String> list=new ArrayList<String>();
        list.add(sharedPreferences.getString("day",null));
        list.add(sharedPreferences.getString("cursor",null));
        list.add(sharedPreferences.getString("weekday", null));
        return list;
    }

    //初始化左右天视图
    public void initNextAndPrevView(Context context,int coursor){
        RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.appshow);

        List<String> params1 = ParseHtml.parsewidgetSource(daySource.get(keys.get(showCursor)));
        //课程内容
        if(ParseHtml.decide(daySource.get(keys.get(showCursor)),DecideWeek(context))){
            views.setTextViewText(R.id.source_show2, params1.get(0));
        }else{
            views.setTextViewText(R.id.source_show2, params1.get(0));
            views.setTextViewText(R.id.decideSouce2,context.getString(R.string.nodosource));
        }
        views.setTextViewText(R.id.sourceplace2, params1.get(1));
        views.setTextViewText(R.id.sourcetime2, ParseHtml.getSourceTime(keys.get(coursor)));
        views.setTextViewText(R.id.source2, ParseHtml.getSouceAction(keys.get(coursor)));

        List<String> params2=ParseHtml.parsewidgetSource(daySource.get(keys.get(showCursor+1)));
        //课程内容
        if(ParseHtml.decide(daySource.get(keys.get(showCursor+1)),DecideWeek(context))){
            views.setTextViewText(R.id.source_show1,params2.get(0));
        }else{
            views.setTextViewText(R.id.source_show1,params2.get(0));
            views.setTextViewText(R.id.decideSouce1,context.getString(R.string.nodosource));
        }
        views.setTextViewText(R.id.sourceplace1,params2.get(1));
        views.setTextViewText(R.id.sourcetime1,ParseHtml.getSourceTime(keys.get(coursor+1)));
        views.setTextViewText(R.id.source1,ParseHtml.getSouceAction(keys.get(coursor+1)));

        ComponentName thisWidget=new ComponentName(context,SourceDesktopShow.class);
        AppWidgetManager manager=AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget,views);
    }

    //清空Source
    private void cleanSourceWidget(Context context){
        RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.appshow);
        views.setTextViewText(R.id.source_show1,null);
        views.setTextViewText(R.id.sourceplace1,null);
        views.setTextViewText(R.id.sourcetime1,null);
        views.setTextViewText(R.id.source1,null);
        views.setTextViewText(R.id.decideSouce1,null);

        views.setTextViewText(R.id.source_show2,null);
        views.setTextViewText(R.id.sourceplace2,null);
        views.setTextViewText(R.id.sourcetime2,null);
        views.setTextViewText(R.id.source2,null);
        views.setTextViewText(R.id.decideSouce2,null);

        ComponentName thisWidget=new ComponentName(context,SourceDesktopShow.class);
        AppWidgetManager manager=AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget,views);
    }

    public int DecideWeek(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user",Context.MODE_PRIVATE);
        int weekstatus=sharedPreferences.getInt("weekstatus",-1);
        if(weekstatus>17){
            weekstatus=17;
        }
        String startDay=sharedPreferences.getString("date","");
        String endDay=CalendarUtil.getMondayOfWeek();
        int tempweek=CalendarUtil.getTwoDay(endDay,startDay);
        int week=tempweek/7;
        if(week>0){
            return weekstatus+week;
        }else{
            return  weekstatus;
        }
    }

}
