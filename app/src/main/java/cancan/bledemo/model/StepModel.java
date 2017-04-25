package cancan.bledemo.model;

import java.io.Serializable;

/**
 * 描述：
 * 作者：Wu on 2017/4/24 21:36
 * 邮箱：wuwende@live.cn
 */

public class  StepModel implements Serializable {


    /**
     * code : 0
     * year : 2017
     * month : 4
     * day : 24
     * hour : 21
     * minute : 33
     * second : 39
     * step : 0
     */

    private String code;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String second;
    private int step;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
