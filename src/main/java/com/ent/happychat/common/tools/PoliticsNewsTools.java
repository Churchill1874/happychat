package com.ent.happychat.common.tools;
import java.net.*;
import java.io.*;
import java.util.*;
public class PoliticsNewsTools {

    private static final String API_KEY = "5957e352ccaef83baea710763202a41803589a178013b31a13c9c34402fe2283";

    public static class NewsItem {
        public String title;
        public String content;
        public String imagePath;
        public String source;
    }

    private static final String[][] PUBLISHERS = {
            {"13e6a296-f159-49ec-bb3c-6afd71109a16", "BBC"},
            {"41ca1abe-cd4c-4ebd-ac37-d1f63ad26993", "美国之音"},
            {"b976be49-2021-42bb-a774-9ed576b7420b", "日经中文网"},
            {"d8689f7c-46d1-4fec-949c-0d3771e35c2e", "纽约时报中文网"},
    };

    public static List<NewsItem> fetchAllPoliticsNews() {
        List<NewsItem> result = new ArrayList<>();
        Set<String> titleSet = new HashSet<>();

        for (String[] publisher : PUBLISHERS) {
            String uuid = publisher[0];
            String name = publisher[1];
            fetchByPublisher(uuid, name, 10, titleSet, result);
            sleep(600);
        }

        return result;
    }

    private static void fetchByPublisher(String publisherUuid, String publisherName, int pageSize,
                                         Set<String> titleSet, List<NewsItem> result) {
        try {
            String searchUrl = "https://api.freenewsapi.io/v1/news"
                    + "?publisher_uuid=" + publisherUuid
                    + "&language=zh"
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

                    if (title.isEmpty() || titleSet.contains(title)) continue;
                    titleSet.add(title);

                    NewsItem item = new NewsItem();
                    item.title     = title;
                    item.content   = (body.isEmpty() ? incipit : body)
                            .replaceAll("<[^>]+>", "").trim();
                    item.imagePath = thumbnail;
                    item.source    = publisherName;

                    result.add(item);
                    sleep(500);

                } catch (Exception e) {
                    System.err.println("详情失败: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("搜索失败 publisher=" + publisherName + ": " + e.getMessage());
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
        List<NewsItem> newsList = fetchAllPoliticsNews();
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
