package com.example.jack.schoolsource;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DateUtil.CalendarUtil;
import com.example.application.LogUtil;
import com.example.dao.DaoImpl;
import com.example.net.NetWork;
import com.example.net.ParseHtml;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by jack on 2015/6/13.
 */
public class SourseShow extends Activity {

    private String[][] source=new String[8][5];
    private SharedPreferences sharedPreferences;
    private static ProgressDialog dialog;
    private String[] weeks;
    private Spinner spinner;
    private int spinnerStatus=-1;

    private TextView text00;
    private TextView text01;
    private TextView text02;
    private TextView text03;
    private TextView text04;

    private TextView text10;
    private TextView text11;
    private TextView text12;
    private TextView text13;
    private TextView text14;

    private TextView text20;
    private TextView text21;
    private TextView text22;
    private TextView text23;
    private TextView text24;

    private TextView text30;
    private TextView text31;
    private TextView text32;
    private TextView text33;
    private TextView text34;

    private TextView text40;
    private TextView text41;
    private TextView text42;
    private TextView text43;
    private TextView text44;

    private TextView text50;
    private TextView text51;
    private TextView text52;
    private TextView text53;
    private TextView text54;

    private TextView text60;
    private TextView text61;
    private TextView text62;
    private TextView text63;
    private TextView text64;

    private TextView text70;
    private TextView text71;
    private TextView text72;
    private TextView text73;
    private TextView text74;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sourceshow);
        setOverflowShowingAlways();
        String[][] temp=getLocalSource();
        weeks=getResources().getStringArray(R.array.weeks);

        if(temp!=null){
            int weekstatus=sharedPreferences.getInt("weekstatus",-1);
            String name=sharedPreferences.getString("name",null);
            if(name!=null){
                getActionBar().setTitle(name);
            }
            if(weekstatus!=-1){
                if(weekstatus<=17){
                    String startDay=sharedPreferences.getString("date","");
                    String endDay=CalendarUtil.getMondayOfWeek();
                    int tempweek=CalendarUtil.getTwoDay(endDay,startDay);
                    int week=tempweek/7;
                    if(week==0){
                        buildView(temp,weekstatus);
                        spinnerStatus=weekstatus;
                    }else{
                        int finalweek=weekstatus+week;
                        if(finalweek>17){
                            buildView(temp,17);
                            spinnerStatus=17;
                        }else{
                            buildView(temp,finalweek);
                            spinnerStatus=finalweek;
                        }
                    }
                }else{
                    buildView(temp,17);
                    spinnerStatus=17;
                }
            }
        }else{
            Toast.makeText(this,getString(R.string.getSourceError),Toast.LENGTH_LONG).show();
        }

    }

    //显示课表
    public void buildView(String[][] source,int week){
        for(int i=1;i<9;i++){
            for(int j=0;j<5;j++){
                if(source[i][j]!=null){
                    selectView(i-1,j,source[i][j],week);
                }
            }
        }
    }

    //选择课表显示
    public void selectView(int i,int j,String source,int week){

        if(i==0&&j==0){
            text00=(TextView)findViewById(R.id.mon1);
            build(text00,source,week);
        }else if(i==0&&j==1){
            text01=(TextView) findViewById(R.id.tues1);
            build(text01,source,week);
        }else if(i==0&&j==2){
            text02=(TextView)findViewById(R.id.wed1);
            build(text02,source,week);
        }else if(i==0&&j==3){
            text03=(TextView)findViewById(R.id.thurs1);
            build(text03,source,week);
        }else if(i==0&&j==4){
            text04=(TextView) findViewById(R.id.fri1);
            build(text04,source,week);
        }else
        if(i==1&&j==0){
            text10 = (TextView) findViewById(R.id.mon2);
            build(text10,source,week);
        }else if(i==1&&j==1){
            text11 = (TextView) findViewById(R.id.thes2);
            build(text11,source,week);
        }else if(i==1&&j==2){
            text12=(TextView) findViewById(R.id.wed2);
            build(text12,source,week);
        }else if(i==1&&j==3){
            text13=(TextView) findViewById(R.id.thurs2);
            build(text13,source,week);
        }else if(i==1&&j==4){
            text14=(TextView) findViewById(R.id.fri2);
            build(text14,source,week);
        }else
        if(i==2&&j==0){
            text20 = (TextView) findViewById(R.id.mon3);
            build(text20,source,week);
        }else if(i==2&&j==1){
            text21 = (TextView) findViewById(R.id.thes3);
            build(text21,source,week);
        }else if(i==2&&j==2){
            text22=(TextView) findViewById(R.id.wed3);
            build(text22,source,week);
        }else if(i==2&&j==3){
            text23=(TextView) findViewById(R.id.thurs3);
            build(text23,source,week);
        }else if(i==2&&j==4){
            text24=(TextView) findViewById(R.id.fri3);
            build(text24,source,week);
        }else
        if(i==3&&j==0){
            text30 = (TextView) findViewById(R.id.mon4);
            build(text30,source,week);
        }else if(i==3&&j==1){
            text31= (TextView) findViewById(R.id.thes4);
            build(text31,source,week);
        }else if(i==3&&j==2){
            text32=(TextView) findViewById(R.id.wed4);
            build(text32,source,week);
        }else if(i==3&&j==3){
            text33=(TextView) findViewById(R.id.thurs4);
            build(text33,source,week);
        }else if(i==3&&j==4){
            text34=(TextView) findViewById(R.id.fri4);
            build(text34,source,week);
        }else
        if(i==4&&j==0){
            text40 = (TextView) findViewById(R.id.mon5);
            build(text40,source,week);
        }else if(i==4&&j==1){
            text41= (TextView) findViewById(R.id.thes5);
            build(text41,source,week);
        }else if(i==4&&j==2){
            text42=(TextView) findViewById(R.id.wed5);
            build(text42,source,week);
        }else if(i==4&&j==3){
            text43=(TextView) findViewById(R.id.thurs5);
            build(text43,source,week);
        }else if(i==4&&j==4){
            text44=(TextView) findViewById(R.id.fri5);
            build(text44,source,week);
        }else
        if(i==5&&j==0){
            text50 = (TextView) findViewById(R.id.mon6);
            build(text50,source,week);
        }else if(i==5&&j==1){
            text51 = (TextView) findViewById(R.id.thes6);
            build(text51,source,week);
        }else if(i==5&&j==2){
            text52=(TextView) findViewById(R.id.wed6);
            build(text52,source,week);
        }else if(i==5&&j==3){
            text53=(TextView) findViewById(R.id.thurs6);
            build(text53,source,week);
        }else if(i==5&&j==4){
            text54=(TextView) findViewById(R.id.fri6);
            build(text54,source,week);
        }else
        if(i==6&&j==0){
            text60 = (TextView) findViewById(R.id.mon7);
            build(text60,source,week);
        }else if(i==6&&j==1){
            text61 = (TextView) findViewById(R.id.thes7);
            build(text61,source,week);
        }else if(i==6&&j==2){
            text62=(TextView) findViewById(R.id.wed7);
            build(text62,source,week);
        }else if(i==6&&j==3){
            text63=(TextView) findViewById(R.id.thurs7);
            build(text63,source,week);
        }else if(i==6&&j==4){
            text64=(TextView) findViewById(R.id.fri7);
            build(text64,source,week);
        }else
        if(i==7&&j==0){
            text70 = (TextView) findViewById(R.id.mon8);
            build(text70,source,week);
        }else if(i==7&&j==1){
            text71 = (TextView) findViewById(R.id.thes8);
            build(text71,source,week);
        }else if(i==7&&j==2){
            text72=(TextView) findViewById(R.id.wed8);
            build(text72,source,week);
        }else if(i==7&&j==3){
            text73=(TextView) findViewById(R.id.thurs8);
            build(text73,source,week);
        }else if(i==7&&j==4){
            text74=(TextView) findViewById(R.id.fri8);
           build(text74,source,week);
        }
    }

    public void build(TextView text,String source,int week) {
        text.setVisibility(View.VISIBLE);
        String temp=ParseHtml.parseSource(source);
        if(ParseHtml.decide(source,week)){
            text.setText(temp);
        }else{
            text.setText(temp+getString(R.string.nodo));
        }
    }

    //获取本地课表
    public String[][] getLocalSource(){
        source= DaoImpl.GetSource();
        if(!GetSaveStatus()){
            DaoImpl.ClearSource();
        }
        return source;
    }

    //获取登陆状态
    public boolean GetSaveStatus(){
        sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        String username=sharedPreferences.getString("username","");
        String password=sharedPreferences.getString("password","");
        if(username.equals("")&&password.equals("")){
           return false;
        }else{
           return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        spinner=(Spinner)menu.findItem(R.id.week).getActionView();
        spinner.setAdapter(new ArrayAdapter<String>(this,R.layout.spinnerview,weeks));
        if(spinnerStatus!=-1){
            spinner.setSelection(spinnerStatus-1,true);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SharedPreferences.Editor editor=getSharedPreferences("user", MODE_PRIVATE).edit();
                    editor.putInt("weekstatus", position + 1);
                    editor.putString("date", CalendarUtil.getMondayOfWeek());
                    editor.commit();
                    buildView(source, position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch(item.getItemId()){
           case R.id.update_settings:
               dialog=ProgressDialog.show(this,null,getString(R.string.updateSourceIng));
               updates();
           break;
           case R.id.unlogin:
               unlogin();
           break;
       }
        return super.onOptionsItemSelected(item);
    }

    //setting显示图标
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window. FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible( true);
                    m.invoke(menu, true);
                }catch (Exception e){
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    //使actionbar图标显示在右上角
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration. class
                    .getDeclaredField("sHasPermanentMenuKey" );
            menuKeyField.setAccessible( true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //网上更新课表
    public void updates(){
        String username=sharedPreferences.getString("username","");
        String password=sharedPreferences.getString("password","");
        if(username.equals("")&&password.equals("")){
            String name=sharedPreferences.getString("name","");
            int status=sharedPreferences.getInt("weekstatus",0);
            String date=sharedPreferences.getString("date","");
            if(!name.equals("")&&status!=0&&!date.equals("")){
                new updateSource().execute("","");
            }else{
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }else{
           new updateSource().execute(username,password);
        }
    }

    //网上更新课表具体方法
    class updateSource extends AsyncTask<String,String,String[][]>{

        int week;

        @Override
        protected String[][] doInBackground(String... params) {
            String result;
            if(params[0].equals("")&&params[1].equals("")){
                result="p";
            }else{
                Map<String,String> map=NetWork.getCode();
                if(map!=null){
                    result= NetWork.login(params[0],params[1], map);
                    if(result==null){
                        publishProgress(getBaseContext().getString(R.string.updateNameError));
                        return null;
                    }
                }else{
                    publishProgress(getBaseContext().getString(R.string.updateServerError));
                    return null;
                }
            }

            if(result.equals("p")){
                String html=NetWork.GetSource();
                String[][] source=ParseHtml.parseHtml(html);
                if(source!=null){
                    DaoImpl.ClearSource();
                    DaoImpl.SaveSource(source);
                    List<String> updateData=ParseHtml.parseInfo(html);
                    if(updateData.get(1).equals("")){
                        week=17;
                    }else{
                        week=Integer.valueOf(ParseHtml.parseWeek(updateData.get(1)));
                    }
                    saveUpdateUser(week);
                    return DaoImpl.GetSource();
                }else{
                    publishProgress(getBaseContext().getString(R.string.getSourceError));
                    return null;
                }
                }else if(result.equals("！")){
                    publishProgress(getBaseContext().getString(R.string.updateNameError));
                    return null;
                }else if(result.equals("]")){
                    publishProgress(getBaseContext().getString(R.string.updatePassError));
                    return null;
                }else{
                    publishProgress(getBaseContext().getString(R.string.updateServerError));
                    return null;
                }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            dialog.setMessage(values[0]);
            Toast.makeText(getBaseContext(),values[0],Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(50);
            }catch(Exception e){
                e.printStackTrace();
            }
            dialog.dismiss();
        }

        @Override
        protected void onPostExecute(String[][] strings) {
            buildView(strings, week);
            Toast.makeText(getBaseContext(),getBaseContext().getString(R.string.updateSuccess)
                    ,Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

    }

    //登出方法
    public void unlogin(){
        try{
            if(getSharedPreferences("user",MODE_PRIVATE).edit().clear().commit()){
                DaoImpl.ClearSource();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this,getString(R.string.unloginError),Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            LogUtil.e("SourseShowError",e.getMessage());
        }
    }

    public void saveUpdateUser(int week){
            SharedPreferences.Editor editor=getSharedPreferences("user",MODE_PRIVATE).edit();
            editor.putInt("weekstatus",week);
            editor.putString("date", CalendarUtil.getMondayOfWeek());
            editor.commit();
    }

}
