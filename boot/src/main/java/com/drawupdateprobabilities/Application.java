package com.drawupdateprobabilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext cac = SpringApplication.run(Application.class, args);
        // displayAllBeans(cac);
        cac.close(); // Make a single execution, like old java projects
        log.info("Finished #######");
    }

    public static void displayAllBeans(ConfigurableApplicationContext cac) {
        String[] allBeanNames = cac.getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
            log.info("Bean:" + beanName);
        }
    }

}