package com.zx.producer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    public static Object getBean(Class c){
        return applicationContext.getBean(c);
    }

    public static <T>T getBean(String s, Class<T> queueClass) {
        return (T)getBean(s);
    }

    public static <T> boolean registerBean(String beanName, T bean) {
        // 将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext context = (ConfigurableApplicationContext)   SpringContextUtil.getApplicationContext();
        // 将bean对象注册到bean工厂
        context.getBeanFactory().registerSingleton(beanName, bean);
        log.debug("【SpringContextUtil】注册实例“" + beanName + "”到spring容器：" + bean);
        return true;
    }
}
