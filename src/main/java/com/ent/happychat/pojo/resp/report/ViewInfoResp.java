package com.ent.happychat.pojo.resp.report;

import lombok.Data;

@Data
public class ViewInfoResp {

    private Integer todayView = 0;
    private Integer yesterdayView = 0;
    private String dayViewRate;

    private Integer thisWeekView = 0;
    private Integer lastWeekView = 0;
    private String weekViewRate;

    private Integer thisMonthView = 0;
    private Integer lastMonthView = 0;
    private String monthViewRate;


    /**
     * 数据赋值完成后调用此方法，统一计算日/周/月环比增长率
     * 使用示例：
     * resp.setTodayRegister(100);
     * resp.setYesterdayRegister(80);
     * ... 其他字段赋值 ...
     * resp.calcRates();
     */
    public void calcRates() {
        this.dayViewRate = calcRate(todayView, yesterdayView);
        this.weekViewRate = calcRate(thisWeekView, lastWeekView);
        this.monthViewRate = calcRate(thisMonthView, lastMonthView);
    }

    /**
     * 计算环比增长率
     *
     * @param current  当期数量
     * @param previous 上期数量
     * @return 示例：↑ +20.00%  /  ↓ -20.00%  /  0.00%  /  --
     */
    private String calcRate(Integer current, Integer previous) {
        // 上期为 0 时无法计算比率
        if (previous == null || previous == 0) {
            return (current == null || current == 0) ? "0.00%" : "↑+100.00%";
        }
        double rate = (double) (current - previous) / previous * 100;
        if (rate > 0) {
            return String.format("↑ +%.2f%%", rate);
        } else if (rate < 0) {
            return String.format("↓ -%.2f%%", Math.abs(rate));
        } else {
            return "0.00%";
        }
    }

}
