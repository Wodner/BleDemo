package cancan.bledemo.model;

import java.io.Serializable;

/**
 * 描述：
 * 作者：Wu on 2017/5/9 00:05
 * 邮箱：wuwende@live.cn
 */

public class SittingStatusDataModel implements Serializable {


    /**
     * code : 0
     * year : 2257
     * month : 5
     * day : 5
     * sitting : 100
     * forward : 200
     * backward : 300
     * leftLeaning : 400
     * rightLeaning : 500
     */

    private String code;
    private String year;
    private String month;
    private String day;
    private int sitting;
    private int forward;
    private int backward;
    private int leftLeaning;
    private int rightLeaning;

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

    public int getSitting() {
        return sitting;
    }

    public void setSitting(int sitting) {
        this.sitting = sitting;
    }

    public int getForward() {
        return forward;
    }

    public void setForward(int forward) {
        this.forward = forward;
    }

    public int getBackward() {
        return backward;
    }

    public void setBackward(int backward) {
        this.backward = backward;
    }

    public int getLeftLeaning() {
        return leftLeaning;
    }

    public void setLeftLeaning(int leftLeaning) {
        this.leftLeaning = leftLeaning;
    }

    public int getRightLeaning() {
        return rightLeaning;
    }

    public void setRightLeaning(int rightLeaning) {
        this.rightLeaning = rightLeaning;
    }
}
