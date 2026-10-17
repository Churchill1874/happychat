// 目标路径: src/main/java/com/ent/happychat/controller/player/SitemapController.java
// 完整文件，直接整份覆盖替换。在原文件基础上只加了 telegram 相关的几处（都标了"电报："注释），
// 其余 southeastAsia/politics/society/exposure/topic/company 六个原有类型一字未改。
package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ent.happychat.entity.*;
import com.ent.happychat.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SitemapController {


    private static final String BASE_URL = "https://grayasia.com";
    private static final int PAGE_SIZE = 500;

    @Autowired
    private PoliticsMapper politicsMapper;
    @Autowired
    private SoutheastAsiaMapper southeastAsiaMapper;
    @Autowired
    private SocietyMapper societyMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private ExposureMapper exposureMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private TelegramMapper telegramMapper; // 电报：注入mapper
    @Autowired
    private VoteMapper voteMapper; // 投票：注入mapper

    @GetMapping(value = "/sitemap.xml", produces = "application/xml;charset=UTF-8")
    public void sitemapIndex(HttpServletResponse response) throws IOException {
        response.setContentType("application/xml;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        writeIndex(writer, "sea", pages(southeastAsiaMapper.selectCount(null)));
        writeIndex(writer, "politics", pages(politicsMapper.selectCount(null)));
        writeIndex(writer, "society", pages(societyMapper.selectCount(null)));
        writeIndex(writer, "exposure", pages(exposureMapper.selectCount(null)));
        writeIndex(writer, "topic", pages(topicMapper.selectCount(null)));
        writeIndex(writer, "company", pages(companyMapper.selectCount(null)));
        // 电报：加入 sitemap 索引，只统计已上架（status=true）的条数
        writeIndex(writer, "telegram", pages(telegramMapper.selectCount(
                new LambdaQueryWrapper<Telegram>().eq(Telegram::getStatus, true))));
        // 投票：只统计展示中（status=true）的投票
        writeIndex(writer, "vote", pages(voteMapper.selectCount(
                new LambdaQueryWrapper<Vote>().eq(Vote::getStatus, true))));

        writer.println("</sitemapindex>");
        writer.flush();
    }

    @GetMapping(value = "/sitemap-{type}-{page}.xml", produces = "application/xml;charset=UTF-8")
    public void sitemapSub(
            @PathVariable String type,
            @PathVariable int page,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/xml;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        int offset = (page - 1) * PAGE_SIZE;
        String last = "LIMIT " + PAGE_SIZE + " OFFSET " + offset;
        String path = getPath(type);

        List<String> ids;
        switch (type) {
            case "sea":
                ids = southeastAsiaMapper.selectList(
                        new LambdaQueryWrapper<SoutheastAsia>().select(SoutheastAsia::getId).last(last)
                ).stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
                break;
            case "politics":
                ids = politicsMapper.selectList(
                        new LambdaQueryWrapper<Politics>().select(Politics::getId).last(last)
                ).stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
                break;
            case "society":
                ids = societyMapper.selectList(
                        new LambdaQueryWrapper<Society>().select(Society::getId).last(last)
                ).stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
                break;
            case "exposure":
                ids = exposureMapper.selectList(
                        new LambdaQueryWrapper<Exposure>().select(Exposure::getId).last(last)
                ).stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
                break;
            case "topic":
                ids = topicMapper.selectList(
                        new LambdaQueryWrapper<Topic>().select(Topic::getId).last(last)
                ).stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
                break;
            case "company":
                ids = companyMapper.selectList(
                        new LambdaQueryWrapper<Company>().select(Company::getId).last(last)
                ).stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
                break;
            case "telegram": // 电报：只把已上架的条目列进sitemap，下架/草稿不给谷歌
                ids = telegramMapper.selectList(
                        new LambdaQueryWrapper<Telegram>().select(Telegram::getId).eq(Telegram::getStatus, true).last(last)
                ).stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
                break;
            case "vote": // 投票：只把展示中的投票列进sitemap
                ids = voteMapper.selectList(
                        new LambdaQueryWrapper<Vote>().select(Vote::getId).eq(Vote::getStatus, true).last(last)
                ).stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
                break;
            default:
                ids = Collections.emptyList();
        }

        for (String id : ids) {
            writer.println("<url><loc>" + BASE_URL + path + id + "</loc>" +
                    "<priority>0.8</priority><changefreq>weekly</changefreq></url>");
        }

        writer.println("</urlset>");
        writer.flush();
    }

    private void writeIndex(PrintWriter writer, String type, int pages) {
        for (int i = 1; i <= pages; i++) {
            writer.println("<sitemap><loc>" + BASE_URL + "/sitemap-" + type + "-" + i + ".xml</loc></sitemap>");
        }
    }

    private int pages(long total) {
        return (int) Math.max(1, Math.ceil((double) total / PAGE_SIZE));
    }

    private String getPath(String type) {
        switch (type) {
            case "sea":
                return "/southeastAsia/";
            case "politics":
                return "/politics/";
            case "society":
                return "/society/";
            case "exposure":
                return "/exposure/";
            case "topic":
                return "/topic/";
            case "company":
                return "/company/";
            case "telegram": // 电报：对应前端详情页路由 /telegram/:id
                return "/telegram/";
            case "vote": // 投票：对应前端详情页路由 /vote/:id
                return "/vote/";
            default:
                return "/";
        }
    }

}