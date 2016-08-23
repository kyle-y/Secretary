package com.example.yxb.secretary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.adapter.ExpressCompanyAdapter;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.utils.FastJson;
import com.example.yxb.secretary.utils.ParseXmlTag;
import com.example.yxb.secretary.utils.UiUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ExpressFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener {
    private Button button_quarey;
    private TextView choose_express;
    private EditText text_orderNum;
    private ListView list_expressB;
    private String result;
    private String companyId;
    private ExpressCompanyAdapter adapter;
    private List<Map<String, Object>> data;
    private List<Map<String, Object>> results;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_express, null);

        button_quarey = (Button) view.findViewById(R.id.button_quarey);
        text_orderNum = (EditText) view.findViewById(R.id.text_orderNum);
        list_expressB = (ListView) view.findViewById(R.id.list_expressB);
        choose_express = (TextView) view.findViewById(R.id.choose_express);

        results = new ArrayList<>();
        data = new ArrayList<>();
        initData();
        adapter = new ExpressCompanyAdapter(data);
        list_expressB.setAdapter(adapter);
        list_expressB.setOnItemClickListener(this);

        button_quarey.setOnClickListener(this);

        return view;
    }

    private void initData() {
        //将raw文件转化为字符串
        InputStream inputStream = getActivity().getResources().openRawResource(R.raw.companyinfos);
        String companys = null;
        try {
            int len = inputStream.available();
            byte[] buffer = new byte[len];
            inputStream.read(buffer);
            companys = new String(buffer,"utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将字符串进行json解析
        Map<String,Object> companyMap = FastJson.getMapObj(companys);
        data = FastJson.getListMap(companyMap.get("companyinfos").toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_quarey:
                results.clear();
                String expressId = text_orderNum.getText().toString();
                if (!expressId.isEmpty()) {
                    getInfo(companyId, expressId);

                } else {
                    Toast.makeText(MyApplication.getContext(), "输入数据为空", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }



    // 点击空白处 软键盘消失

    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) MyApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;

    }
    //获得快递信息
    private void getInfo(String id, String expressId) {
        int rand = (int) Math.random() * 1000 + 1000;
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(
                "http://wap.kuaidi100.com/q.jsp?rand=" + rand
                        + "&id=" + id + "&postid="
                        + expressId + "&fromWeb=null").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string();
                Object[] lineArray = ParseXmlTag.getObjectArray(result, "p");

                for (int i = 3; i < lineArray.length - 2; i++) {
                    String temp = lineArray[i].toString();
                    String stateInfo = null;
                    String stateTime = null;
                    if (temp.substring(0, 4).equals("<p>·")) {
                        Pattern p = Pattern.compile(">([^</]+)<br");//提取标签值
                        Matcher m = p.matcher(temp);//
                        while (m.find()) {
                            //为了复用这个listView，将信息放进map里
                            stateTime = m.group(1);
                        }
                        p = Pattern.compile(">([^</]+)</");
                        Matcher m2 = p.matcher(temp);//
                        while (m2.find()) {
                            //为了复用这个listView，将信息放进map里
                            stateInfo = m2.group(1);
                        }
                        Map<String,Object> tempMap = new HashMap<String, Object>();
                        tempMap.put("name",stateTime + "  :" + stateInfo);
                        results.add(tempMap);
                    }
                }
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.update(results);
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        companyId = data.get(position).get("id").toString();
        choose_express.setText(data.get(position).get("name").toString());
    }
}
