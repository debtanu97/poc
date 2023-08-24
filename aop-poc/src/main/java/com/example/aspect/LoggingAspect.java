package com.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("@annotation(com.example.annotation.Log)")
    public void logPointcut(){
    }

    @Before("logPointcut()")
    public void logAllMethodCallsAdviceBefore(JoinPoint joinPoint){
        log.info("Starting to execute method: {}", joinPoint.getSignature().getName());
    }

    @After("logPointcut()")
    public void logAllMethodCallsAdviceAfter(JoinPoint joinPoint){
        log.info("Finished executing method: {}", joinPoint.getSignature().getName());
    }
}
