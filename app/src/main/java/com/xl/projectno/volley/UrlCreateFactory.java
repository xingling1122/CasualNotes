package com.xl.projectno.volley;

import java.util.Random;

public class UrlCreateFactory {
    /**
     * 随机数对象，用于生成请求URL后面的随机数
     */
    public static Random mRandom = new Random(System.currentTimeMillis());

    public static String getRequestUrl(int shopType) {
        String url = "";
        switch (shopType) {
            case ShopType.API_REGISTER:
                url = getUrl("register");
                break;
            case ShopType.API_LOGIN:
                url = getUrl("login");
                break;
            case ShopType.API_DATA_GET:
                url = getUrl("basicdata");
                break;
            case ShopType.API_DATA_UPLOAD:
                url = getUrl("uploadBill");
                break;
            case ShopType.API_DATA_LOGFILE:
                url = getUrl("log");
                break;
            case ShopType.API_DATA_PAY:
                url = getUrl("wepay/micropay");
                break;
            case ShopType.API_KEEPLIVE:
                url = getUrl("keeplive");
                break;
            case ShopType.API_LOGOUT:
                url = getUrl("logout");
                break;
            case ShopType.API_UPLOADIMAGE:
                url = getUrl("uploadimage");
                break;
            case ShopType.API_CHANGEINFO:
                url = getUrl("info");
                break;
            case ShopType.API_ALTERPS:
                url = getUrl("alterps");
                break;
            case ShopType.API_RESETPS:
                url = getUrl("resetps");
                break;
            case ShopType.API_GENERATECODE:
                url = getUrl("generatecode");
                break;
            default:
                break;
        }
        return url;

    }

    // 注意：该字段的值需与统计协议20-106（GO输入法商店进入后所处位置代码表）相对应
    public interface ShopType {

        int API_GENERATECODE = 92;
        int API_RESETPS = 93;
        int API_ALTERPS = 94;
        int API_CHANGEINFO = 95;
        int API_UPLOADIMAGE = 96;
        int API_KEEPLIVE =97;
        int API_LOGOUT = 98;
        int API_REGISTER = 99;
        int API_LOGIN = 100;
        int API_DATA_GET = 101;
        int API_DATA_UPLOAD = 102;
        int API_DATA_LOGFILE = 103;
        int API_DATA_PAY = 104;
    }

    /**
     * <br>
     * 功能简述:获取各模块的数据的url <br>
     * 功能详细描述: <br>
     * 注意:
     *
     * @param context
     * @return
     */
    public static String getUrl(String name) {
        String host = GoPluginEnv.Server.HOST;
//        String host = GoPluginEnv.Server.HOST_TEST;
        String url = host + name;
        return url;
    }


}
