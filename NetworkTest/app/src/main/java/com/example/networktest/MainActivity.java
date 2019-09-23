package com.example.networktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, HttpCallbackListener, okhttp3.Callback {
    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {
            //sendRequestWithHttpURLConnection(); //非回调用法

            //HttpURLConnection使用回调的方法一,创建回调接口实例
//            HttpUtil.sendHttpRequest("http://192.168.2.62:8080/get_data.json", new HttpCallbackListener() {
//                @Override
//                public void onFinish(String response) {
//                    parseJSONWithGSON(response);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    System.out.println("网络请求错误");
//                }
//            });

            //HttpURLConnection使用回调的方法二,实现回调接口
            //  HttpUtil.sendHttpRequest("http://192.168.2.62:8080/get_data.json",this);

            //OkHttp使用回调的方法一,创建回调接口实例
//            HttpUtil.sendOkHttpRequest("http://192.168.2.62:8080/get_data.json", new okhttp3.Callback() {
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    parseJSONWithGSON(response.body().string());
//                }
//
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    System.out.println("网络请求错误");
//                }
//            });

            //OkHttp使用回调的方法二,实现回调接口
            HttpUtil.sendOkHttpRequest("http://192.168.2.62:8080/get_data.json",this);

        }
    }

    //HttpURLConnection的回调方法
    @Override
    public void onFinish(String response) {
        parseJSONWithGSON(response);
    }

    //HttpURLConnection的回调方法
    @Override
    public void onError(Exception e) {
        System.out.println("网络请求错误");
    }

    //OkHttp的回调方法
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        parseJSONWithGSON(response.body().string());

    }

    //OkHttp的回调方法
    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println("网络请求错误");
    }


    private void sendRequestWithHttpURLConnection() {
        // 开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //    .url("http://192.168.2.62:8080/get_data.xml")
                            .url("http://192.168.2.62:8080/get_data.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();


                    parseJSONWithGSON(responseData);//使用GSON解析json
                    //   parseJSONWithJSONObject(responseData);//使用JSONObject解析json
                    //  parseXMLWithSAX(responseData);//使用SAX解析XML
                    //parseXMLWithPull(responseData);//使用pull解析XML方式
                    //showResponse(responseData);//普通显示

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                responseText.setText(response);
            }
        });
    }


    //使用pull解析XML的方法
    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    // 开始解析某个节点
                    case XmlPullParser.START_TAG: {
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    // 完成解析某个节点
                    case XmlPullParser.END_TAG: {
                        if ("app".equals(nodeName)) {
                            Log.d("MainActivity", "id is " + id);
                            Log.d("MainActivity", "name is " + name);
                            Log.d("MainActivity", "version is " + version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //使用SAX解析XML的方法
    private void parseXMLWithSAX(String xmlData) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            ContentHandler handler = new ContentHandler();
            // 将ContentHandler的实例设置到XMLReader中
            xmlReader.setContentHandler(handler);
            // 开始执行解析
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //使用JSONObject解析json的方法
    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                Log.d("MainActivity", "id is " + id);
                Log.d("MainActivity", "name is " + name);
                Log.d("MainActivity", "version is " + version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //使用GSON解析json的方法
    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<App> appList = gson.fromJson(jsonData, new TypeToken<List<App>>() {
        }.getType());
        for (App app : appList) {
            Log.d("MainActivity", "id is " + app.getId());
            Log.d("MainActivity", "name is " + app.getName());
            Log.d("MainActivity", "version is " + app.getVersion());
        }

    }

}