package cn.hellohao.test;

import java.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Testhttp {
    public static void main(String[] args) throws Exception {

        StringBuffer buffer = new StringBuffer();
        HttpURLConnection httpConn = null;
        BufferedReader reader = null;
        try {
            //url远程接口
            String strURL = "http://wallpaper.apc.360.cn/index.php?c=WallPaper&a=getAppsByOrder&order=create_time&start=0&count=3&from=360chrome";

            //原先使用的时com.sun.org.apache.xml.internal.security.utils.Base64，这个包虽然在jdk中，
            //但并不是标准的包，所以在gradle编译打包时总是无法引入改包，所以不用这个包
//            String author = "Basic " + Base64.encode((username+":"+ password).getBytes());
            //使用jdk1.8中的java.util.Base64来对字符串加密
            //String author = "Basic " + Base64.getEncoder().encodeToString((username+":"+ password).getBytes());

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
        System.out.println("接口返回值："+buffer.toString());
    }


}
