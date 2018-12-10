package com.example.love.friendcircle;

//import android.R;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Looper;
//import android.os.StrictMode;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import org.json.JSONObject;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLEncoder;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class RegActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**开辟一个子线程访问网络，否则会抛出异常**/
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            String username = ((EditText)findViewById(R.id.editText)).getText().toString();
                            String phone  = ((EditText)findViewById(R.id.editText2)).getText().toString();
                            String password = ((EditText)findViewById(R.id.editText3)).getText().toString();

                            URL url = new URL("http://liyihui.free.idcfengye.com/register");
                            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();//打开HTTP连接
                            httpURLConnection.setRequestMethod("POST");//设置请求方式
                            httpURLConnection.setConnectTimeout(5 * 1000);//连接的超时时间
                            httpURLConnection.setDoInput(true);//设置这个连接是否可以写入数据
                            httpURLConnection.setDoOutput(true);//设置这个连接是否可以输入数据
                            httpURLConnection.setRequestProperty("content-Type","application/json;charset = UTF-8");//设置消息的类型
                            httpURLConnection.connect();//连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
                            JSONObject json = new JSONObject();//创建JSON对象
                            json.put("username", URLEncoder.encode(username, "UTF-8"));//对特殊和不可见字符进行编码
                            json.put("phone", URLEncoder.encode(phone, "UTF-8"));//把数据put进JSON对象中
                            json.put("password", URLEncoder.encode(password, "UTF-8"));
                            String jsonstr = json.toString();//把JSON对象按JSON的编码格式转换为字符串
                            //字符流写入数据
                            OutputStream out = httpURLConnection.getOutputStream();//输入流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
                            bw.write(jsonstr);//把json字符串写入缓冲区中
                            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
                            out.close();
                            bw.close();//使用完关闭

                            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){//得到服务端的返回码是否连接成功
                                InputStream in = httpURLConnection.getInputStream();
                                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                                String str = null;
                                StringBuffer buffer = new StringBuffer();
                                while((str = br.readLine()) != null){//BufferedReader特有功能，一次读取一行数据
                                    buffer.append(str);
                                }
                                in.close();
                                br.close();
                                JSONObject rjson = new JSONObject(buffer.toString());
                                System.out.println(rjson);


//                                if( "Landing successfully".equals(rjson.get("state").toString())){
//                                    Intent i = new Intent(RegActivity.this, MainActivity.class);
//                                    startActivity(i);
//                                }else {
//                                    Looper.prepare();
//                                    Toast.makeText(getApplicationContext(),rjson.get("state").toString() , Toast.LENGTH_SHORT).show();
//                                    Looper.loop();
//                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });


    }
}
