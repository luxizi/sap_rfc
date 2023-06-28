package com.weimeizi.rfc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Classname Application
 * @Description TODO
 * @Date 2023-05-31 10:26
 * @Author by 03126
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        System.exit(SpringApplication.exit(run));
    }
}
