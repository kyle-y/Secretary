package com.example.yxb.secretary.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/1.
 */
public class FastJson {
    public static Map<String,Object> getMapObj(String json){
        return JSON.parseObject(json,new TypeReference<Map<String, Object>>(){});
    }

    public static List<Map<String,Object>> getListMap(String json){
        return JSON.parseObject(json,new TypeReference<List<Map<String, Object>>>(){});
    }

    public static List<Object> getList(String json){
        return JSON.parseObject(json,new TypeReference<List<Object>>(){});
    }
}
