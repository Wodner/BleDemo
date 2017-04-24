package cancan.bledemo.utils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 描述：
 * 作者：Wu on 2017/2/22 01:43
 * 邮箱：wuwende@live.cn
 */

public class JsonParser {

    static Gson gson = new Gson();

    public static <T> T parseWithGson(Class<T> classOfT, String jsonStr) {
        return gson.fromJson(jsonStr, classOfT);
    }





    public static String parseWithJSONObjectKey(String key,String json){
        String reslut;
        try {
            JSONObject jsonObject = new JSONObject(json);
            reslut = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            reslut = null;
        }
        return reslut;
    }




}
