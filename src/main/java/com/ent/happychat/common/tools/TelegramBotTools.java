package com.ent.happychat.common.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TelegramBotTools {
    private static final String TG_API = "https://api.telegram.org/bot";

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.channel-id}")
    private String channelId;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 发送消息到频道（支持HTML格式）
     */
    public boolean sendMessage(String text) {
        String url = TG_API + botToken + "/sendMessage";

        Map<String, Object> params = new HashMap<>();
        params.put("chat_id", channelId);
        params.put("text", text);
        params.put("parse_mode", "HTML");
        params.put("disable_web_page_preview", false);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, params, Map.class);
            boolean ok = Boolean.TRUE.equals(((Map) response.getBody()).get("ok"));
            if (!ok) {
                log.error("TG发送失败: {}", response.getBody());
            }
            return ok;
        } catch (Exception e) {
            log.error("TG发送异常: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 发送带图片的消息
     */
    public boolean sendPhoto(String imageUrl, String caption) {
        String url = TG_API + botToken + "/sendPhoto";

        Map<String, Object> params = new HashMap<>();
        params.put("chat_id", channelId);
        params.put("photo", imageUrl);
        params.put("caption", caption);
        params.put("parse_mode", "HTML");

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, params, Map.class);
            return Boolean.TRUE.equals(((Map) response.getBody()).get("ok"));
        } catch (Exception e) {
            log.error("TG发送图片异常: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 发送新闻（标题加粗 + 换行 + 正文 + 可选链接）
     *
     * @param title      新闻标题
     * @param content    正文内容（会自动截取前500字）
     * @param source     来源，如 "BBC中文"，传null则不显示
     * @param originalUrl 原文链接，传null则不显示
     */
    public boolean sendNews(String title, String content, String source, String type, String originalUrl) {
        StringBuilder sb = new StringBuilder();

        // 标题加粗，后面空一行
        sb.append("\uD83D\uDCCC <b>").append(escapeHtml(title)).append("</b>");
        sb.append("\n\n");

        // 正文截取前500字
        if (content != null && !content.isEmpty()) {
            String trimmed = content.length() > 3000 ? content.substring(0, 3000) + "..." : content;
            sb.append(escapeHtml(trimmed));
            sb.append("\n\n");
        }

        if (StringUtils.isNotBlank(type)){
            sb.append("类型：").append(escapeHtml(type));
        }

        // 来源
        if (source != null && !source.isEmpty()) {
            sb.append(" 📰来源: ").append("<i>").append(escapeHtml(source)).append("</i>").append("\n\n\n");
        }

        // 原文链接
        if (originalUrl != null && !originalUrl.isEmpty()) {
            sb.append("<a href=\"").append(originalUrl).append("\">📖 阅读原文</a>");
        }


        sb.append("\uD83D\uDCE3").append("<b>").append("投搞爆料:").append("</b>").append("@grayasia");
        sb.append("\n\n<i>#灰亚新闻 专属东南亚灰产圈的新闻咨询网站</i>");
        sb.append("\n\n<i>www.grayasia.com</i>");


        return sendMessage(sb.toString());
    }

    /**
     * HTML特殊字符转义，防止parse_mode=HTML时报错
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}