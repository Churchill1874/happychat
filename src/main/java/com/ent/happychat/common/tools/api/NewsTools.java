package com.ent.happychat.common.tools.api;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ent.happychat.common.constant.enums.NewsCategoryEnum;
import com.ent.happychat.common.constant.enums.NewsStatusEnum;
import com.ent.happychat.entity.News;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NewsTools {

    //appkey
    public static final String APPKEY = "b7801cbd24c02a19";
    //密钥
    private static String APP_SECRET = "32KIqRBakT9HyKoQCUrg05ivxDZgtv6Z";
    public static final String URL = "https://api.jisuapi.com/news/get";
    public static final String CATEGORY = "头条";// utf8  新闻频道(头条,财经,体育,娱乐,军事,教育,科技,NBA,股票,星座,女性,健康,育儿)

    public static List<News> getNewsData(NewsCategoryEnum categoryEnum, int size) {
        List<News> newsList = new ArrayList<>();

        //请求极速新闻的响应结果
        String result = null;
        //加密后的新闻通道
        String encodedChannel = null;
        try {
            encodedChannel = URLEncoder.encode(categoryEnum.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("新闻类型转换请求编码报错");
            e.printStackTrace();
        }

        String url = URL + "?channel=" + encodedChannel + "&num=" + size + "&appkey=" + APPKEY;
        log.info("请求极速新闻信息:{}", url);

        try {
            result = HttpUtil.get(url);
            JSONObject json = new JSONObject(result);
            if (json.getInt("status") != 0) {
                log.info(json.getStr("msg"));
            } else {
                JSONObject resultarr = json.getJSONObject("result");
                String channel = resultarr.getStr("channel");
                String num = resultarr.getStr("num");
                log.info(channel + " " + num);
                JSONArray list = resultarr.getJSONArray("list");
                if (CollectionUtils.isNotEmpty(list)) {
                    for (int i = 0; i < list.size(); i++) {
                        JSONObject obj = list.getJSONObject(i);
                        News news = new News();
                        news.setTitle(obj.getStr("title"));
                        news.setContent(obj.getStr("content"));
                        news.setSource(obj.getStr("src"));
                        news.setCategory(categoryEnum);
                        news.setPhotoPath(obj.getStr("pic"));
                        news.setUrl(obj.getStr("url"));
                        news.setNewsStatus(NewsStatusEnum.NORMAL);
                        //将返回的新闻发生时间转换成localdatetime格式
                        String time = obj.getStr("time");
                        if (StringUtils.isNotBlank(time)){
                            if (time.contains(" ")){
                                news.setNewsTime(LocalDateTime.parse(time.replace(" ","T")));
                            }
                            if (!time.contains(" ")){
                                news.setNewsTime(LocalDate.parse(time).atStartOfDay());
                            }
                        }
                        news.setCreateTime(LocalDateTime.now());
                        newsList.add(news);
                    }
                } else {
                    log.error("请求新闻接口未获取到数据");
                }
            }
        } catch (Exception e) {
            log.error("请求极速获取新闻异常:{}", e.getMessage());
            e.printStackTrace();
        }

        return newsList;
    }

/*
    public static void main(String[] args) {
        getNewsData(NewsCategoryEnum.NEWS, 20);
    }*/
}
