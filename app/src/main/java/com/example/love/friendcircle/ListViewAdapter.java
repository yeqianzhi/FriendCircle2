package com.example.love.friendcircle;

//import android.R;
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ListViewAdapter extends BaseAdapter {
    //数据集合
    List<HashMap<String,String>> list;
    //反射器
    LayoutInflater inflater;
    //构造器 alt+insert
    public ListViewAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
    }

    public ListViewAdapter(View.OnClickListener onClickListener) {
    }

    //传入数据集合
    public void setList(List<HashMap<String,String>> list){
        this.list = list;
    }
    @Override
    public int getCount() { //listview显示数据的条数
        return list.size();
    }

    @Override
    public Object getItem(int position) { //当前项的下标
        return list.get(position);
    }

    @Override
    public long getItemId(int position) { //项的ID
        return position;
    }

    @Override
    /**渲染**/
    public View getView(int position, View convertView, ViewGroup parent) {
        //反射行布局
        View view = inflater.inflate(R.layout.listview_layout, null);
        //获取各个控件
        TextView username = (TextView)view.findViewById(R.id.username);
        TextView content = (TextView)view.findViewById(R.id.content);
        //1
//        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        HashMap<String,String> map = list.get(position);
        //给控件赋值
        username.setText((String)map.get("username"));
        content.setText((String)map.get("content"));
        return view;
    }
}
