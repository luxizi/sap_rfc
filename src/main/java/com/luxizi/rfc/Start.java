package com.luxizi.rfc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.luxizi.rfc.mapper.MaxComputeMapper;

import java.lang.reflect.Method;
import java.util.TimeZone;

/**
 * @Classname Start
 * @Description TODO
 * @Date 2023-05-31 10:49
 * @Author by 03126
 */
@Component
public class Start implements ApplicationRunner {

    //如果是开发环境则值为dev，否则为启动的类名
    @Value("${function:dev}")
    private String function;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //如果启动命令不含function字段，则赋值为test，代表是本地环境测试运行，则跳过函数执行
        String DEV = "dev";
        System.out.println("function:"+function);
        //初始化函数运行时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        if (DEV.equals(this.function)) {
            MaxComputeMapper mapper = MaxComputeMapper.getInstance(true);
        }else {
            MaxComputeMapper mapper = MaxComputeMapper.getInstance(false);
            //执行传入就参数的函数
            System.out.println("The running function is:" + this.function);
            Class<?> aClass = Class.forName(this.function);
            Object o = aClass.newInstance();
            Method run = aClass.getMethod("run");
            run.invoke(o);
        }
    }
}
