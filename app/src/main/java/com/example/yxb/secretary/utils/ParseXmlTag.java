package com.example.yxb.secretary.utils;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.utils
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/19
 * MODIFY_BY:
 */
public class ParseXmlTag {
    public static Object[] getObjectArray(String target, String tag){
        org.jsoup.nodes.Document doc = Jsoup.parse(target);
        Elements element = doc.body().getElementsByTag(tag);
        Object[] objArr = element.toArray();
        return objArr;
    }
}
