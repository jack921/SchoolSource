package com.example.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.application.LogUtil;
import com.example.application.SourceApplication;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jack on 2015/6/13.
 */

public class DaoImpl{

    //保存数据
    public static boolean SaveSource(String[][] source){
       try{
           SourceDataBaseHelper source_db=new SourceDataBaseHelper(SourceApplication.getContext(),"source.db",null,1);
           SQLiteDatabase db=source_db.getWritableDatabase();
           ContentValues values=null;
           for(int i=0;i<9;i++){
               values=new ContentValues();
               values.put("id",i);
               for(int j=0;j<8;j++){
                   values.put("source"+j,source[i][j]);
               }
               db.insert("source",null,values);
           }
           return true;
       }catch(Exception e) {
           LogUtil.e("DaoimplError",e.getMessage());
           return false;
       }
    }

    //得到课表数据
    public static String[][] GetSource(){
        try{
            SourceDataBaseHelper source=new SourceDataBaseHelper(SourceApplication.getContext(),"source.db",null,1);
            SQLiteDatabase db=source.getWritableDatabase();
            Cursor cursor=db.query("source",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                String[][] SourceData=new String[9][5];
                int source1=cursor.getColumnIndex("source1");
                int source2=cursor.getColumnIndex("source2");
                int source3=cursor.getColumnIndex("source3");
                int source4=cursor.getColumnIndex("source4");
                int source5=cursor.getColumnIndex("source5");
                int i=0;
                do{
                    SourceData[i][0]=cursor.getString(source1);
                    SourceData[i][1]=cursor.getString(source2);
                    SourceData[i][2]=cursor.getString(source3);
                    SourceData[i][3]=cursor.getString(source4);
                    SourceData[i][4]=cursor.getString(source5);
                    i++;
                }while(cursor.moveToNext());
                cursor.close();
                return SourceData;
            }else{
                cursor.close();
                return null;
            }
        }catch(Exception e){
            LogUtil.e("DaoimplError",e.getMessage());
            return null;
        }
    }

    //清除课表
    public static boolean ClearSource(){
        try{
            SourceDataBaseHelper source=new SourceDataBaseHelper(SourceApplication.getContext(),"source.db",null,1);
            SQLiteDatabase db=source.getWritableDatabase();
            db.delete("source",null,null);
            return true;
        }catch(Exception e){
            LogUtil.e("DaoimplError",e.getMessage());
            return false;
        }
    }

    //获取每天的课表信息
    public static Map<String,String> getDayOfSource(String day){
        try{
            SourceDataBaseHelper source=new SourceDataBaseHelper(SourceApplication.getContext(),"source.db",null,1);
            SQLiteDatabase db=source.getWritableDatabase();
            Cursor cursor=db.query("source",new String[]{"id","source"+day},"id>?",new String[]{"0"},null,null,null);
            Map<String,String> param=null;
            if(cursor.moveToFirst()){
                int id=cursor.getColumnIndex("id");
                int sourcenum=cursor.getColumnIndex("source"+day);
                param=new TreeMap<String,String>(
                        new Comparator<String>() {
                            @Override
                            public int compare(String lhs, String rhs) {
                             return rhs.compareTo(lhs);
                            }
                        }
                );
                String sourcetemp=null;
                do{
                    sourcetemp=cursor.getString(sourcenum);
                    if(sourcetemp!=null){
                        param.put(cursor.getString(id),sourcetemp);
                    }
                }while(cursor.moveToNext());
            }
            return param;
        }catch(Exception e){
            LogUtil.e("error",e.getMessage());
            return null;
        }
    }

}
