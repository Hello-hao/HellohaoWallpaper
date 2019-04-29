package cn.hellohao.controller;

import cn.hellohao.service.WallpaperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WallpaperController {
    @Autowired
    private WallpaperService wallpaperService;
//Hellohao编写修改
    @RequestMapping("/")
    public String index() {
        //return "wallpaper";
        System.out.println("blog:www.hellohao.cn");
        return "index";
    }

    @RequestMapping(value="/GetWallpapers",method = RequestMethod.POST)
    @ResponseBody
    public String GetWallpaper(Integer start, Integer count) {
        String wallpaper = wallpaperService.GetWallpaper(start, count);
        return wallpaper;
    }
}
