# HellohaoWallpaper
基于SpringBoot的高清壁纸图片站。


（如果不会可以用我打包好的jar包直接部署就可以。在`/成品jar包下`）
![t2_副本.jpg](https://i.loli.net/2019/05/18/5cdee4559157f87215.jpg)
![t1_副本.jpg](https://i.loli.net/2019/05/18/5cdee455b4b2577616.jpg)
**打包：**

用maven命令打成jar包就可以。
```shell
mvn clean
mvn package
```

**部署：**
然后把jar包放到你服务器的某个目录下，进入此目录然后执行即可部署完成
```shell
java -jar hellowallpaper.jar
```

然后访问：你的域名:8089即可访问,这样，你的网站就部署成功了。
**如果你想修改端口进入项目目录/src/main/resources在application.properties修改即可**

