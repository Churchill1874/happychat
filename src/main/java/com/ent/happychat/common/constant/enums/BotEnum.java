package com.ent.happychat.common.constant.enums;

import lombok.Getter;

@Getter
public enum BotEnum {

    jiushishuo("1","就是说",  "广东", 1, "1996-03-15"),
    bietaizao("10","别太早", "福建", 1, "2000-07-22"),
    YouDianDongXi("5","有点东西", "云南", 1, "1993-11-08"),
    XingYiXing("14","醒一醒", "广西", 0, "1998-05-30"),
    butaileng("3","不太冷", "湖南", 1, "2002-01-17"),
    HuiXiang("7","回响", "四川", 1, "1983-09-04"),
    tutu123("5","突突", "贵州", 1, "1991-12-25"),
    FeiDian("1","沸点", "广东", 0, "1999-06-11"),
    jiujiuxin("1","99新", "福建", 1, "1997-04-03"),
    JinRiFenLiPu("1","今日份离谱", "云南", 0, "2001-08-19"),
    FaFaFaFa("2","发发发发", "广西", 1, "1994-02-28"),
    SSS("1","SSS", "湖南", 0, "1980-10-14"),
    bibibi("1","哔哔哔你妈啊哔哔", "四川", 1, "1989-07-07"),
    Tom("1","Tom", "广东", 1, "1998-03-21"),
    huanhuanhuanlelele("1","欢欢欢欢乐乐乐乐", "福建", 0, "2000-11-05"),
    Mike("1","mike", "云南", 1, "1992-05-16"),
    Curry("4","curry", "广西", 1, "1996-08-30"),
    aiyouei("13","哎呦欸", "湖南", 0, "2002-04-09"),
    Lucy("12","lucy", "四川", 0, "1997-01-23"),
    Linda("11","linda", "贵州", 0, "1988-06-18");

    private final String avatar;
    private final String nickname;
    private final String region;
    private final int gender;
    private final String birthday;

    BotEnum(String avatar, String nickname, String region, int gender, String birthday) {
        this.avatar = avatar;
        this.nickname = nickname;
        this.region = region;
        this.gender = gender;
        this.birthday = birthday;
    }

    // 随机获取一个账号
    public static BotEnum random() {
        BotEnum[] values = BotEnum.values();
        return values[new java.util.Random().nextInt(values.length)];
    }

}
