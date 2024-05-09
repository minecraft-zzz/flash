package com.google.mlkit.vision.demo.java;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.dao.UserDao;
import com.google.mlkit.vision.demo.entity.User;

public class RegisterActivity extends AppCompatActivity {
    EditText name, username, password, phone, age, repeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        age = findViewById(R.id.age);
        repeatPassword = findViewById(R.id.repeatPassword);
    }

    public void register(View view){
        String cname = name.getText().toString();
        String cusername = username.getText().toString();
        String cpassword = password.getText().toString();
        String crepeatPassword = repeatPassword.getText().toString();

        if (!cpassword.equals(crepeatPassword)) {
            Toast.makeText(getApplicationContext(), "密码不一致，请重新输入", Toast.LENGTH_LONG).show();
            return;
        }

        if(cname.length() < 2 || cusername.length() < 2 || cpassword.length() < 2 ){
            Toast.makeText(getApplicationContext(), "输入信息不符合要求请重新输入", Toast.LENGTH_LONG).show();
            return;
        }

        new Thread(){
            @Override
            public void run() {
                UserDao userDao = new UserDao();

                // 检查账号名是否已存在
                if (userDao.isAccountExists(cname)) {
                    hand.sendEmptyMessage(3);
                    return;
                }

                // 检查用户名是否已存在
                if (userDao.isUsernameExists(cusername)) {
                    hand.sendEmptyMessage(1);
                    return;
                }

                User user = new User();
                user.setName(cname);
                user.setUsername(cusername);
                user.setPassword(cpassword);
                user.setAge(Integer.parseInt(age.getText().toString()));
                user.setPhone(phone.getText().toString());

                if (userDao.register(user)) {
                    hand.sendEmptyMessage(2);
                } else {
                    hand.sendEmptyMessage(0);
                }
            }
        }.start();
    }



    final Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "该用户名已存在，请换一个用户名", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                    finish(); // 注册成功后返回上一级页面
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "该账号已存在，请换一个账号", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


}


