package com.example.yxb.secretary.fragment;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.example.yxb.secretary.R;
import com.example.yxb.secretary.activity.MainActivity;
import com.example.yxb.secretary.adapter.MyNewsAdapter;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.deroctions.DividerItemlistDraction;
import com.example.yxb.secretary.utils.FastJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.fragment
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class NewsFragment extends Fragment implements View.OnClickListener{

    private AppBarLayout news_appBar;
    private RecyclerView news_recycleView;
    private MainActivity mActivity;
    private BottomNavigationBar bottomNavigationBar;
    private FloatingActionButton FAB;
    private Animation animation;
    private MyNewsAdapter adapter;
    private Toolbar toolbar_news;
    private CollapsingToolbarLayout collapsing;
    private boolean isHide = false;
    private float height;
    private float oldHeight;
    private List<Map<String, Object>> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);

        news_appBar = (AppBarLayout) view.findViewById(R.id.news_appBar);
        news_recycleView = (RecyclerView) view.findViewById(R.id.news_recycleView);

        data = new ArrayList<>();
        news_recycleView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        adapter = new MyNewsAdapter(data);
        news_recycleView.setAdapter(adapter);
        news_recycleView.addItemDecoration(new DividerItemlistDraction(MyApplication.getContext(), DividerItemlistDraction.VERTICAL_LIST));
        news_recycleView.setItemAnimator(new DefaultItemAnimator());
        initData(1 + "");
        mActivity = (MainActivity) getActivity();
        bottomNavigationBar = mActivity.bottomnavigationbar;
        height = bottomNavigationBar.getHeight();
        oldHeight = mActivity.layout_fragments.getHeight();
        news_recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && dy > 10 && !isHide){
                    hideMenu();
                }
                if (dy < 0 && -dy > 10 && isHide){
                    showMenu();
                }
            }

        });

//        toolbar_news = (Toolbar) view.findViewById(R.id.toolbar_news);
//        toolbar_news.setTitle("新闻头条");
//        toolbar_news.setTitleTextColor(Color.WHITE);

        collapsing = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing);
        collapsing.setTitle("新闻头条");
        collapsing.setCollapsedTitleTextColor(Color.WHITE);
        collapsing.setExpandedTitleColor(Color.WHITE);


        FAB = (FloatingActionButton) view.findViewById(R.id.FAB);
        FAB.setOnClickListener(this);
        animation = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.refresh);

        return view;
    }

    private void initData(String page) {
        Parameters param = new Parameters();
        param.put("page",page);
        ApiStoreSDK.execute("http://apis.baidu.com/showapi_open_bus/channel_news/search_news",ApiStoreSDK.GET,param,
                new ApiCallBack(){
                    @Override
                    public void onSuccess(int gi, String s) {
                        Log.i("aaa","执行下载onSuccess");
                        Map<String, Object> sObject = FastJson.getMapObj(s);
                        Map<String, Object> bodyObject = FastJson.getMapObj(sObject.get("showapi_res_body").toString());
                        Map<String, Object> beanObject = FastJson.getMapObj(bodyObject.get("pagebean").toString());
                        List<Map<String, Object>> contentList = FastJson.getListMap(beanObject.get("contentlist").toString());
                        if (contentList != null){
                            for (int j = 0; j < contentList.size(); j++){
                                StringBuffer sb = new StringBuffer();
                                String title = null;
                                if (contentList.get(j).get("title") != null){
                                    title = contentList.get(j).get("title").toString();
                                }else{
                                    title = "";
                                }
                                String topic = contentList.get(j).get("desc").toString();
                                String time = contentList.get(j).get("pubDate").toString().substring(11,16);
                                String imageurl = "http://img0.imgtn.bdimg.com/it/u=994036202,3864811580&fm=206&gp=0.jpg";
                                String content = "内容为空";

                                List<Map<String, Object>> imageList = FastJson.getListMap(contentList.get(j).get("imageurls").toString());
                                if (imageList.size() > 0){
                                    imageurl = imageList.get(0).get("url").toString();
                                }
                                Map<String,Object> tempMap = contentList.get(j);
                                Object temp = tempMap.get("allList");
                                if (temp != null){
                                    String textListString = temp.toString();
                                    List<Object> textList = FastJson.getList(textListString);
                                    if (textList != null){
                                        for (int k = 0; k < textList.size(); k++){
                                            if (textList.get(k) instanceof String){
                                                sb.append(textList.get(k).toString());
                                            }else {
                                                continue;
                                            }
                                        }
                                        content = sb.toString();
                                    }
                                }

                                Map<String, Object> dataMap = new HashMap<String, Object>();
                                dataMap.put("title",title);
                                dataMap.put("topic",topic);
                                dataMap.put("time",time);
                                dataMap.put("imageurl",imageurl);
                                dataMap.put("content",content);

                                data.add(dataMap);
                            }
                            adapter.updateData(data);
                        }

                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();


                    }

                    @Override
                    public void onError(int i, String s, Exception e) {
                        Log.i("aaa",s);
                    }
                });

    }


    private void hideMenu() {
        Log.i("aaa","hideMenu");
        ValueAnimator animator = ValueAnimator.ofFloat(0f, height);
        animator.setTarget(bottomNavigationBar);
        animator.setDuration(500);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bottomNavigationBar.setTranslationY((Float) animation.getAnimatedValue());
            }
        });

        isHide = true;
    }

    private void showMenu() {
        Log.i("aaa","showMenu");
        ValueAnimator animator = ValueAnimator.ofFloat(height, 0f);
        animator.setTarget(bottomNavigationBar);
        animator.setDuration(500);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bottomNavigationBar.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        isHide = false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.FAB:
                FAB.startAnimation(animation);
                initData(2 + "");
                break;
        }
    }
}
