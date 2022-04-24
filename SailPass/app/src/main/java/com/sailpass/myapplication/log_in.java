package com.sailpass.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class log_in extends AppCompatActivity {
    private Button btn_login;//登录按钮
    private String uerName,password,spPsw;//获取的用户名，密码，加密密码
    private EditText user_input,password_input;//编辑框
    private ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {

        btn_login=findViewById(R.id.btn_login);
        user_input=findViewById(R.id.user_input);
        password_input=findViewById(R.id.password_input);
        btn_back = findViewById(R.id.btn_back_log);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始登录，获取用户名和密码 getText().toString().trim();
                uerName=user_input.getText().toString().trim();
                password=password_input.getText().toString().trim();
                // 定义方法 readPsw为了读取用户名，得到密码
                spPsw=readPsw(uerName);
                // TextUtils.isEmpty
                if(TextUtils.isEmpty(uerName)){
                    Toast.makeText(log_in.this, "Please enter your username.", Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(log_in.this, "Please enter your password.", Toast.LENGTH_SHORT).show();

                    // md5Psw.equals(); 判断，输入的密码加密后，是否与保存在SharedPreferences中一致
                }else if(password.equals(spPsw)){
                    //一致登录成功
                    Toast.makeText(log_in.this, "Log in successfully.", Toast.LENGTH_SHORT).show();
                    //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
                    saveLoginStatus(true, uerName);
                    //登录成功后关闭此页面进入主页
                    Intent data=new Intent();
                    //datad.putExtra( ); name , value ;
                    data.putExtra("isLogin",true);
                    //RESULT_OK为Activity系统常量，状态码为-1
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    setResult(RESULT_OK,data);
                    //销毁登录界面
                    log_in.this.finish();
                    //跳转到主界面，登录成功的状态传递到 MainActivity 中
                    startActivity(new Intent(log_in.this, home.class));
                }else if((spPsw!=null&&!TextUtils.isEmpty(spPsw)&&!password.equals(spPsw))){
                    Toast.makeText(log_in.this, "Your password is incorrect.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(log_in.this, "This username doesn't exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(log_in.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    /*
     *从SharedPreferences中根据用户名读取密码
     */
    private String readPsw(String userName){
        //getSharedPreferences("loginInfo",MODE_PRIVATE);
        //"loginInfo",mode_private; MODE_PRIVATE表示可以继续写入
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //sp.getString() userName, "";
        return sp.getString(userName , "");
    }
    /*
     *保存登录状态和登录用户名到SharedPreferences中
     */
    private void saveLoginStatus(boolean status,String userName){
        //saveLoginStatus(true, userName);
        //loginInfo表示文件名  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器
        SharedPreferences.Editor editor=sp.edit();
        //存入boolean类型的登录状态
        editor.putBoolean("isLogin", status);
        //存入登录状态时的用户名
        editor.putString("loginUserName", userName);
        //提交修改
        editor.apply();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //是获取注册界面回传过来的用户名
            // getExtra().getString("***");
            String userName=data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){
                //设置用户名到 et_user_name 控件
                user_input.setText(userName);
                //et_user_name控件的setSelection()方法来设置光标位置
                user_input.setSelection(userName.length());
            }
        }
    }
}

