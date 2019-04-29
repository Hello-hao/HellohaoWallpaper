package cn.hellohao.controller;

import cn.hellohao.service.WallpaperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WallpaperController {
    @Autowired
    private WallpaperService wallpaperService;

    @RequestMapping("/")
    public String index(){
        //return "wallpaper";
        return "index";
    }

    @RequestMapping("/GetWallpapers")
    @ResponseBody
    public String GetWallpaper(Integer start,Integer count){

        String wallpaper = wallpaperService.GetWallpaper(start,count);

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        com.alibaba.fastjson.JSONObject imgjo= com.alibaba.fastjson.JSONObject.parseObject(wallpaper);
        //可以使用parseObject(params，Class<T> clazz)直接转换成需要的Bean
        JSONArray imgjson = JSONArray.fromObject(imgjo.getString("data"));
        for(int i=0;i<imgjson.size();i++){
            JSONObject job = imgjson.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            jsonObject.put("ImgUrl",job.get("url_mobile"));
            jsonObject.put("ImgTag",job.get("utag"));
            jsonArray.add(i,jsonObject);
        }
        System.out.println(jsonArray);
        return jsonArray.toString();
    }
}
