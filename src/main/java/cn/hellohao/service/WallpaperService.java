package cn.hellohao.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WallpaperService {
    public String GetWallpaper(Integer start, Integer count) {
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection httpConn = null;
        BufferedReader reader = null;
        try {
            String strURL = "http://wallpaper.apc.360.cn/index.php?c=WallPaper&a=getAppsByOrder&order=create_time&start=" + start + "&count=" + count + "&from=360chrome";
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
}
