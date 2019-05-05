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
        System.out.println("如有Bug请去博客反馈与我，持续更新：www.hellohao.cn");
        return "index";
    }

    //首页最新更新
    @RequestMapping(value="/GetWallpapers",method = RequestMethod.POST)
    @ResponseBody
    public String GetWallpaper(Integer start, Integer count,Integer category) {
        String wallpaper = "";
        wallpaper = wallpaperService.GetWallpaper(start, count,category);
        return wallpaper;
    }


    //获取所有分类
    @RequestMapping(value="/GetCategory",method = RequestMethod.POST)
    @ResponseBody
    public String GetCategory(){
        String category = wallpaperService.GetWallpaperCategory();
        return category;

    }
    //如有Bug请去博客反馈与我，持续更新：www.hellohao.cn
}
