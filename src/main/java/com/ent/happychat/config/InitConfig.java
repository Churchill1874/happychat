package com.ent.happychat.config;
import java.math.BigDecimal;
import com.ent.happychat.common.constant.enums.LevelEnum;
import com.ent.happychat.common.constant.enums.CampEnum;
import java.time.LocalDate;
import com.ent.happychat.common.constant.enums.GenderEnum;
import com.ent.happychat.common.constant.enums.UserStatusEnum;

import com.ent.happychat.common.constant.enums.BotEnum;
import com.ent.happychat.common.constant.enums.ManageRoleEnum;
import com.ent.happychat.common.tools.CodeTools;
import com.ent.happychat.common.tools.GenerateTools;
import com.ent.happychat.common.tools.HttpTools;
import com.ent.happychat.entity.Administrators;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.service.AdministratorsService;
import com.ent.happychat.service.NewsService;
import com.ent.happychat.service.PlayerInfoService;
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
    @Autowired
    private PlayerInfoService playerInfoService;

    /**
     * 项目启动时运行方法
     */
    @PostConstruct
    private void run() {

        Administrators administrators = administratorsService.findByAccount(SUPER_ADMIN_ACCOUNT);
        if (administrators == null) {
            String salt = GenerateTools.getUUID();
            administrators = new Administrators();
            administrators.setAccount(SUPER_ADMIN_ACCOUNT);
            administrators.setName(ManageRoleEnum.SUPER_ADMIN.getName());
            administrators.setRole(ManageRoleEnum.SUPER_ADMIN);
            administrators.setPassword(CodeTools.md5AndSalt(PASSWORD, salt));
            administrators.setSalt(salt);
            administrators.setCreateTime(LocalDateTime.now());
            administratorsService.save(administrators);
            log.info("初始化了管理员账号");
        }

        for(BotEnum bot: BotEnum.values()){
            PlayerInfo playerInfo = playerInfoService.findByAccount(bot.name());
            if(playerInfo == null){
                String salt = GenerateTools.getUUID();
                PlayerInfo botInfo = new PlayerInfo();
                botInfo.setName(bot.getNickname());
                botInfo.setAccount(bot.name());
                botInfo.setPassword(CodeTools.md5AndSalt("11111111p", salt));
                botInfo.setGender(GenderEnum.findByCode(bot.getGender()));
                botInfo.setCity(bot.getRegion());
                botInfo.setBirth(LocalDate.parse(bot.getBirthday()));
                botInfo.setLevel(LevelEnum.LEVEL_0);
                botInfo.setIsBot(true);
                botInfo.setStatus(UserStatusEnum.NORMAL);
                botInfo.setAvatarPath(bot.getAvatar());
                botInfo.setBalance(new BigDecimal("0"));
                botInfo.setSalt(salt);
                botInfo.setAddress("内网");
                botInfo.setIp("127.0.0.1");
                botInfo.setCampType(CampEnum.NO);
                botInfo.setCreateTime(LocalDateTime.now());
                botInfo.setCreateName("系统");
                playerInfoService.save(botInfo);
                log.info("初始化机器人:{}", bot.name());
            }
        }



        //log.info("获取创建机器人开关配置:{}", createBot);
/*        List<News> newsList = NewsTools.getNewsData(NewsCategoryEnum.ENTERTAINMENT,25);
        newsService.saveList(newsList);*/


    }

/*    public static void main(String[] args) {
        log.info(CodeTools.md5AndSalt(PASSWORD));
    }*/

}
