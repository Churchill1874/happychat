package com.ent.happychat.common.constant;

import lombok.Getter;

/**
 * 足球赛事系列常量
 */
public enum FootballMatchSeriesEnum {

    WORLD_CUP("WORLD CUP", "世界杯"),

    WORLD_CUP_WOMAN("WORLD CUP WOMAN", "女足世界杯"),

    FIFA_WORLD_CUP_QUALIFIERS("FIFA WORLD CUP QUALIFIERS", "世界杯预选赛"),

    FIFA_WORLD_CUP_QUALIFIERS_WOMAN("FIFA WORLD CUP QUALIFIERS WOMAN", "女足世界杯预选赛"),

    IFC("IFC", "国际友谊赛"),

    IFC_WOMAN("IFC WOMAN", "女足国际友谊赛"),

    AFC_ASIAN_CUP("AFC ASIAN CUP", "亚洲杯"),

    AFC_ASIAN_CUP_WOMAN("AFC ASIAN CUP WOMAN", "女足亚洲杯"),

    AFC_ASIAN_CUP_QUALIFIERS("AFC ASIAN CUP QUALIFIERS", "亚洲杯预选赛"),

    AFC_ASIAN_CUP_QUALIFIERS_WOMAN("AFC ASIAN CUP QUALIFIERS WOMAN", "女足亚洲杯预选赛"),

    SPANISH_LA_LIGA("SPANISH LA LIGA", "西甲"),

    ENGLISH_PREMIER_LEAGUE("ENGLISH PREMIER LEAGUE", "英超"),

    SERIE_A("SERIE A", "意甲"),

    EREDIVISIE("EREDIVISIE", "荷甲"),

    BUNDESLIGA("BUNDESLIGA", "德甲"),

    PRIMEIRA_LIGA("PRIMEIRA LIGA", "葡甲"),

    LIGUE_1("LIGUE 1", "法甲"),

    BRAZILIAN_SERIE_A("BRAZILIAN SERIE A", "巴甲"),

    CHINESE_SUPER_LEAGUE("CHINESE SUPER LEAGUE", "中超");

    @Getter
    private String english;

    @Getter
    private String chinese;

    FootballMatchSeriesEnum(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    /**
     * 根据赛事系列中文查找枚举
     * @param chinese
     * @return
     */
    public FootballMatchSeriesEnum findByChinese(String chinese) {
        for (FootballMatchSeriesEnum footballMatchSeriesEnum : FootballMatchSeriesEnum.values()) {
            if (footballMatchSeriesEnum.getChinese().equals(chinese)){
                return footballMatchSeriesEnum;
            }
        }
        return null;
    }

}
