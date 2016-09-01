package com.example.yxb.secretary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.activity.BusLineSearchActivity;
import com.example.yxb.secretary.adapter.LinesAdapter;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.utils.ParseXmlTag;
import com.example.yxb.secretary.utils.UiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.fragment
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class BusFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{
    private EditText bus_line;
    private Button bus_query_line, bus_query_map;
    private ListView list_line;
    private TextView text_tips;
    private String result;
    private List<String> lines;
    private List<String> Stations;
    private List<String> results;
    private LinesAdapter adapter;

    private String lineNum;
    private String orieitation;
    private int position1;
    private String station;
    private int position2;

    private static final int STATE_LINES = 0;
    private static final int STATE_SATATIONS = 1;
    private static final int STATE_RESULTS = 2;
    private int state = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_bus, null);

        bus_line = (EditText) view.findViewById(R.id.bus_line);
        bus_query_line = (Button) view.findViewById(R.id.bus_query_line);
        bus_query_map = (Button) view.findViewById(R.id.bus_query_map);
        list_line = (ListView) view.findViewById(R.id.list_line);
        text_tips = (TextView) view.findViewById(R.id.text_tips);

        lines = new ArrayList<String>();
        Stations = new ArrayList<String>();
        results = new ArrayList<String>();

        adapter = new LinesAdapter(lines);
        list_line.setAdapter(adapter);

        list_line.setOnItemClickListener(this);
        bus_query_line.setOnClickListener(this);
        bus_query_map.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bus_query_line:
                state = STATE_LINES;
                lines.clear();
                Stations.clear();
                results.clear();
                lineNum = bus_line.getText().toString();
                if (!lineNum.isEmpty()){
                    getLineNums(lineNum);
                }else{
                    Toast.makeText(MyApplication.getContext(), "输入为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bus_query_map:
                lineNum = bus_line.getText().toString();
                if (!lineNum.isEmpty()){
                    Intent intent = new Intent(MyApplication.getContext(), BusLineSearchActivity.class);
                    intent.putExtra("city","郑州");
                    intent.putExtra("line", lineNum);
                    startActivity(intent);
                }else{
                    Toast.makeText(MyApplication.getContext(), "输入为空！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (state == STATE_LINES){
            orieitation = lines.get(position);
            position1 = position;
            getStations(lineNum,orieitation,position1);
        }
        if (state == STATE_SATATIONS){
            station = Stations.get(position);
            position2 = position + 1;
            getResult(lineNum,orieitation,station,position1,position2);
        }

    }

    private void getLineNums(String line){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(
                "http://www.deng84.cn/gpsbak222.aspx?xl="+line+"路&ref=2&uid=aa").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string();
                Object[] lineArray = ParseXmlTag.getObjectArray(result, "a");

                for (int i = 0; i < lineArray.length; i ++){
                    String temp = lineArray[i].toString();
                    if (temp.substring(0,12).equals("<a href=\"gps")){
                        Pattern p = Pattern.compile(">([^</]+)</");//提取标签值
                        Matcher m = p.matcher(temp);//
                        while (m.find()) {
                            lines.add(m.group(1));
                        }
                    }
                }
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text_tips.setText("请选择方向");
                        adapter.update(lines);
                    }
                });

            }
        });
    }

    private void getStations(String line, String orieitation,int position1){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(
                "http://www.deng84.cn/gpsbak222.aspx?ref=3&xl="+line+"路&ud="+position1+"&fx="+orieitation+"&uid=www.deng84.cn").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string();
                Object[] lineArray = ParseXmlTag.getObjectArray(result, "a");

                for (int i = 0; i < lineArray.length; i ++){
                    String temp = lineArray[i].toString();
                    if (temp.substring(0,12).equals("<a href=\"gps")){
                        Pattern p = Pattern.compile(">([^</]+)<p");//提取标签值
                        Matcher m = p.matcher(temp);//
                        while (m.find()) {
                            Stations.add(m.group(1));
                        }
                    }
                }

                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text_tips.setText("请选择您所在的站点");
                        adapter.update(Stations);
                        state = STATE_SATATIONS;
                    }
                });

            }
        });
    }

    private void getResult(String line, String orieitation,String station,int positon1,int position2){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(
                "http://www.deng84.cn//gpsbak222.aspx?ref=4&xl="+lineNum+"路&ud="+position1+"&fx="+orieitation
                        +"&sno="+position2+"&hczd="+station+"&uid=www.deng84.cn").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string();
                Object[] lineArray = ParseXmlTag.getObjectArray(result, "p");

                for (int i = 0; i < lineArray.length; i ++){
                    String temp = lineArray[i].toString();
                    if (temp.substring(0,14).equals("<p style=\"font")){
                        Pattern p = Pattern.compile(">([^</]+)</");//提取标签值
                        Matcher m = p.matcher(temp);//
                        while (m.find()) {
                            results.add(m.group(1));
                        }
                    }
                }

                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text_tips.setText("班车信息为：");
                        adapter.update(results);
                        state = STATE_RESULTS;
                    }
                });

            }
        });
    }


}
