package com.ent.happychat.common.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;


public class SoutheastAsiaNewsTools {

    private static final String API_KEY = "5957e352ccaef83baea710763202a41803589a178013b31a13c9c34402fe2283";

    // 新闻数据结构
    public static class NewsItem {
        public String title;      // 标题
        public String content;    // 正文（优先body，降级用incipit）
        public String imagePath;  // 图片URL
        public String source;     // 来源
        public String area;       // 国家/地区
    }

    // 9个国家关键词配置 {中文关键词, 英文关键词, area名称}
    private static final String[][] COUNTRY_KEYWORDS = {
            {"缅甸",     "缅甸"},
            {"泰国",     "泰国"},
            {"柬埔寨",   "柬埔寨"},
            {"菲律宾",   "菲律宾"},
            {"马来西亚", "马来西亚"},
            {"越南",     "越南"},
            {"迪拜",     "迪拜"},
            {"日本",     "日本"},
            {"新加坡",   "新加坡"},
    };

    /**
     * 抓取所有国家新闻，返回NewsItem列表
     * 每个国家中英文各取5条，合计最多90条，去重后返回
     */
    public static List<NewsItem> fetchAllCountryNews() {
        List<NewsItem> result = new ArrayList<>();
        Set<String> titleSet = new HashSet<>(); // 去重用

        for (String[] kw : COUNTRY_KEYWORDS) {
            String zhKeyword = kw[0];
            String area = kw[1];

            // 中文关键词搜索
            fetchByKeyword(zhKeyword, area, 1, titleSet, result);
            sleep(600);

        }

        return result;
    }

    /**
     * 按关键词搜索并拉取详情，填入result
     */
    private static void fetchByKeyword(String keyword, String area, int pageSize,
                                       Set<String> titleSet, List<NewsItem> result) {
        try {
            String searchUrl = "https://api.freenewsapi.io/v1/news"
                    + "?language=zh"
                    + "&in_title=" + URLEncoder.encode(keyword, "UTF-8")
                    + "&page_size=" + pageSize;

            String searchJson = httpGet(searchUrl);
            List<String> uuids = extractAll(searchJson, "uuid");

            for (String uuid : uuids) {
                try {
                    String detailUrl = "https://api.freenewsapi.io/v1/details?uuid=" + uuid;
                    String detailJson = httpGet(detailUrl);

                    String title = extractOne(detailJson, "title");
                    String body = extractOne(detailJson, "body");
                    String incipit = extractOne(detailJson, "incipit");
                    String thumbnail = extractOne(detailJson, "thumbnail");
                    String publisher = extractOne(detailJson, "publisher");

                    // 标题去重
                    if (title.isEmpty() || titleSet.contains(title)) continue;
                    titleSet.add(title);

                    List<String> keywords = Arrays.asList(
                            "体育","足球","篮球","排球","乒乓球","羽毛球","网球",
                            "球员","球队","教练","运动员","国家队","俱乐部","运动","举办",
                            "世界杯","奥运","亚运","锦标赛","联赛","金牌","摘金","摘银","摘铜",
                            "进球","夺冠","冠军","赛事","比赛","季军",
                            "拳击","UFC","MMA","马拉松","赛车","F1",
                            "泰拳","曼联","利物浦","巴萨","皇马","亚军","银牌","铜牌"
                    );

                    boolean isSports = keywords.stream().anyMatch(title::contains);

                    if(isSports){
                        continue;
                    }

                    NewsItem item = new NewsItem();
                    item.title = title;
                    item.content = body.isEmpty() ? incipit : body; // body优先，没有则用摘要
                    item.content = item.content.replaceAll("<[^>]+>", "").trim();
                    item.imagePath = thumbnail;
                    item.source = publisher;
                    item.area = area;

                    result.add(item);
                    sleep(500);

                } catch (Exception e) {
                    System.err.println("拉取详情失败 uuid=" + uuid + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("搜索失败 keyword=" + keyword + ": " + e.getMessage());
        }
    }

    // ========== 工具方法 ==========

    private static String httpGet(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("x-api-key", API_KEY);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        InputStream is = (conn.getResponseCode() >= 400) ? conn.getErrorStream() : conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        reader.close();
        return sb.toString();
    }

    private static String extractOne(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escape) {
                if (c == 'n') sb.append('\n');
                else if (c == 't') sb.append('\t');
                else sb.append(c);
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static List<String> extractAll(String json, String key) {
        List<String> list = new ArrayList<>();
        String search = "\"" + key + "\":\"";
        int pos = 0;
        while (true) {
            int start = json.indexOf(search, pos);
            if (start == -1) break;
            start += search.length();
            int end = json.indexOf("\"", start);
            if (end == -1) break;
            list.add(json.substring(start, end));
            pos = end + 1;
        }
        return list;
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    // ========== 测试入口 ==========
    public static void main(String[] args) {
        List<NewsItem> newsList = fetchAllCountryNews();
        System.out.println("共获取 " + newsList.size() + " 条新闻\n");

        for (NewsItem item : newsList) {
            System.out.println("【标题】" + item.title);
            System.out.println("【来源】" + item.source);
            System.out.println("【地区】" + item.area);
            System.out.println("【图片】" + item.imagePath);
            System.out.println("【内容长度】" + item.content.length() + " 字");
            System.out.println("----------------------------------------");


            // TODO: 在此处调用你的入库逻辑
            // southeastAsiaService.save(item);
        }
    }
}
