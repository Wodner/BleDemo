package cancan.bledemo.model;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 作者：Wu on 2017/4/24 14:03
 * 邮箱：wuwende@live.cn
 */

public class UserStatusDataModel implements Serializable {


    /**
     * code : 0
     * month : 4
     * day : 24
     * rows : [{"hour":0,"minute":0,"status":1},{"hour":1,"minute":1,"status":2},{"hour":2,"minute":2,"status":3},{"hour":3,"minute":3,"status":4},{"hour":4,"minute":4,"status":1},{"hour":5,"minute":5,"status":2},{"hour":6,"minute":6,"status":3},{"hour":7,"minute":7,"status":4},{"hour":8,"minute":8,"status":1},{"hour":9,"minute":9,"status":2},{"hour":10,"minute":10,"status":3},{"hour":11,"minute":11,"status":4},{"hour":12,"minute":12,"status":1},{"hour":13,"minute":13,"status":2},{"hour":14,"minute":14,"status":3},{"hour":15,"minute":15,"status":4},{"hour":16,"minute":16,"status":1},{"hour":17,"minute":17,"status":2},{"hour":18,"minute":18,"status":3},{"hour":19,"minute":19,"status":4}]
     */

    private int code;
    private int year;
    private int month;
    private int day;
    private List<RowsBean> rows;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Serializable{
        /**
         * hour : 0
         * minute : 0
         * status : 1
         */

        private int hour;
        private int minute;
        private String status;

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
