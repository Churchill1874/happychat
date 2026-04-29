package com.ent.happychat.common.tools;
import java.net.*;
import java.io.*;
import java.util.*;

public class SocietyNewsTools {

    private static final String API_KEY = "5957e352ccaef83baea710763202a41803589a178013b31a13c9c34402fe2283";

    public static class NewsItem {
        public String title;
        public String content;
        public String imagePath;
        public String source;
        public String area;
    }

    private static final String[] KEYWORDS = {
            // 冲突对抗类
            "抗议", "游行", "示威", "暴动", "骚乱", "冲突", "镇压",
            // 犯罪违法类
            "犯罪", "骗局", "诈骗", "腐败", "贪污", "走私", "贩毒",
            "性侵", "强奸", "猥亵", "骚扰", "杀人", "凶杀", "谋杀",
            // 社会问题类
            "歧视", "种族", "维权", "上访", "丑闻", "曝光", "举报",
            "失业", "裁员", "欠薪", "讨薪", "罢工", "罢课",
            // 灾难事故类
            "爆炸", "火灾", "洪水", "地震", "空难", "车祸", "沉船",
            // 公共卫生类
            "疫情", "病毒", "传染病", "食品安全", "污染",
            // 人权自由类
            "人权", "言论自由", "新闻自由", "审查", "封锁", "监控",
            // 移民难民类
            "移民", "难民", "偷渡", "驱逐", "遣返",
            // 女性议题
            "女权", "性别歧视", "家暴", "拐卖", "强迫婚姻",
            // 劳工议题
            "劳工", "血汗工厂", "强迫劳动", "童工",
            // 其他社会热点
            "自杀", "抑郁", "贫困", "流浪", "老龄化",
    };

    public static List<NewsItem> fetchAllSocietyNews() {
        List<NewsItem> result = new ArrayList<>();
        Set<String> titleSet = new HashSet<>();

        for (String keyword : KEYWORDS) {
            fetchByKeyword(keyword, 3, titleSet, result);
            sleep(500);
        }

        return result;
    }

    private static void fetchByKeyword(String keyword, int pageSize,
                                       Set<String> titleSet, List<NewsItem> result) {
        try {
            String searchUrl = "https://api.freenewsapi.io/v1/news"
                    + "?language=zh"
                    + "&in_title=" + URLEncoder.encode(keyword, "UTF-8")
                    + "&page_size=" + pageSize
                    + "&order_by=recent";

            String searchJson = httpGet(searchUrl);
            List<String> uuids = extractAll(searchJson, "uuid");

            for (String uuid : uuids) {
                try {
                    String detailJson = httpGet("https://api.freenewsapi.io/v1/details?uuid=" + uuid);

                    String title     = extractOne(detailJson, "title");
                    String body      = extractOne(detailJson, "body");
                    String incipit   = extractOne(detailJson, "incipit");
                    String thumbnail = extractOne(detailJson, "thumbnail");
                    String publisher = extractOne(detailJson, "publisher");

                    if (title.isEmpty() || titleSet.contains(title)) continue;
                    titleSet.add(title);

                    NewsItem item = new NewsItem();
                    item.title     = title;
                    item.content   = (body.isEmpty() ? incipit : body)
                            .replaceAll("<[^>]+>", "").trim();
                    item.imagePath = thumbnail;
                    item.source    = publisher;
                    item.area      = "社会";

                    result.add(item);
                    sleep(500);

                } catch (Exception e) {
                    System.err.println("详情失败: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("搜索失败 keyword=" + keyword + ": " + e.getMessage());
        }
    }

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
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    public static void main(String[] args) {
        List<NewsItem> newsList = fetchAllSocietyNews();
        System.out.println("共获取 " + newsList.size() + " 条\n");

        for (NewsItem item : newsList) {
            System.out.println("【标题】" + item.title);
            System.out.println("【来源】" + item.source);
            System.out.println("【图片】" + item.imagePath);
            System.out.println("【内容长度】" + item.content.length() + " 字");
            System.out.println("----------------------------------------");

            // TODO: 入库逻辑
        }
    }
}