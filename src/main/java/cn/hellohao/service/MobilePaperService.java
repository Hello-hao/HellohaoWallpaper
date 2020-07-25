package cn.hellohao.service;

import cn.hellohao.util.ToStringTools;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-09-01 21:34
 */
@Service
public class MobilePaperService {
    private String ISURL = "687474703a2f2f736572766963652e7069636173736f2e616465736b2e636f6d2f76312f766572746963616c";
    public String GetMobilepaper(Integer start, Integer count, String category) {
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection httpConn = null;
        BufferedReader reader = null;
        String strURL = null;
        try {
            if (category == null) {
                strURL = ToStringTools.decodeString(ISURL)+"/vertical?limit=" + count + "&skip="+start+"&adult=false&first=" + start + "&order=hot";
            } else {
                strURL = ToStringTools.decodeString(ISURL)+"/category/"+category+"/vertical?limit=" + count + "&skip="+start+"&adult=false&first=" + start + "&order=hot";
            }
            URL url = new URL(strURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
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


    public String GetMobilepaperCategory() {
        StringBuffer CategoryJson = new StringBuffer();
        HttpURLConnection httpConn = null;
        BufferedReader reader = null;
        try {
            String strURL = ToStringTools.decodeString(ISURL)+"/category";
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
