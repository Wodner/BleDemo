package cancan.bledemo.model;

/**
 * 描述：
 * 作者：Wu on 2017/4/24 18:53
 * 邮箱：wuwende@live.cn
 */

public class FirmwareModel {


    /**
     * code : 0
     * batery : 2%
     * monitorflag : F5
     * firmwareversion : WZP01.0.1
     */

    private String code;
    private String batery;
    private String monitorflag;
    private String firmwareversion;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBatery() {
        return batery;
    }

    public void setBatery(String batery) {
        this.batery = batery;
    }

    public String getMonitorflag() {
        return monitorflag;
    }

    public void setMonitorflag(String monitorflag) {
        this.monitorflag = monitorflag;
    }

    public String getFirmwareversion() {
        return firmwareversion;
    }

    public void setFirmwareversion(String firmwareversion) {
        this.firmwareversion = firmwareversion;
    }
}
