package com.example.jack.schoolsource;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.DateUtil.CalendarUtil;
import com.example.application.LogUtil;
import com.example.dao.DaoImpl;
import com.example.net.NetWork;
import com.example.net.ParseHtml;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText user,pass;
    private Button login;
    private CheckBox save;
    private static int ErrorTop=1;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckSaveLogin();
        user=(EditText)findViewById(R.id.user);
        pass=(EditText)findViewById(R.id.pass);
        login=(Button)findViewById(R.id.login);
        save=(CheckBox)findViewById(R.id.save);

        login.setOnClickListener(this);

    }

    public void CheckSaveLogin(){
        try{
            SharedPreferences Preferences=getSharedPreferences("user",MODE_PRIVATE);
            String username=Preferences.getString("username","");
            String password=Preferences.getString("password","");
            if(!username.equals("")&&!password.equals("")){
                Intent intent=new Intent(this,SourseShow.class);
                startActivity(intent);
                finish();
            }
        }catch(Exception e){
            LogUtil.e("MainActivityError",e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login:
                progressDialog=ProgressDialog.show(this,null,getString(R.string.logining));
                login(user.getText().toString(),pass.getText().toString());
                break;
        }
    }

    public void login(final String user, final String pass){
        new Thread(){
            @Override
            public void run() {
                Map<String,String> map=NetWork.getCode();
                if(map!=null){
                    String result=NetWork.login(user,pass,map);
                    if(result==null){
                        sendMessage(getBaseContext().getString(R.string.updateServerError));
                    }else if(result.equals("p")){
                        String html=NetWork.GetSource();
                        String[][] source= ParseHtml.parseHtml(html);
                        if(source!=null){
                            DaoImpl.SaveSource(source);
                            List<String> params=ParseHtml.parseInfo(html);
                            if(params.get(1).equals("")){
                                saveUser(ParseHtml.parseName(params.get(0)),17);
                            }else{
                                saveUser(ParseHtml.parseName(params.get(0))
                                        ,Integer.valueOf(ParseHtml.parseWeek(params.get(1))));
                            }
                            Intent intent=new Intent(getBaseContext(),SourseShow.class);
                            startActivity(intent);
                            finish();
                        }else{
                            sendMessage(getBaseContext().getString(R.string.getSourceError));
                        }
                    }else if(result.equals("！")){
                        sendMessage(getBaseContext().getString(R.string.snoerror));
                    }else if(result.equals("]")){
                        sendMessage(getBaseContext().getString(R.string.passerror));
                    }else{
                        sendMessage(getBaseContext().getString(R.string.updateServerError));
                    }
                }else{
                    sendMessage(getBaseContext().getString(R.string.updateServerError));
                }
            }
        }.start();
    }

    public void saveUser(String name,int week){
        SharedPreferences.Editor editor=getSharedPreferences("user",MODE_PRIVATE).edit();
        editor.putString("name",name);
        editor.putInt("weekstatus",week);
        editor.putString("date", CalendarUtil.getMondayOfWeek());
        if(save.isChecked()){
            editor.putString("username",user.getText().toString());
            editor.putString("password",pass.getText().toString());
        }
        editor.commit();
    }

    public void sendMessage(String tip){
        Message message=new Message();
        message.what=ErrorTop;
        Bundle bundle=new Bundle();
        bundle.putString("content",tip);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
        switch(msg.what){
            case 1:
                progressDialog.dismiss();
                String message=msg.getData().getString("content");
                Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT).show();
            break;
        }
        }
    };

}
