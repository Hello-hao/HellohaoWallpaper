package cn.hellohao.util;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AfterServiceStarted implements ApplicationRunner {

    /**
     * 会在服务启动完成后立即执行
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Print.Normal("______________________________________________");
        Print.Normal("              HelloWallpaper                ");
        Print.Normal("     Successful startup of the program      ");
        Print.Normal("     is OK!  Open http://your ip:port       ");
        Print.Normal("______________________________________________");

    }



}