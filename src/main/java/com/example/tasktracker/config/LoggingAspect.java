package com.example.tasktracker.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.example.tasktracker.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering: {} with args={}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @AfterReturning(
            pointcut = "execution(* com.example.tasktracker.service.*.*(..))",
            returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("Exiting: {} with result={}",
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.tasktracker.service.*.*(..))",
            throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        log.error("Exception in {}: {}",
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }
}