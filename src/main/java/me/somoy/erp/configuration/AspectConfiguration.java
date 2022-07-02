package me.somoy.erp.configuration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Aspect
@Configuration
@EnableAspectJAutoProxy
public class AspectConfiguration {
    Logger logger = LoggerFactory.getLogger(AspectConfiguration.class);

    @After("execution(* me.somoy.erp.controller.*.*(..))")
    public void afterExecution(JoinPoint joinPoint) {
        logger.info("Controller Execution Completed: {}",joinPoint);
    }
}
