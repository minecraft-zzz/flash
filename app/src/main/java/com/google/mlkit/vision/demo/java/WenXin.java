package com.google.mlkit.vision.demo.java;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;


/**主要用于实现对接文心一言API的功能
 */
public class WenXin{

    public String API_KEY = "QEIGdgKkKtVcgAryEoBi9Qn5";
    public String SECRET_KEY = "HiP4fFcqYHlVQonGtmbKx3dD2df5YnzZ";

    public String AI_Role="";
    private SharedPreferences preferences;
    View view;

    public JSONArray Dialogue_Content;//用来储存对话内容，当然初始是空的

    Gson gson = new Gson();

    /**
     * WenXin类的初始化设置，设置好apikey等参数，以向服务器发送信息，airole参数的作用是自定义ai助手的角色功能类型等
     * @param apikey
     * @param secrectkey
     * @param airole
     */
    WenXin(String apikey,String secrectkey,String airole){
        Dialogue_Content=new JSONArray();
        //初始化
        API_KEY=apikey;
        SECRET_KEY=secrectkey;
        AI_Role=airole;
    }

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public String GetAnswer(String user_msg) throws IOException, JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("role", "user");
        jsonObject.put("content", user_msg);

        try{
            // 将JSONObject添加到JSONArray中
            Dialogue_Content.put(jsonObject);

            MediaType mediaType = MediaType.parse("application/json");
            //这是一行参考代码，只能进行一次对话，要想多次对话就必须动态调整content的内容
            //RequestBody body = RequestBody.create(mediaType,  "{\"messages\":[{\"role\":\"user\",\"content\":\"你好啊\"}],\"disable_search\":false,\"enable_citation\":false}");

            RequestBody body = RequestBody.create(mediaType,  "{\"messages\":" +
                    Dialogue_Content.toString() +
                    ",\"system\":\""+AI_Role+"\",\"disable_search\":false,\"enable_citation\":false}");
            //预先设定AI的角色功能啥的


            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token=" +
                            getAccessToken())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();

            //解析出文心一言的回答
            JSONObject json_feedback = new JSONObject(response.body().string());
            String re=json_feedback.getString("result");
            //把文心一言的回答加入到Dialogue_Content中
            JSONObject jsontmp=new JSONObject();
            jsontmp.put("assistant",re);
            Dialogue_Content.put(jsontmp);

            return re;

        }catch(java.net.SocketTimeoutException e){
            return "获取信息失败";
        }

    }

    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    public String getAccessToken() throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }



}
