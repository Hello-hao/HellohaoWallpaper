package cn.hellohao.service;

import cn.hellohao.util.ToStringTools;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


@Service
public class WallpaperService {
    private String ISURL ="687474703a2f2f77616c6c70617065722e6170632e3336302e636e2f696e6465782e706870";
    public String GetWallpaper(Integer start, Integer count, Integer category) {
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection httpConn = null;
        BufferedReader reader = null;
        String strURL = null;
        try {
            if (category < 1) {
                strURL = ToStringTools.decodeString(ISURL)+"?c=WallPaper&a=getAppsByOrder&order=create_time&start=" + start + "&count=" + count + "&from=360chrome";
            } else {
                strURL = ToStringTools.decodeString(ISURL)+"?c=WallPaper&a=getAppsByCategory&cid=" + category + "&start=" + start + "&count=" + count + "&from=360chrome";
            }

            URL url = new URL(strURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            //httpConn.setRequestProperty("Authorization", author);
            httpConn.connect();
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    public String GetWallpaperCategory() {
        StringBuffer CategoryJson = new StringBuffer();
        HttpURLConnection httpConn = null;
        BufferedReader reader = null;
        try {
            String strURL = ToStringTools.decodeString(ISURL)+"?c=WallPaper&a=getAllCategoriesV2";
            URL url = new URL(strURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                CategoryJson.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return CategoryJson.toString();
    }
}
