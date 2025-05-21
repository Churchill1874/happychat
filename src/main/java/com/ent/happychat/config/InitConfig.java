package com.ent.happychat.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ent.happychat.common.constant.enums.ManageRoleEnum;
import com.ent.happychat.common.tools.CodeTools;
import com.ent.happychat.common.tools.GenerateTools;
import com.ent.happychat.entity.Administrators;
import com.ent.happychat.entity.News;
import com.ent.happychat.service.AdministratorsService;
import com.ent.happychat.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            String salt = GenerateTools.getUUID();
            administrators = new Administrators();
            administrators.setAccount(SUPER_ADMIN_ACCOUNT);
            administrators.setName(ManageRoleEnum.SUPER_ADMIN.getName());
            administrators.setRole(ManageRoleEnum.SUPER_ADMIN);
            administrators.setPassword(CodeTools.md5AndSalt(PASSWORD,salt));
            administrators.setSalt(salt);
            administrators.setCreateTime(LocalDateTime.now());
            administratorsService.save(administrators);
        }


        //log.info("获取创建机器人开关配置:{}", createBot);
/*        List<News> newsList = NewsTools.getNewsData(NewsCategoryEnum.ENTERTAINMENT,25);
        newsService.saveList(newsList);*/
    }

/*    public static void main(String[] args) {
        log.info(CodeTools.md5AndSalt(PASSWORD));
    }*/

}
