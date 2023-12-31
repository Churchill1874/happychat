package com.ent.happychat.config;

import com.ent.happychat.common.constant.enums.RoleEnum;
import com.ent.happychat.entity.Administrators;
import com.ent.happychat.service.AdministratorsService;
import com.ent.happychat.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@Component
public class InitConfig {

    @Autowired
    private AdministratorsService administratorsService;

    //超管管理员账号
    private static final String SUPER_ADMIN_ACCOUNT = "admin";

    private static final String PASSWORD = "111111a";

    //获取创建机器人开关
    @Value("${init.create.bot}")
    private boolean createBot;

    @Autowired
    private NewsService newsService;

    /**
     * 项目启动时运行方法
     */
    @PostConstruct
    private void run() {

        Administrators administrators = administratorsService.findByAccount(SUPER_ADMIN_ACCOUNT);
        if (administrators == null){
            administrators = new Administrators();
            administrators.setAccount(SUPER_ADMIN_ACCOUNT);
            administrators.setName(RoleEnum.SUPER_ADMIN.getName());
            administrators.setRole(RoleEnum.SUPER_ADMIN);
            administrators.setPassword(PASSWORD);
            administrators.setCreateTime(LocalDateTime.now());
            administratorsService.save(administrators);
        }


        //log.info("获取创建机器人开关配置:{}", createBot);
/*        List<News> newsList = NewsTools.getNewsData(NewsCategoryEnum.ENTERTAINMENT,25);
        newsService.saveList(newsList);*/
    }

}
