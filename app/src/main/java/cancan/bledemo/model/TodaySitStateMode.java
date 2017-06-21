package cancan.bledemo.model;

/**
 * 描述：
 * 作者：Wu on 2017/6/21 22:08
 * 邮箱：wuwende@live.cn
 */

public class TodaySitStateMode {


    /**
     * code : 0
     * month : 6
     * day : 21
     * sitting : 0
     * forward : 0
     * backward : 0
     * leftLeaning : 0
     * rightLeaning : 0
     */

    private String code;
    private int month;
    private int day;
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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
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
