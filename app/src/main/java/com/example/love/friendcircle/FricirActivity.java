package com.example.love.friendcircle;

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

public class FricirActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fricir);

        if (Build.VERSION.SDK_INT >= 15) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy
                    .Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy
                    .Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build());
        }
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");//获取登录页面填写的用户名
        LayoutInflater factory = LayoutInflater.from(FricirActivity.this);
        View view = factory.inflate(R.layout.listview_layout, null);
        TextView name = (TextView) view.findViewById(R.id.username);//定义 listview_layout 中的 username
        //name.setText(username);
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> usernames = new ArrayList<String>();
        String res = "";
        try {
            URL url = new URL("http://liyihui.free.idcfengye.com/getcontext");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();//打开HTTP连接
            httpURLConnection.setRequestMethod("POST");//设置请求方式
            httpURLConnection.setConnectTimeout(5 * 1000);//连接的超时时间
            httpURLConnection.setDoInput(true);//设置这个连接是否可以写入数据
            httpURLConnection.setDoOutput(true);//设置这个连接是否可以输入数据
            httpURLConnection.setRequestProperty("content-Type", "application/json;charset = UTF-8");//设置消息的类型
            httpURLConnection.connect();//连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            JSONObject json = new JSONObject();//创建JSON对象
            json.put("username", URLEncoder.encode(username, "UTF-8"));//对特殊和不可见字符进行编码
            String jsonstr = json.toString();//把JSON对象按JSON的编码格式转换为字符串
            //字符流写入数据
            OutputStream out = httpURLConnection.getOutputStream();//输入流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭
            String str = null;
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {//得到服务端的返回码是否连接成功
                InputStream in = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                while ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                    res += str;
                }
                in.close();
                br.close();
                JSONObject jsonObject = new JSONObject(res);
                System.out.println(jsonObject.getString("state"));
                JSONArray contents = jsonObject.getJSONArray("contents");
                int index = 1;
                for (int i = 0; i < contents.length(); i++) {
                    temp.add(contents.getJSONObject(i).getString("content"));
                    usernames.add(contents.getJSONObject(i).getString("username"));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        /**1.获取ListView对象**/
        ListView listView = (ListView) findViewById(R.id.list_view);
        /**2.准备数据源**/
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < temp.size(); i++) {
            HashMap<String, String> data = new HashMap<>();
            data.put("username", usernames.get(i));
            data.put("content", temp.get(i));
            list.add(data);
        }
        /**3.准备适配器Adapter**/
        ListViewAdapter adapter = new ListViewAdapter(this);
        adapter.setList(list);
        /**4.关联适配器**/
        listView.setAdapter(adapter);


        /**跳转到AddActivity**/
        Button AddButton = (Button) findViewById(R.id.btnAdd);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FricirActivity.this, AddActivity.class);
                i.putExtra("username", username);
                FricirActivity.this.startActivity(i);
            }
        });
    }

}
